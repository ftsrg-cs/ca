package hu.bme.mit.ca.pred.waitlist;

import java.util.ArrayDeque;
import java.util.Deque;

import hu.bme.mit.ca.pred.arg.ArgNode;

public final class LifoWaitlist implements Waitlist {

	private final Deque<ArgNode> nodes;

	public LifoWaitlist() {
		nodes = new ArrayDeque<>();
	}

	public static LifoWaitlist create() {
		return new LifoWaitlist();
	}

	@Override
	public void add(final ArgNode node) {
		nodes.add(node);
	}

	@Override
	public ArgNode remove() {
		return nodes.pop();
	}

	@Override
	public int size() {
		return nodes.size();
	}

}
