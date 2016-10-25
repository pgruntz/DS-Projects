package ch.ethz.inf.vs.a3.clock;

public interface Clock{

	/**
	 * Update the current clock with a new one, taking into
	 * account the values of the incoming clock.
	 *
	 * E.g. for vector clocks, c1 = [2 1 0], c2 = [1 2 0],
	 * the c1.update(c2) will lead to [2 2 0].
	 * @param other
	 */
	public void update(Clock other);

	/**
	 * Change the current clock with a new one, overwriting the
	 * old values.
	 * @param other
	 */
	public void setClock(Clock other);

	/**
	 * Tick a clock given the process id.
	 *
	 * For Lamport timestamps, since there is only one logical time,
	 * the method can be called with the "null" parameter. (e.g.
	 * clock.tick(null).
	 * @param pid
	 */
	public void tick(Integer pid);

	/**
	 * Check whether a clock has happened before another one.
	 *
	 * @param other
	 * @return True if a clock has happened before, false otherwise.
	 */
	public boolean happenedBefore(Clock other);

	/**
	 * toString
	 *
	 * @return String representation of the clock.
	 */
	public String toString();

	/**
	 * Set a clock given it's string representation.
	 *
	 * @param clock
	 */
	public void setClockFromString(String clock);
	
}
