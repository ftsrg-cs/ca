package hu.bme.mit.ca.bmc;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.ImmutableList;

import hu.bme.mit.theta.formalism.cfa.CfaEdge;
import hu.bme.mit.theta.formalism.cfa.CfaLoc;

final class Path {

	private final CfaLoc loc;
	private final List<CfaEdge> edges;

	private Path(final CfaLoc loc) {
		this.loc = checkNotNull(loc);
		this.edges = ImmutableList.of();
	}

	private Path(final List<? extends CfaEdge> edges) {
		checkNotNull(edges);
		checkArgument(!edges.isEmpty());

		checkPath(edges);

		this.loc = edges.get(0).getSource();
		this.edges = ImmutableList.copyOf(edges);
	}

	private static void checkPath(final List<? extends CfaEdge> edges) {
		final Iterator<? extends CfaEdge> iterator = edges.iterator();
		CfaEdge currentEdge = iterator.next();
		CfaLoc currentLoc = null;
		while (iterator.hasNext()) {
			currentLoc = currentEdge.getTarget();
			currentEdge = iterator.next();
			checkArgument(currentLoc.equals(currentEdge.getSource()));
		}
	}

	public static Path of(final CfaLoc loc) {
		return new Path(loc);
	}

	public static Path of(final List<? extends CfaEdge> edges) {
		return new Path(edges);
	}

	public static Path of(final CfaEdge... edges) {
		return new Path(Arrays.asList(edges));
	}

	public CfaLoc getLoc() {
		return loc;
	}

	public List<CfaEdge> getEdges() {
		return edges;
	}

}
