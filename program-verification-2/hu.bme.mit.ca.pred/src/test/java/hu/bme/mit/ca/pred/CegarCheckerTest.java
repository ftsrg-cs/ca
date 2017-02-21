package hu.bme.mit.ca.pred;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import hu.bme.mit.ca.pred.CegarChecker.SearchStrategy;
import hu.bme.mit.ca.pred.arg.ArgVisualizer;
import hu.bme.mit.theta.common.visualization.GraphvizWriter;
import hu.bme.mit.theta.formalism.cfa.CFA;

@RunWith(value = Parameterized.class)
public final class CegarCheckerTest {

	@Parameter(value = 0)
	public String filepath;

	@Parameter(value = 1)
	public boolean safe;

	@Parameters(name = "{index}: {0}, {1}")
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] {

				{ "src/test/resources/trivial/ca-ex_false.c", false },

				{ "src/test/resources/trivial/ca-ex-const_false.c", false },

				{ "src/test/resources/trivial/ca-lock_false.c", false },

				{ "src/test/resources/trivial/ca-nop_false.c", false },

				{ "src/test/resources/trivial/lock_true.c", true },

				{ "src/test/resources/trivial/counter_true.c", true }

				// { "src/test/resources/locks/locks9_true.c", true },
				//
				// { "src/test/resources/locks/locks10_true.c", true },
				//
				// { "src/test/resources/locks/locks14_false.c", false },
				//
				// { "src/test/resources/locks/locks15_false.c", false }

		});
	}

	@Test
	public void testCegar() {
		final CFA cfa = SourceToCfaTransformer.largeBlockEncoding(filepath);
		final SafetyChecker checker = CegarChecker.create(cfa, SearchStrategy.DEPTH_FIRST);
		final SafetyResult result = checker.check();

		if (safe) {
			assertTrue(result.isSafe());
			System.out
					.println(new GraphvizWriter().writeString(ArgVisualizer.visualize(result.asSafe().getRootNode())));
		} else {
			assertTrue(result.isUnsafe());
		}
	}

}
