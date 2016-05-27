/**
 * @author Ricardo Ferreira Ribeiro (250798)
 * @author Clarise Estelle Fleurimont (246866)
 * @date 29 févr. 2016
 */
package ch.epfl.xblast.server;

import java.util.NoSuchElementException;

/**
 * Define the different block's type
 */
public enum Block {

	FREE, INDESTRUCTIBLE_WALL, DESTRUCTIBLE_WALL, CRUMBLING_WALL, BONUS_BOMB(Bonus.INC_BOMB), BONUS_RANGE(
			Bonus.INC_RANGE);

	private Bonus maybeAssociatedBonus;

	private Block(Bonus maybeAssociatedBonus) {
		this.maybeAssociatedBonus = maybeAssociatedBonus;
	}

	private Block() {
		this.maybeAssociatedBonus = null;
	}

	/**
	 * Check if the block is free or not
	 * 
	 * @return true iff the block is free, otherwise false
	 */
	public boolean isFree() {
		return (this.equals(FREE));
	}

	/**
	 * Check if the block is free or has a bonus (= can host a player)
	 * 
	 * @return true iff the player can go on the block, otherwise false
	 */
	public boolean canHostPlayer() {
		return (this.isFree() || this.isBonus());
	}

	/**
	 * Check if the block can cast a shadow (if the block is a wall,
	 * destructible, indestructible, crumbling)
	 * 
	 * @return true if the block can cast a shadow, otherwise false
	 */
	public boolean castsShadow() {
		return (this == INDESTRUCTIBLE_WALL) || (this == DESTRUCTIBLE_WALL) || (this == CRUMBLING_WALL);

	}

	/**
	 * Check if there is a bonus on the cell or not
	 * 
	 * @return true if the block is a bonus, otherwise false
	 */
	public boolean isBonus() {
		return (maybeAssociatedBonus != null);
	}

	/**
	 * @return the bonus corresponding to the block
	 */
	public Bonus associatedBonus() {
		if (isBonus()) {
			return maybeAssociatedBonus;
		} else {
			throw new NoSuchElementException("The block doesn't contain a bonus.");
		}
	}
}
