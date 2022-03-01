package hu.bme.mit.ca.pred;

import static com.google.common.base.Preconditions.checkNotNull;

import hu.bme.mit.ca.pred.arg.ArgNode;
import hu.bme.mit.ca.pred.domain.PredPrecision;

abstract class RefinementResult {

	private RefinementResult() {
	}

	public static Spurious spurious(final PredPrecision precision) {
		return new Spurious(precision);
	}

	public static Unsafe unsafe(final ArgNode errorNode) {
		return new Unsafe(errorNode);
	}

	public boolean isSpurious() {
		return false;
	}

	public boolean isUnsafe() {
		return false;
	}

	public Spurious asSpurious() {
		throw new ClassCastException();
	}

	public Unsafe asUnsafe() {
		throw new ClassCastException();
	}

	////

	public static final class Spurious extends RefinementResult {
		private final PredPrecision precision;

		private Spurious(final PredPrecision precision) {
			this.precision = checkNotNull(precision);
		}

		public PredPrecision getPrecision() {
			return precision;
		}

		@Override
		public boolean isSpurious() {
			return true;
		}

		@Override
		public Spurious asSpurious() {
			return this;
		}

		@Override
		public String toString() {
			return "SUCCESS";
		}
	}

	public static final class Unsafe extends RefinementResult {
		private final ArgNode errorNode;

		private Unsafe(final ArgNode errorNode) {
			this.errorNode = checkNotNull(errorNode);
		}

		public ArgNode getErrorNode() {
			return errorNode;
		}

		@Override
		public boolean isUnsafe() {
			return true;
		}

		@Override
		public Unsafe asUnsafe() {
			return this;
		}

		@Override
		public String toString() {
			return "FAILURE";
		}
	}
}
