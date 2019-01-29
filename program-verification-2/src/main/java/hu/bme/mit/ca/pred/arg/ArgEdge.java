package hu.bme.mit.ca.pred.arg;

import static com.google.common.base.Preconditions.checkNotNull;

import hu.bme.mit.ca.pred.domain.PredState;
import hu.bme.mit.theta.cfa.CFA.Edge;

public final class ArgEdge {
	private final ArgNode source;
	private final ArgNode target;
	private final Edge edge;

	private ArgEdge(final ArgNode source, final Edge edge, final PredState targetState) {
		this.source = checkNotNull(source);
		this.edge = checkNotNull(edge);
		checkNotNull(targetState);
		this.target = ArgNode.successor(this, targetState);
	}

	static ArgEdge create(final ArgNode source, final Edge edge, final PredState targetState) {
		return new ArgEdge(source, edge, targetState);
	}

	////

	public ArgNode getSource() {
		return source;
	}

	public ArgNode getTarget() {
		return target;
	}

	public Edge getEdge() {
		return edge;
	}
}
