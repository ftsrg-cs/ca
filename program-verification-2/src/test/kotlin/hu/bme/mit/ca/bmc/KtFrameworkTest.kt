package hu.bme.mit.ca.bmc

import hu.bme.mit.theta.core.decl.Decls.Const
import hu.bme.mit.theta.core.type.inttype.IntExprs.Eq
import hu.bme.mit.theta.core.type.inttype.IntExprs.Gt
import hu.bme.mit.theta.core.type.inttype.IntExprs.Int
import hu.bme.mit.theta.solver.z3.Z3SolverFactory
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.*

class FrameworkTest {

	@Test
	fun test() {
		val x = Const("x", Int()).ref
		val y = Const("y", Int()).ref

		val solver = Z3SolverFactory.getInstace().createSolver()

		solver.add(Eq(x, Int(0)));
		solver.add(Gt(x, y));
		solver.check()

		assertTrue(solver.status.isSat)

		val model = solver.model

		println(model)
	}

}
