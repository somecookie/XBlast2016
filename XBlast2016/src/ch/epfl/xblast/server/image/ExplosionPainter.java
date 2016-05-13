/**
 * @author Ricardo Ferreira Ribeiro (250798)
 * @author Eleonore Pochon (262959)
 */
package ch.epfl.xblast.server.image;

import java.util.Set;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;
import ch.epfl.xblast.server.Bomb;
import ch.epfl.xblast.server.GameState;

public final class ExplosionPainter {

	public final static byte BYTE_FOR_EMPTY = 16;

	/**
	 * Constructor of the player painter, it's empty because the class isn't
	 * instanciable
	 */
	private ExplosionPainter() {

	}

	/**
	 * Calculate the image corresponding to the bomb in function of her fuse
	 * length
	 * 
	 * @param bomb
	 *            the bomb on the actual game state
	 * @return the corresponding byte to the corresponding image of the bomb
	 */
	public static byte byteForBomb(Bomb bomb) {
		if (Integer.bitCount(bomb.fuseLength()) == 1) {
			return (byte) 21;
		} else {
			return (byte) 20;
		}
	}

	/**
	 * Calculate which image do we have to use for the blast in function of its
	 * neighbors
	 * 
	 * @param northCellIsBlasted
	 *            if there is a blast on the North's cell
	 * @param eastCellIsBlasted
	 *            if there is a blast on the East's cell
	 * @param southCellIsBlasted
	 *            if there is a blast on the Southe's cell
	 * @param westCellIsBlasted
	 *            if there is a blast on the West's cell
	 * @return the corresponding blast in function of the directions
	 */
	public static byte byteForBlast(boolean northCellIsBlasted, boolean eastCellIsBlasted, boolean southCellIsBlasted,
			boolean westCellIsBlasted) {
		byte byteForBlast = 0;
		if (northCellIsBlasted) {
			byteForBlast += 8;
		}
		if (eastCellIsBlasted) {
			byteForBlast += 4;
		}
		if (southCellIsBlasted) {
			byteForBlast += 2;
		}
		if (westCellIsBlasted) {
			byteForBlast += 1;
		}

		return byteForBlast;

	}

	/**
	 * Calculate which image do we have to use for the blast in function of its
	 * neighbors
	 * 
	 * @param cell
	 *            the cell's containing a blast
	 * @param gameState
	 *            the actual game state
	 * @return
	 */
	public static byte byteForBlast(Cell cell, GameState gameState) {
		Set<Cell> blastedCells = gameState.blastedCells();
		Board board = gameState.board();
		Block currentBlock = board.blockAt(cell);

		if (!currentBlock.isFree() || !blastedCells.contains(cell)) {
			return BYTE_FOR_EMPTY;
		}

		boolean northCellIsBlasted = blastedCells.contains(board.blockAt(cell.neighbor(Direction.N)));
		boolean eastCellIsBlasted = blastedCells.contains(board.blockAt(cell.neighbor(Direction.E)));
		boolean southCellIsBlasted = blastedCells.contains(board.blockAt(cell.neighbor(Direction.S)));
		boolean westCellIsBlasted = blastedCells.contains(board.blockAt(cell.neighbor(Direction.W)));

		return byteForBlast(northCellIsBlasted, eastCellIsBlasted, southCellIsBlasted, westCellIsBlasted);
	}

}