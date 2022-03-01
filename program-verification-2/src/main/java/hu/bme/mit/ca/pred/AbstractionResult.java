package hu.bme.mit.ca.pred;

import static com.google.common.base.Preconditions.checkNotNull;

import hu.bme.mit.ca.pred.arg.ArgNode;

abstract class AbstractionResult {

	private AbstractionResult() {
	}

	public static Safe safe(final ArgNode rootNode) {
		return new Safe(rootNode);
	}

	public static Unsafe unsafe(final ArgNode errorNode) {
		return new Unsafe(errorNode);
	}

	public boolean isSafe() {
		return false;
	}

	public boolean isUnsafe() {
		return false;
	}

	public Safe asSafe() {
		throw new ClassCastException();
	}

	public Unsafe asUnsafe() {
		throw new ClassCastException();
	}

	////

	public static final class Safe extends AbstractionResult {
		private final ArgNode rootNode;

		private Safe(final ArgNode rootNode) {
			this.rootNode = checkNotNull(rootNode);
		}

		public ArgNode getRootNode() {
			return rootNode;
		}

		@Override
		public boolean isSafe() {
			return true;
		}

		@Override
		public Safe asSafe() {
			return this;
		}

		@Override
		public String toString() {
			return "SUCCESS";
		}
	}

	public static final class Unsafe extends AbstractionResult {
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
