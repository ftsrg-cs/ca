package hu.bme.mit.ca.pred.arg;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import hu.bme.mit.ca.pred.domain.PredState;
import hu.bme.mit.theta.formalism.cfa.CFA.Edge;
import hu.bme.mit.theta.formalism.cfa.CFA.Loc;

public final class ArgNode {
	private final Loc loc;
	private final PredState state;

	private final Optional<ArgEdge> inEdge;
	private final Collection<ArgEdge> outEdges;

	private Optional<ArgNode> coveringNode;

	private ArgNode(final Optional<ArgEdge> inEdge, final Loc loc, final PredState state) {
		this.loc = checkNotNull(loc);
		this.state = checkNotNull(state);
		this.inEdge = checkNotNull(inEdge);
		this.outEdges = new ArrayList<>();
		this.coveringNode = Optional.empty();
		checkArgument(!inEdge.isPresent() || inEdge.get().getEdge().getTarget().equals(loc));
	}

	public static ArgNode root(final Loc loc, final PredState state) {
		return new ArgNode(Optional.empty(), loc, state);
	}

	static ArgNode successor(final ArgEdge edge, final PredState state) {
		return new ArgNode(Optional.of(edge), edge.getEdge().getTarget(), state);
	}

	////

	public Loc getLoc() {
		return loc;
	}

	public PredState getState() {
		return state;
	}

	////

	public Optional<ArgEdge> getInEdge() {
		return inEdge;
	}

	public Collection<ArgEdge> getOutEdges() {
		return Collections.unmodifiableCollection(outEdges);
	}

	////

	public Optional<ArgNode> getCoveringNode() {
		return coveringNode;
	}

	public void coverWith(final ArgNode node) {
		this.coveringNode = Optional.of(node);
	}

	public void uncover() {
		this.coveringNode = Optional.empty();
	}

	////

	public Optional<ArgNode> getParent() {
		return inEdge.map(ArgEdge::getSource);
	}

	public Collection<ArgNode> getChildren() {
		return outEdges.stream().map(ArgEdge::getTarget).collect(toList());
	}

	public boolean isCovered() {
		return coveringNode.isPresent();
	}

	public boolean isLeaf() {
		return outEdges.isEmpty();
	}

	////

	public ArgNode createChild(final Edge edge, final PredState targetState) {
		final ArgEdge outEdge = ArgEdge.create(this, edge, targetState);
		outEdges.add(outEdge);
		return outEdge.getTarget();
	}

}
