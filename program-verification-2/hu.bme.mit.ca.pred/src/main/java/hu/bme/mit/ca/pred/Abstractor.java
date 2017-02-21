package hu.bme.mit.ca.pred;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collection;

import hu.bme.mit.ca.pred.CegarChecker.SearchStrategy;
import hu.bme.mit.ca.pred.arg.ArgNode;
import hu.bme.mit.ca.pred.domain.PredDomain;
import hu.bme.mit.ca.pred.domain.PredPrecision;
import hu.bme.mit.ca.pred.domain.PredState;
import hu.bme.mit.ca.pred.waitlist.Waitlist;
import hu.bme.mit.theta.formalism.cfa.CFA;

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
		private final ArgNode rootNode;

		public AbstractionBuilder(final PredPrecision precision) {
			this.precision = checkNotNull(precision);
			reachedSet = new ArrayList<>();
			waitlist = strategy.createWaitlist();
			rootNode = ArgNode.root(cfa.getInitLoc(), PredState.top());

			reachedSet.add(rootNode);
			waitlist.add(rootNode);
		}

		public AbstractionResult buildAbstraction() {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException("TODO: auto-generated method stub");
		}

		private void close(final ArgNode node) {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException("TODO: auto-generated method stub");
		}

		private void expand(final ArgNode node) {			
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException("TODO: auto-generated method stub");
		}
	}

}
