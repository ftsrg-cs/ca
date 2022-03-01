package hu.bme.mit.ca.pred;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collection;

import hu.bme.mit.ca.pred.CegarChecker.SearchStrategy;
import hu.bme.mit.ca.pred.arg.ArgNode;
import hu.bme.mit.ca.pred.domain.PredDomain;
import hu.bme.mit.ca.pred.domain.PredPrecision;
import hu.bme.mit.ca.pred.waitlist.Waitlist;
import hu.bme.mit.theta.cfa.CFA;

final class Abstractor {
	private final CFA cfa;
	private final SearchStrategy strategy;
	private final PredDomain domain;

	private Abstractor(final CFA cfa, final SearchStrategy strategy) {
		this.cfa = checkNotNull(cfa);
		this.strategy = checkNotNull(strategy);
		domain = PredDomain.create();
	}

	public static Abstractor create(final CFA cfa, final SearchStrategy strategy) {
		return new Abstractor(cfa, strategy);
	}

	////

	public AbstractionResult check(final PredPrecision precision) {
		return new AbstractionBuilder(precision).buildAbstraction();
	}

	private final class AbstractionBuilder {
		private final PredPrecision precision;
		private final Collection<ArgNode> reachedSet;
		private final Waitlist waitlist;

		public AbstractionBuilder(final PredPrecision precision) {
			this.precision = checkNotNull(precision);
			reachedSet = new ArrayList<>();
			waitlist = strategy.createWaitlist();
		}

		public AbstractionResult buildAbstraction() {
			// TODO Implement the abstract state space exploration here:
			//  	build an ARG using the expand and close methods;
			//		use the waitlist
			throw new UnsupportedOperationException("TODO: buildAbstraction method not implemented!");
		}

		private void close(final ArgNode node) {
			// TODO Implement cover checking here:
			//  	for each non-covered reached ARG node, check whether it can cover the node given in the argument,
			//		and if one is found, use the coverWith function of ArgNode to set the covering edge
			throw new UnsupportedOperationException("TODO: close method not implemented");
		}

		private void expand(final ArgNode node) {
			// TODO Implement the expansion of an ARG node here
			throw new UnsupportedOperationException("TODO: auto-generated method stub");
		}
	}

}
