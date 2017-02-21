package hu.bme.mit.ca.pred;

import static com.google.common.base.Preconditions.checkNotNull;

import hu.bme.mit.ca.pred.arg.ArgNode;

abstract class AbstractionResult {

	private AbstractionResult() {
	}

	public static Success success(final ArgNode rootNode) {
		return new Success(rootNode);
	}

	public static Failure failure(final ArgNode errorNode) {
		return new Failure(errorNode);
	}

	public boolean isSuccess() {
		return false;
	}

	public boolean isFailure() {
		return false;
	}

	public Success asSuccess() {
		throw new ClassCastException();
	}

	public Failure asFailure() {
		throw new ClassCastException();
	}

	////

	public static final class Success extends AbstractionResult {
		private final ArgNode rootNode;

		private Success(final ArgNode rootNode) {
			this.rootNode = checkNotNull(rootNode);
		}

		public ArgNode getRootNode() {
			return rootNode;
		}

		@Override
		public boolean isSuccess() {
			return true;
		}

		@Override
		public Success asSuccess() {
			return this;
		}

		@Override
		public String toString() {
			return "SUCCESS";
		}
	}

	public static final class Failure extends AbstractionResult {
		private final ArgNode errorNode;

		private Failure(final ArgNode errorNode) {
			this.errorNode = checkNotNull(errorNode);
		}

		public ArgNode getErrorNode() {
			return errorNode;
		}

		@Override
		public boolean isFailure() {
			return true;
		}

		@Override
		public Failure asFailure() {
			return this;
		}

		@Override
		public String toString() {
			return "FAILURE";
		}
	}

}
