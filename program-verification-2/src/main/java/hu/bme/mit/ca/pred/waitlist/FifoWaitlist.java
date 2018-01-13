package hu.bme.mit.ca.pred.waitlist;

import java.util.ArrayDeque;
import java.util.Queue;

import hu.bme.mit.ca.pred.arg.ArgNode;

public final class FifoWaitlist implements Waitlist {

	private final Queue<ArgNode> nodes;

	private FifoWaitlist() {
		nodes = new ArrayDeque<>();
	}

	public static FifoWaitlist create() {
		return new FifoWaitlist();
	}

	@Override
	public void add(final ArgNode node) {
		nodes.add(node);
	}

	@Override
	public ArgNode remove() {
		return nodes.remove();
	}

	@Override
	public int size() {
		return nodes.size();
	}

}
