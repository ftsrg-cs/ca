package hu.bme.mit.ca.pred.waitlist;

import hu.bme.mit.ca.pred.arg.ArgNode;

public interface Waitlist {

	void add(final ArgNode node);

	ArgNode remove();

	int size();

	default void addAll(final Iterable<? extends ArgNode> nodes) {
		nodes.forEach(this::add);
	}

	default boolean isEmpty() {
		return size() == 0;
	}

}
