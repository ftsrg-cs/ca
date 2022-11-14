package hu.bme.mit.ca.bmc;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.google.common.base.Stopwatch;

import hu.bme.mit.theta.cfa.CFA;
import hu.bme.mit.theta.core.stmt.Stmt;
import hu.bme.mit.theta.core.type.Expr;
import hu.bme.mit.theta.core.type.booltype.BoolType;
import hu.bme.mit.theta.solver.Solver;
import hu.bme.mit.theta.solver.z3.Z3SolverFactory;

public final class BoundedModelChecker implements SafetyChecker {

	private final CFA cfa;
	private final int bound;
	private final int timeout;

	private BoundedModelChecker(final CFA cfa, final int bound, final int timeout) {
		checkArgument(bound >= 0);
		checkArgument(timeout >= 0);

		this.cfa = checkNotNull(cfa);
		this.bound = bound;
		this.timeout = timeout;
	}

	public static BoundedModelChecker create(final CFA cfa, final int bound, final int timeout) {
		return new BoundedModelChecker(cfa, bound, timeout);
	}

	@Override
	public SafetyResult check() {
		final Stopwatch stopwatch = Stopwatch.createStarted();

		while (stopwatch.elapsed(TimeUnit.SECONDS) < timeout) {

			if (cfa.getErrorLoc().isEmpty())
				return SafetyResult.SAFE;
			final CFA.Loc ERROR_LOC = cfa.getErrorLoc().get();
			List<List<CFA.Edge>> paths =
					cfa.getInitLoc()
					.getOutEdges()
					.stream()
					.map(Collections::singletonList)
					.collect(Collectors.toList());
			for (int k = 1; k <= bound; k++) {
				List<List<CFA.Edge>> newPaths = new LinkedList<>();
				for (List<CFA.Edge> path :
						paths) {
					CFA.Loc currentLocation = path.get(path.size() - 1).getTarget();
					if (currentLocation == ERROR_LOC) {
						if (isPathSat(path)) {
							return SafetyResult.UNSAFE;
						}
					}
					currentLocation.getOutEdges().forEach(
							edge -> {
								List<CFA.Edge> newPath = new LinkedList<>(path);
								newPath.add(edge);
								newPaths.add(newPath);
							}
					);
				}
				paths = newPaths;
			}

			return SafetyResult.UNKNOWN;
		}

		stopwatch.stop();

		return SafetyResult.TIMEOUT;
	}

	private boolean isPathSat(Collection<CFA.Edge> path) {
		Collection<Expr<BoolType>> expressions = new ArrayList<>();
		List<Stmt> statements = path.stream()
				.map(CFA.Edge::getStmt)
				.collect(Collectors.toList());
		Solver solver = Z3SolverFactory.getInstance().createSolver();
		solver.add(StmtToExprTransformer.unfold(statements));
		solver.check();
		return solver.getStatus().isSat();
	}

}
