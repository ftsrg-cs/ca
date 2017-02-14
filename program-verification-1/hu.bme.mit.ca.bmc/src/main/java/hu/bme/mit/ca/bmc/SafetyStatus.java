package hu.bme.mit.ca.bmc;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import hu.bme.mit.theta.common.ObjectUtils;

abstract class SafetyStatus {

	private SafetyStatus() {
	}

	public static Safe safe() {
		return Safe.INSTANCE;
	}

	public static Unsafe unsafe(final Path counterexample) {
		return new Unsafe(counterexample);
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

	public static final class Safe extends SafetyStatus {
		private static final Safe INSTANCE = new Safe();

		private Safe() {
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

	public static final class Unsafe extends SafetyStatus {
		private final Path counterexample;

		private Unsafe(final Path counterexample) {
			this.counterexample = checkNotNull(counterexample);
		}

		public Path getCounterexample() {
			return counterexample;
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
			return ObjectUtils.toStringBuilder("UNSAFE").add(counterexample).toString();
		}
	}

	public static final class Unknown extends SafetyStatus {
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

	public static final class Timeout extends SafetyStatus {
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
			return ObjectUtils.toStringBuilder("TIMEOUT").add(elapsed).toString();
		}
	}

}
