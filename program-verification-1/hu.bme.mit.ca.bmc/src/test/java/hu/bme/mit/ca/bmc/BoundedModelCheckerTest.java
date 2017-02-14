package hu.bme.mit.ca.bmc;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import hu.bme.mit.theta.formalism.cfa.CFA;

@RunWith(value = Parameterized.class)
public final class BoundedModelCheckerTest {
	private static final String PATH = "src/test/resources/trivial/";

	@Parameter(value = 0)
	public String filename;

	@Parameters(name = "{index}: {0}")
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] {

				{ "ca-ex_false.c" },

				{ "ca-ex-const_false.c" },

				{ "ca-lock_false.c" },

				{ "ca-nop_false.c" },

				{ "gcd0_false.c" },

				{ "gcd0-const_false.c" },

				{ "state-machine0_false.c" }

		});
	}

	@Test
	public void testTrivial() {
		final CFA cfa = SourceToCfaTransformer.largeBlockEncoding(PATH + filename);
		final SafetyChecker checker = new BoundedModelChecker(cfa, 20, 10);
		final SafetyStatus status = checker.check();

		assertTrue(status.isUnsafe());
		System.out.println(status.asUnsafe().getCounterexample());
	}

}
