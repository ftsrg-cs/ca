package hu.bme.mit.ca.pred;

import static hu.bme.mit.theta.core.decl.Decls.Const;
import static hu.bme.mit.theta.core.type.inttype.IntExprs.Eq;
import static hu.bme.mit.theta.core.type.inttype.IntExprs.Gt;
import static hu.bme.mit.theta.core.type.inttype.IntExprs.Int;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import hu.bme.mit.theta.core.model.Valuation;
import hu.bme.mit.theta.core.type.Expr;
import hu.bme.mit.theta.core.type.inttype.IntType;
import hu.bme.mit.theta.solver.Solver;
import hu.bme.mit.theta.solver.z3.Z3SolverFactory;

public final class FrameworkTest {

	@Test
	public void test() {
		final Expr<IntType> x = Const("x", Int()).getRef();
		final Expr<IntType> y = Const("y", Int()).getRef();

		final Solver solver = Z3SolverFactory.getInstace().createSolver();

		solver.add(Eq(x, Int(0)));
		solver.add(Gt(x, y));
		solver.check();

		assertTrue(solver.getStatus().isSat());

		final Valuation model = solver.getModel();

		System.out.println(model);
	}

}
