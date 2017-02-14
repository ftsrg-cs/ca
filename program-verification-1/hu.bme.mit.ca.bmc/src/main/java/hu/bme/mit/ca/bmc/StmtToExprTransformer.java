package hu.bme.mit.ca.bmc;

import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.List;

import hu.bme.mit.theta.core.expr.Expr;
import hu.bme.mit.theta.core.stmt.Stmt;
import hu.bme.mit.theta.core.type.BoolType;
import hu.bme.mit.theta.core.utils.impl.PathUtils;
import hu.bme.mit.theta.core.utils.impl.StmtUtils;
import hu.bme.mit.theta.core.utils.impl.UnfoldResult;
import hu.bme.mit.theta.core.utils.impl.VarIndexing;

final class StmtToExprTransformer {

	public static Collection<Expr<? extends BoolType>> unfold(final List<? extends Stmt> stmts) {
		final UnfoldResult unfoldResult = StmtUtils.toExpr(stmts, VarIndexing.all(0));
		final Collection<? extends Expr<? extends BoolType>> exprs = unfoldResult.getExprs();
		final Collection<Expr<? extends BoolType>> result = exprs.stream().map(e -> PathUtils.unfold(e, 0))
				.collect(toList());
		return result;
	}

}
