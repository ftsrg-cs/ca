package hu.bme.mit.ca.pred.domain;

import static hu.bme.mit.theta.core.utils.ExprUtils.ponate;
import static java.util.Collections.emptySet;

import java.util.Collection;
import java.util.HashSet;

import com.google.common.collect.ImmutableSet;

import hu.bme.mit.theta.common.Utils;
import hu.bme.mit.theta.core.type.Expr;
import hu.bme.mit.theta.core.type.booltype.BoolLitExpr;
import hu.bme.mit.theta.core.type.booltype.BoolType;

public final class PredPrecision {
	private static final PredPrecision EMTPY = PredPrecision.of(emptySet());

	private final Collection<Expr<BoolType>> predicates;

	public PredPrecision(final Collection<? extends Expr<BoolType>> predicates) {
		this.predicates = collectPredicatesFrom(predicates);
	}

	private static Collection<Expr<BoolType>> collectPredicatesFrom(
			final Collection<? extends Expr<BoolType>> predicates) {
		final ImmutableSet.Builder<Expr<BoolType>> builder = ImmutableSet.builder();
		for (final Expr<BoolType> predicate : predicates) {
			if (!(predicate instanceof BoolLitExpr)) {
				builder.add(ponate(predicate));
			}
		}
		return builder.build();
	}

	public static PredPrecision of(final Collection<? extends Expr<BoolType>> predicates) {
		return new PredPrecision(predicates);
	}

	public static PredPrecision empty() {
		return EMTPY;
	}

	////

	public Collection<Expr<BoolType>> getPredicates() {
		return predicates;
	}

	public PredPrecision join(final PredPrecision that) {
		final Collection<Expr<BoolType>> predicates = new HashSet<>();
		predicates.addAll(this.predicates);
		predicates.addAll(that.predicates);
		return PredPrecision.of(predicates);
	}

	////

	@Override
	public String toString() {
		return Utils.lispStringBuilder(getClass().getSimpleName()).aligned().addAll(predicates).toString();
	}

}
