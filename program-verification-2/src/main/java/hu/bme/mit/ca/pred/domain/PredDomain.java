package hu.bme.mit.ca.pred.domain;

import static com.google.common.base.Preconditions.checkNotNull;
import static hu.bme.mit.theta.core.type.booltype.BoolExprs.Not;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import hu.bme.mit.theta.cfa.CFA.Edge;
import hu.bme.mit.theta.core.model.Valuation;
import hu.bme.mit.theta.core.type.Expr;
import hu.bme.mit.theta.core.type.booltype.BoolType;
import hu.bme.mit.theta.core.utils.PathUtils;
import hu.bme.mit.theta.core.utils.StmtUnfoldResult;
import hu.bme.mit.theta.core.utils.StmtUtils;
import hu.bme.mit.theta.core.utils.VarIndexing;
import hu.bme.mit.theta.solver.Solver;
import hu.bme.mit.theta.solver.z3.Z3SolverFactory;

public final class PredDomain {
	private final Solver solver;
	private final Solver liftSolver;

	private PredDomain() {
		solver = Z3SolverFactory.getInstance().createSolver();
		liftSolver = Z3SolverFactory.getInstance().createSolver();
	}

	public static PredDomain create() {
		return new PredDomain();
	}

	/**
	 * Returns true if the predicate assignment of the state is satisfied by every valuation.
	 * (So state is semantically equivalent to True)
	 */
	public boolean isTop(final PredState state) {
		solver.push();
		solver.add(PathUtils.unfold(Not(state.toExpr()), 0));
		final boolean result = solver.check().isUnsat();
		solver.pop();
		return result;
	}

	/**
	 * Returns true if the predicate assignments of the state are not satisfiable.
	 * (So the state is semantically equivalent to False),
	 */
	public boolean isBottom(final PredState state) {
		solver.push();
		solver.add(PathUtils.unfold((state.toExpr()), 0));
		final boolean result = solver.check().isUnsat();
		solver.pop();
		return result;
	}

	/**
	 * Checks whether state1 is less abstract than state2, meaning that every concrete state
	 * contained in the abstract state state1 is also contained in the abstract state state2.
	 * In case of the predicate domain, this is equivalent to saying that "state1 implies state2".
	 */
	public boolean isLessAbstractThan(final PredState state1, final PredState state2) {
		solver.push();
		solver.add(PathUtils.unfold(state1.toExpr(), 0));
		solver.add(PathUtils.unfold(Not(state2.toExpr()), 0));
		final boolean isLeq = solver.check().isUnsat();
		solver.pop();
		return isLeq;
	}


	/**
	 * Lifts a valuation to the given precision: computes whether each of the predicates in the precision
	 * is true or false if the program variables are assigned to values as given in the valuation. As
	 * variables can have an unknown value in the valuation, the resulting PredState can also have predicates
	 * with unknown value.
	 */
	public PredState lift(final Valuation valuation, final PredPrecision precision) {
		checkNotNull(valuation);
		checkNotNull(precision);
		final Collection<Expr<BoolType>> statePreds = new LinkedList<>();

		final Expr<BoolType> stateExpr = PathUtils.unfold(valuation.toExpr(), 0);

		liftSolver.push();
		liftSolver.add(stateExpr);

		for (final Expr<BoolType> pred : precision.getPredicates()) {
			final Expr<BoolType> predExpr = PathUtils.unfold(pred, 0);

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

	/**
	 * Computes the successors of an abstract state with respect to a CFA edge (= applying the statement of the edge
	 * to the program variables).
	 * @param state State to get the successors of.
	 * @param precision Precision of the returned successor states.
	 * @param edge Edge to use.
	 * @return The collection of possible next states with the given precision.
	 */
	public Collection<PredState> getSuccStates(final PredState state, final PredPrecision precision, final Edge edge) {
		checkNotNull(state);
		checkNotNull(precision);
		checkNotNull(edge);

		final Collection<PredState> succStates = new ArrayList<>();

		final StmtUnfoldResult unfoldResult = StmtUtils.toExpr(edge.getStmt(), VarIndexing.all(0));
		final Collection<? extends Expr<BoolType>> edgeExprs = unfoldResult.getExprs();
		final VarIndexing indexing = unfoldResult.getIndexing();

		final Expr<BoolType> sourceExpr = PathUtils.unfold(state.toExpr(), 0);
		final Collection<Expr<BoolType>> transitionExprs = edgeExprs.stream().map(e -> PathUtils.unfold(e, 0))
				.collect(toList());

		solver.push();

		solver.add(sourceExpr);
		solver.add(transitionExprs);

		while (solver.check().isSat()) {
			final Valuation model = solver.getModel();
			final Valuation nextSuccStateVal = PathUtils.extractValuation(model, indexing);

			final PredState nextSuccState = lift(nextSuccStateVal, precision);
			succStates.add(nextSuccState);

			final Expr<BoolType> targetExpr = PathUtils.unfold(nextSuccState.toExpr(), indexing);
			solver.add(Not(targetExpr));
		}

		solver.pop();

		return succStates;
	}

}
