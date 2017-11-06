package hu.bme.mit.ca.bmc;

import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.List;

import hu.bme.mit.theta.core.stmt.Stmt;
import hu.bme.mit.theta.core.type.Expr;
import hu.bme.mit.theta.core.type.booltype.BoolType;
import hu.bme.mit.theta.core.utils.PathUtils;
import hu.bme.mit.theta.core.utils.StmtUnfoldResult;
import hu.bme.mit.theta.core.utils.StmtUtils;
import hu.bme.mit.theta.core.utils.VarIndexing;

final class StmtToExprTransformer {

	public static Collection<Expr<BoolType>> unfold(final List<? extends Stmt> stmts) {
		final StmtUnfoldResult unfoldResult = StmtUtils.toExpr(stmts, VarIndexing.all(0));
		final Collection<? extends Expr<BoolType>> exprs = unfoldResult.getExprs();
		final Collection<Expr<BoolType>> result = exprs.stream().map(e -> PathUtils.unfold(e, 0)).collect(toList());
		return result;
	}

}
