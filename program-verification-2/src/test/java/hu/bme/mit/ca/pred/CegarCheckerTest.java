package hu.bme.mit.ca.pred;

//import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import hu.bme.mit.ca.pred.CegarChecker.SearchStrategy;
//import hu.bme.mit.ca.pred.arg.ArgVisualizer;
import hu.bme.mit.theta.cfa.CFA;
import hu.bme.mit.theta.cfa.dsl.CfaDslManager;
//import hu.bme.mit.theta.common.visualization.writer.GraphvizWriter;

@RunWith(value = Parameterized.class)
public final class CegarCheckerTest {

	/*
	These are parameterized tests: you will see one test run for each element of the list
	returned by the data() method. This method returns a list of (filepath, safe) pairs,
	specifying the two parameters of the tests. The filepath is the path to the file describing
	the input CFA, and the boolean parameter safe specifies whether the expected output is safe
	or unsafe.
	 */

	@Parameter(value = 0)
	public String filepath;

	@Parameter(value = 1)
	public boolean safe;

	@Parameters(name = "{index}: {0}, {1}")
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] {

				{ "src/test/resources/ca-ex_false.cfa", false },

				{ "src/test/resources/counter5_false.cfa", false },

				{ "src/test/resources/counter5_true.cfa", true },

				{ "src/test/resources/gcd_true.cfa", true },

				{ "src/test/resources/locking_true.cfa", true },

		});
	}

	@Test
	public void test() throws IOException {
		final InputStream inputStream = new FileInputStream(filepath);
		final CFA cfa = CfaDslManager.createCfa(inputStream);
		final SafetyChecker checker = CegarChecker.create(cfa, SearchStrategy.DEPTH_FIRST);

//		Uncomment after implementation!
//		final SafetyResult result = checker.check();
//		if (safe) {
//			assertTrue(result.isSafe());
//			System.out.println(
//					GraphvizWriter.getInstance().writeString(ArgVisualizer.visualize(result.asSafe().getRootNode())));
//		} else {
//			assertTrue(result.isUnsafe());
//		}
	}

}
