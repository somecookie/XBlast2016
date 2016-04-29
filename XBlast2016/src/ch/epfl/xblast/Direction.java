package ch.epfl.xblast;

public enum Direction {
	N, E, S, W;

	/**
	 * Return the opposite direction of a given direction
	 * 
	 * @return the opposite (Direction) the opposite direction of a given
	 *         direction
	 */
	public Direction opposite() {
		switch (this) {
		case N:
			return S;
		case E:
			return W;
		case S:
			return N;
		case W:
			return E;
		default:
			throw new Error();
		}
	}

	/**
	 * Check if the direction is horizontal to the screen
	 * 
	 * @return true if direction is E or W, false otherwise
	 */
	public boolean isHorizontal() {
		return (this.equals(E) || this.equals(W));
	}

	/**
	 * Check if a direction is parallel to that
	 * 
	 * @param that
	 * @return true if it is parallel, false otherwise
	 */
	public boolean isParallelTo(Direction that) {
		return (that.equals(this) || that.equals(this.opposite()));
	}
}
