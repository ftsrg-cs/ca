package hu.bme.mit.ca.pred;

import static com.google.common.base.Preconditions.checkNotNull;

import hu.bme.mit.ca.pred.waitlist.FifoWaitlist;
import hu.bme.mit.ca.pred.waitlist.LifoWaitlist;
import hu.bme.mit.ca.pred.waitlist.Waitlist;
import hu.bme.mit.theta.cfa.CFA;

public final class CegarChecker implements SafetyChecker {
	private final Abstractor abstractor;
	private final Refiner refiner;

	private CegarChecker(final CFA cfa, final SearchStrategy strategy) {
		checkNotNull(cfa);
		checkNotNull(strategy);
		abstractor = Abstractor.create(cfa, strategy);
		refiner = Refiner.create();
	}

	public static CegarChecker create(final CFA cfa, final SearchStrategy strategy) {
		return new CegarChecker(cfa, strategy);
	}

	@Override
	public SafetyResult check() {
		// TODO Implement the main CEGAR loop here.
		throw new UnsupportedOperationException("TODO: check method not implemented");
	}

	public enum SearchStrategy {
		BREADTH_FIRST {
			@Override
			Waitlist createWaitlist() {
				return FifoWaitlist.create();
			}
		},

		DEPTH_FIRST {
			@Override
			Waitlist createWaitlist() {
				return LifoWaitlist.create();
			}
		};

		abstract Waitlist createWaitlist();
	}

}
