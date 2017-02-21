package hu.bme.mit.ca.pred;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import hu.bme.mit.ca.pred.arg.ArgNode;

public abstract class SafetyResult {

	private SafetyResult() {
	}

	public static Safe safe(final ArgNode rootNode) {
		return new Safe(rootNode);
	}

	public static Unsafe unsafe(final ArgNode errorNode) {
		return new Unsafe(errorNode);
	}

	public static Unknown unknown() {
		return Unknown.INSTANCE;
	}

	public static Timeout timeout(final long elapsed) {
		return new Timeout(elapsed);
	}

	////

	public boolean isSafe() {
		return false;
	}

	public boolean isUnsafe() {
		return false;
	}

	public boolean isUnknown() {
		return false;
	}

	public boolean isTimeout() {
		return false;
	}

	////

	public Safe asSafe() {
		throw new ClassCastException();
	}

	public Unsafe asUnsafe() {
		throw new ClassCastException();
	}

	public Unknown asUnknow() {
		throw new ClassCastException();
	}

	public Timeout asTimeout() {
		throw new ClassCastException();
	}

	////

	public static final class Safe extends SafetyResult {
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
			return "SAFE";
		}
	}

	////

	public static final class Unsafe extends SafetyResult {
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
			return "UNSAFE";
		}
	}

	public static final class Unknown extends SafetyResult {
		private static final Unknown INSTANCE = new Unknown();

		private Unknown() {
		}

		@Override
		public boolean isUnknown() {
			return true;
		}

		@Override
		public Unknown asUnknow() {
			return this;
		}

		@Override
		public String toString() {
			return "UNKNOWN";
		}
	}

	public static final class Timeout extends SafetyResult {
		private final long elapsed;

		private Timeout(final long elapsed) {
			checkArgument(elapsed >= 0);
			this.elapsed = elapsed;
		}

		public long getElapsed() {
			return elapsed;
		}

		@Override
		public boolean isTimeout() {
			return true;
		}

		@Override
		public Timeout asTimeout() {
			return this;
		}

		@Override
		public String toString() {
			return "TIMEOUT";
		}
	}

}
