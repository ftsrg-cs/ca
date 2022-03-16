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

	/**
	 * Explores the abstract state space of the CFA model with the given precision (=set of predicates).
	 * Returns whether the result of the exploration is Unsafe or Safe.
	 */
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
			//		use the waitlist to store and retrieve the non-expanded reached nodes
			//		--> this makes the exploration strategy configurable
			//			(FIFO waitlist => BFS, LIFO waitlist => DFS)
			//
			//		The method should return Unsafe if an abstract state with the error location is found,
			//			with the reached error node provided in it as abstract counterexample.
			//		If no such state is found, the method should return Safe, with the root node
			//			of the ARG given as proof.
			//
			//		Start the exploration from the initial location of the CFA with fully unknown predicate
			//		values (= the top element of the predicate domain)
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
