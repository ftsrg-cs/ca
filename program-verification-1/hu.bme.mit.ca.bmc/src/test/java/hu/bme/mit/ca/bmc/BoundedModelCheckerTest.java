package hu.bme.mit.ca.bmc;

import static org.junit.Assert.assertEquals;

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

import hu.bme.mit.theta.formalism.cfa.CFA;
import hu.bme.mit.theta.formalism.cfa.dsl.CfaDslManager;

@RunWith(value = Parameterized.class)
public final class BoundedModelCheckerTest {

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

				{ "src/test/resources/gcd_false.cfa", false },

				{ "src/test/resources/locking_true.cfa", false },

		});
	}

	@Test
	public void test() throws IOException {
		final InputStream inputStream = new FileInputStream(filepath);
		final CFA cfa = CfaDslManager.createCfa(inputStream);
		final SafetyChecker checker = BoundedModelChecker.create(cfa, 20, 5);
		final SafetyResult result = checker.check();

		if (safe) {
			assertEquals(SafetyResult.UNKNOWN, result);
		} else {
			assertEquals(SafetyResult.UNSAFE, result);
		}

	}

}
