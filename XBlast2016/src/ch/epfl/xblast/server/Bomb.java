/**
 * @author Ricardo Ferreira Ribeiro (250798)
 * @author Clarise Estelle Fleurimont (246866)
 * @date 7 mars 2016
 */
package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.ArgumentChecker;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;

public final class Bomb {
	private final PlayerID ownerID;
	private final Cell position;
	private final Sq<Integer> fuseLengths;
	private final int range;

	/**
	 * Construct a bomb with her owner, her position, her fuse length and her
	 * range
	 * 
	 * @param ownerId
	 * @param position
	 * @param fuseLengths
	 * @param range
	 * @throws NullPointerException
	 *             if one of the three arguments is null (ownerId, position,
	 *             range)
	 * @throws IllegalArgumentException
	 *             if the fuse length is empty
	 */
	public Bomb(PlayerID ownerId, Cell position, Sq<Integer> fuseLengths, int range) {
		if (fuseLengths.isEmpty()) {
			throw new IllegalArgumentException();
		}
		this.ownerID = Objects.requireNonNull(ownerId);
		;
		this.fuseLengths = Objects.requireNonNull(fuseLengths);
		this.position = Objects.requireNonNull(position);
		this.range = ArgumentChecker.requireNonNegative(range);
	}

	/**
	 * Construct a bomb with her owner, her position, her fuse length and her
	 * range, but the difference with that constructor is that the fuse length
	 * is an integer and not a sequence, the constructor as the same exceptions
	 * as the first constructor
	 * 
	 * @param ownerId
	 * @param position
	 * @param fuseLength
	 * @param range
	 */
	public Bomb(PlayerID ownerId, Cell position, int fuseLength, int range) {
		this(ownerId, position, Objects.requireNonNull(Sq.iterate(fuseLength, u -> u - 1).limit(fuseLength)), range);
	}

	public PlayerID ownerId() {
		return ownerID;
	}

	public Cell position() {
		return position;
	}

	public Sq<Integer> fuseLengths() {
		return fuseLengths;
	}

	public int fuseLength() {
		return fuseLengths().head();
	}

	public int range() {
		return range;
	}

	/**
	 * Return the explosions corresponding to the bomb as a table of 4 elements,
	 * which one represents an arm of the explosion, the time of the explosions
	 * defines with Ticks.EXPLOSION_TICKS
	 * 
	 * @return the explosion corresponding to the bomb
	 */
	public List<Sq<Sq<Cell>>> explosion() {
		List<Sq<Sq<Cell>>> explosion = new ArrayList<>();

		explosion.add(explosionArmTowards(Direction.N));
		explosion.add(explosionArmTowards(Direction.E));
		explosion.add(explosionArmTowards(Direction.S));
		explosion.add(explosionArmTowards(Direction.W));

		return explosion;
	}

	/**
	 * Calculate one arm of the explosion, this function does not depend on
	 * directions
	 * 
	 * @param dir
	 * 		  the given direction
	 * @return the arm of the explosion in function of the direction
	 */
	private Sq<Sq<Cell>> explosionArmTowards(Direction dir) {
		Sq<Cell> pos = Sq.iterate(position, c -> c.neighbor(dir)).limit(range);
		Sq<Sq<Cell>> arm = Sq.repeat(Ticks.EXPLOSION_TICKS, pos);
		return arm;

	}
}
