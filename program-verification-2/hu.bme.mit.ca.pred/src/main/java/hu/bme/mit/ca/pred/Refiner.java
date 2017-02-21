package hu.bme.mit.ca.pred;

import static hu.bme.mit.theta.core.expr.impl.Exprs.True;
import static hu.bme.mit.theta.core.utils.impl.ExprUtils.getAtoms;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import hu.bme.mit.ca.pred.arg.ArgEdge;
import hu.bme.mit.ca.pred.arg.ArgNode;
import hu.bme.mit.ca.pred.arg.ArgPath;
import hu.bme.mit.ca.pred.domain.PredPrecision;
import hu.bme.mit.theta.core.expr.Expr;
import hu.bme.mit.theta.core.type.BoolType;
import hu.bme.mit.theta.core.utils.impl.PathUtils;
import hu.bme.mit.theta.core.utils.impl.StmtUtils;
import hu.bme.mit.theta.core.utils.impl.UnfoldResult;
import hu.bme.mit.theta.core.utils.impl.VarIndexing;
import hu.bme.mit.theta.solver.Interpolant;
import hu.bme.mit.theta.solver.ItpMarker;
import hu.bme.mit.theta.solver.ItpPattern;
import hu.bme.mit.theta.solver.ItpSolver;
import hu.bme.mit.theta.solver.z3.Z3SolverFactory;

final class Refiner {

	private final ItpSolver solver;

	private Refiner() {
		solver = Z3SolverFactory.getInstace().createItpSolver();
	}

	public static Refiner create() {
		return new Refiner();
	}

	public RefinementResult refine(final ArgNode errorNode) {
		solver.push();

		final ArgPath path = ArgPath.to(errorNode);
		final int nodeCount = path.getNodes().size();

		final List<ItpMarker> markers = createMarkers(nodeCount + 1);
		final ItpPattern pattern = solver.createSeqPattern(markers);
		final List<VarIndexing> indexings = new ArrayList<>(nodeCount);

		indexings.add(VarIndexing.all(0));

		solver.add(markers.get(0), PathUtils.unfold(path.getNode(0).getState().toExpr(), indexings.get(0)));

		for (int i = 1; i < nodeCount; i++) {
			final VarIndexing sourceIndexing = indexings.get(i - 1);
			final ArgEdge edge = path.getEdge(i - 1);

			final UnfoldResult unfoldResult = StmtUtils.toExpr(edge.getEdge().getStmts(), VarIndexing.all(0));
			final VarIndexing offset = unfoldResult.getIndexing();

			final VarIndexing targetIndexing = sourceIndexing.add(offset);
			final Collection<? extends Expr<? extends BoolType>> edgeExprs = unfoldResult.getExprs();

			final Collection<Expr<? extends BoolType>> transitionExprs = edgeExprs.stream()
					.map(e -> PathUtils.unfold(e, sourceIndexing)).collect(toList());
			final Expr<? extends BoolType> targetExpr = PathUtils.unfold(path.getNode(i).getState().toExpr(),
					targetIndexing);

			indexings.add(targetIndexing);
			solver.add(markers.get(i), transitionExprs);
			solver.add(markers.get(i), targetExpr);
		}

		solver.add(markers.get(nodeCount), True());

		solver.check();
		final boolean concretizable = solver.getStatus().isSat();

		final RefinementResult result;

		if (concretizable) {
			result = RefinementResult.failure(errorNode);
		} else {
			final List<Expr<? extends BoolType>> interpolants = new ArrayList<>();
			final Interpolant interpolant = solver.getInterpolant(pattern);
			for (int i = 0; i < markers.size() - 1; i++) {
				interpolants.add(PathUtils.foldin(interpolant.eval(markers.get(i)), indexings.get(i)));
			}
			final Collection<Expr<? extends BoolType>> atoms = interpolants.stream().flatMap(e -> getAtoms(e).stream())
					.collect(toSet());
			final PredPrecision precision = PredPrecision.of(atoms);
			result = RefinementResult.success(precision);
		}

		solver.pop();
		return result;
	}

	private List<ItpMarker> createMarkers(final int count) {
		final List<ItpMarker> markers = new ArrayList<>(count);
		for (int i = 0; i < count; i++) {
			markers.add(solver.createMarker());
		}
		return markers;
	}

}
