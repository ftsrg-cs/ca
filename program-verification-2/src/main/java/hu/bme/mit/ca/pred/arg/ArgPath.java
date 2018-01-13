package hu.bme.mit.ca.pred.arg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ArgPath {
	private final List<ArgNode> nodes;
	private final List<ArgEdge> edges;

	public ArgPath(final ArgNode node) {
		final List<ArgNode> nodeList = new ArrayList<>();
		final List<ArgEdge> edgeList = new ArrayList<>();

		ArgNode running = node;
		nodeList.add(running);

		while (running.getInEdge().isPresent()) {
			final ArgEdge inEdge = running.getInEdge().get();
			running = inEdge.getSource();
			edgeList.add(0, inEdge);
			nodeList.add(0, running);
		}
		this.nodes = Collections.unmodifiableList(nodeList);
		this.edges = Collections.unmodifiableList(edgeList);
	}

	public static ArgPath to(final ArgNode node) {
		return new ArgPath(node);
	}

	public List<ArgNode> getNodes() {
		return nodes;
	}

	public List<ArgEdge> getEdges() {
		return edges;
	}

	public ArgNode getNode(final int i) {
		return nodes.get(i);
	}

	public ArgEdge getEdge(final int i) {
		return edges.get(i);
	}

}
