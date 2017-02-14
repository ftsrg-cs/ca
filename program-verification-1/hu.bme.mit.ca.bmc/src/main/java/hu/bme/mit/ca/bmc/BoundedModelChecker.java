package hu.bme.mit.ca.bmc;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

import hu.bme.mit.theta.formalism.cfa.CFA;

final class BoundedModelChecker implements SafetyChecker {

	private final CFA cfa;
	private final int bound;
	private final int timeout;

	public BoundedModelChecker(final CFA cfa, final int bound, final int timeout) {
		checkArgument(bound >= 0);
		checkArgument(timeout >= 0);

		this.cfa = checkNotNull(cfa);
		this.bound = bound;
		this.timeout = timeout;
	}

	@Override
	public SafetyStatus check() {
		final Stopwatch stopwatch = Stopwatch.createStarted();

		while (stopwatch.elapsed(TimeUnit.SECONDS) < timeout) {
			// TODO Implement bounded model checker:
			// by building an unwinding of the program,
			// search for error paths with length not greater than the bound,
			// and check their feasibility using the SMT solver

			// TODO Auto-generated method stub
			throw new UnsupportedOperationException("TODO: auto-generated method stub");
		}

		stopwatch.stop();

		return SafetyStatus.timeout(stopwatch.elapsed(TimeUnit.SECONDS));
	}

}
