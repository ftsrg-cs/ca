package hu.bme.mit.ca.pred.domain;

import static com.google.common.base.Preconditions.checkNotNull;
import static hu.bme.mit.theta.core.expr.impl.Exprs.Not;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import hu.bme.mit.theta.core.expr.Expr;
import hu.bme.mit.theta.core.model.Model;
import hu.bme.mit.theta.core.model.impl.Valuation;
import hu.bme.mit.theta.core.type.BoolType;
import hu.bme.mit.theta.core.utils.impl.PathUtils;
import hu.bme.mit.theta.core.utils.impl.StmtUtils;
import hu.bme.mit.theta.core.utils.impl.UnfoldResult;
import hu.bme.mit.theta.core.utils.impl.VarIndexing;
import hu.bme.mit.theta.formalism.cfa.CfaEdge;
import hu.bme.mit.theta.solver.Solver;
import hu.bme.mit.theta.solver.z3.Z3SolverFactory;

public final class PredDomain {
	private final Solver solver;
	private final Solver liftSolver;

	private PredDomain() {
		solver = Z3SolverFactory.getInstace().createSolver();
		liftSolver = Z3SolverFactory.getInstace().createSolver();
	}

	public static PredDomain create() {
		return new PredDomain();
	}

	public boolean isTop(final PredState state) {
		solver.push();
		solver.add(PathUtils.unfold(Not(state.toExpr()), 0));
		final boolean result = solver.check().isUnsat();
		solver.pop();
		return result;
	}

	public boolean isBottom(final PredState state) {
		solver.push();
		solver.add(PathUtils.unfold(Not(state.toExpr()), 0));
		final boolean result = solver.check().isUnsat();
		solver.pop();
		return result;
	}

	public boolean isLessOrEqual(final PredState state1, final PredState state2) {
		solver.push();
		solver.add(PathUtils.unfold(state1.toExpr(), 0));
		solver.add(PathUtils.unfold(Not(state2.toExpr()), 0));
		final boolean isLeq = solver.check().isUnsat();
		solver.pop();
		return isLeq;
	}

	public PredState lift(final Valuation valuation, final PredPrecision precision) {
		checkNotNull(valuation);
		checkNotNull(precision);
		final Collection<Expr<? extends BoolType>> statePreds = new LinkedList<>();

		final Expr<? extends BoolType> stateExpr = PathUtils.unfold(valuation.toExpr(), 0);

		liftSolver.push();
		liftSolver.add(stateExpr);

		for (final Expr<? extends BoolType> pred : precision.getPredicates()) {
			final Expr<? extends BoolType> predExpr = PathUtils.unfold(pred, 0);

			liftSolver.push();
			liftSolver.add(Not(predExpr));
			if (liftSolver.check().isUnsat()) {
				statePreds.add(pred);
			} else {
				liftSolver.pop();
				liftSolver.push();
				liftSolver.add(predExpr);
				if (liftSolver.check().isUnsat()) {
					statePreds.add(Not(pred));
				}
			}
			liftSolver.pop();
		}

		liftSolver.pop();

		assert liftSolver.getAssertions().isEmpty();

		return PredState.of(statePreds);
	}

	public Collection<PredState> getSuccStates(final PredState state, final PredPrecision precision,
			final CfaEdge edge) {
		checkNotNull(state);
		checkNotNull(precision);
		checkNotNull(edge);

		final Collection<PredState> succStates = new ArrayList<>();

		final UnfoldResult unfoldResult = StmtUtils.toExpr(edge.getStmts(), VarIndexing.all(0));
		final Collection<? extends Expr<? extends BoolType>> edgeExprs = unfoldResult.getExprs();
		final VarIndexing indexing = unfoldResult.getIndexing();

		final Expr<? extends BoolType> sourceExpr = PathUtils.unfold(state.toExpr(), 0);
		final Collection<Expr<? extends BoolType>> transitionExprs = edgeExprs.stream().map(e -> PathUtils.unfold(e, 0))
				.collect(toList());

		solver.push();

		solver.add(sourceExpr);
		solver.add(transitionExprs);

		while (solver.check().isSat()) {
			final Model model = solver.getModel();
			final Valuation nextSuccStateVal = PathUtils.extractValuation(model, indexing);

			final PredState nextSuccState = lift(nextSuccStateVal, precision);
			succStates.add(nextSuccState);

			final Expr<? extends BoolType> targetExpr = PathUtils.unfold(nextSuccState.toExpr(), indexing);
			solver.add(Not(targetExpr));
		}

		solver.pop();

		return succStates;
	}

}
