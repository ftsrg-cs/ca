package hu.bme.mit.ca.pred.domain;

import static hu.bme.mit.theta.core.utils.impl.ExprUtils.ponate;
import static java.util.Collections.emptySet;

import java.util.Collection;
import java.util.HashSet;

import com.google.common.collect.ImmutableSet;

import hu.bme.mit.theta.common.ObjectUtils;
import hu.bme.mit.theta.core.expr.BoolLitExpr;
import hu.bme.mit.theta.core.expr.Expr;
import hu.bme.mit.theta.core.type.BoolType;

public final class PredPrecision {
	private static final PredPrecision EMTPY = PredPrecision.of(emptySet());

	private final Collection<Expr<? extends BoolType>> predicates;

	public PredPrecision(final Collection<? extends Expr<? extends BoolType>> predicates) {
		this.predicates = collectPredicatesFrom(predicates);
	}

	private static Collection<Expr<? extends BoolType>> collectPredicatesFrom(
			final Collection<? extends Expr<? extends BoolType>> predicates) {
		final ImmutableSet.Builder<Expr<? extends BoolType>> builder = ImmutableSet.builder();
		for (final Expr<? extends BoolType> predicate : predicates) {
			if (!(predicate instanceof BoolLitExpr)) {
				builder.add(ponate(predicate));
			}
		}
		return builder.build();
	}

	public static PredPrecision of(final Collection<? extends Expr<? extends BoolType>> predicates) {
		return new PredPrecision(predicates);
	}

	public static PredPrecision empty() {
		return EMTPY;
	}

	////

	public Collection<Expr<? extends BoolType>> getPredicates() {
		return predicates;
	}

	public PredPrecision join(final PredPrecision that) {
		final Collection<Expr<? extends BoolType>> predicates = new HashSet<>();
		predicates.addAll(this.predicates);
		predicates.addAll(that.predicates);
		return PredPrecision.of(predicates);
	}

	////

	@Override
	public String toString() {
		return ObjectUtils.toStringBuilder(getClass().getSimpleName()).addAll(predicates).toString();
	}

}
