package hu.bme.mit.ca.pred.arg;

import static java.util.stream.Collectors.toList;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import hu.bme.mit.theta.common.visualization.EdgeAttributes;
import hu.bme.mit.theta.common.visualization.Graph;
import hu.bme.mit.theta.common.visualization.LineStyle;
import hu.bme.mit.theta.common.visualization.NodeAttributes;

public final class ArgVisualizer {
	private static final LineStyle COVER_EDGE_STYLE = LineStyle.DASHED;
	private static final LineStyle SUCC_EDGE_STYLE = LineStyle.NORMAL;
	private static final String ARG_LABEL = "";
	private static final String ARG_ID = "arg";
	private static final String NODE_ID_PREFIX = "node_";
	private static final Color FILL_COLOR = Color.WHITE;
	private static final Color LINE_COLOR = Color.BLACK;
	private static final String PHANTOM_INIT_ID = "phantom_init";

	private ArgVisualizer() {
	}

	public static Graph visualize(final ArgNode rootNode) {
		final Graph graph = new Graph(ARG_ID, ARG_LABEL);

		final Map<ArgNode, Integer> nodeToId = new HashMap<>();

		traverse(graph, rootNode, nodeToId);
		final NodeAttributes nAttributes = NodeAttributes.builder().label("").fillColor(FILL_COLOR)
				.lineColor(FILL_COLOR).lineStyle(SUCC_EDGE_STYLE).peripheries(1).build();

		graph.addNode(PHANTOM_INIT_ID + nodeToId.get(rootNode), nAttributes);
		final EdgeAttributes eAttributes = EdgeAttributes.builder().label("").color(LINE_COLOR)
				.lineStyle(SUCC_EDGE_STYLE).build();
		graph.addEdge(PHANTOM_INIT_ID + nodeToId.get(rootNode), NODE_ID_PREFIX + nodeToId.get(rootNode), eAttributes);

		return graph;
	}

	private static void traverse(final Graph graph, final ArgNode node, final Map<ArgNode, Integer> nodeToId) {
		if (nodeToId.containsKey(node)) {
			return;
		} else {
			nodeToId.put(node, nodeToId.size());
		}
		final String nodeId = NODE_ID_PREFIX + nodeToId.get(node);
		final LineStyle lineStyle = SUCC_EDGE_STYLE;

		final NodeAttributes nAttributes = NodeAttributes.builder()
				.label(node.getLoc().getName() + "\\n"
						+ String.join(",\\n",
								node.getState().getPredicates().stream().map(e -> e.toString()).collect(toList())))
				.fillColor(FILL_COLOR).lineColor(LINE_COLOR).lineStyle(lineStyle).peripheries(1).build();

		graph.addNode(nodeId, nAttributes);

		for (final ArgEdge edge : node.getOutEdges()) {
			traverse(graph, edge.getTarget(), nodeToId);
			final String sourceId = NODE_ID_PREFIX + nodeToId.get(edge.getSource());
			final String targetId = NODE_ID_PREFIX + nodeToId.get(edge.getTarget());
			final EdgeAttributes eAttributes = EdgeAttributes.builder()
					.label(edge.getEdge().getStmt().toString())
					.color(LINE_COLOR).lineStyle(SUCC_EDGE_STYLE).build();
			graph.addEdge(sourceId, targetId, eAttributes);
		}

		if (node.getCoveringNode().isPresent()) {
			traverse(graph, node.getCoveringNode().get(), nodeToId);
			final String sourceId = NODE_ID_PREFIX + nodeToId.get(node);
			final String targetId = NODE_ID_PREFIX + nodeToId.get(node.getCoveringNode().get());
			final EdgeAttributes eAttributes = EdgeAttributes.builder().label("").color(LINE_COLOR)
					.lineStyle(COVER_EDGE_STYLE).weight(0).build();
			graph.addEdge(sourceId, targetId, eAttributes);
		}
	}

}
