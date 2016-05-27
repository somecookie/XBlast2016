/**
 * @author Ricardo Ferreira Ribeiro (250798)
 * @author Eleonore Pochon (262959)
 */

package ch.epfl.xblast.server.image;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;

public final class BoardPainter {

	private final Map<Block, BlockImage> pallet;
	private final BlockImage shadedFree;

	/**
	 * Construct the board painter
	 * 
	 * @param p,
	 *            the pallet, which is represented by a map, matching each block
	 *            with its BlockImage.
	 * @param sFree,
	 *            the BlockImage representing the shaded Free blocks.
	 */
	public BoardPainter(Map<Block, BlockImage> p, BlockImage sFree) {
		pallet = Collections.unmodifiableMap(new HashMap<>(p));
		shadedFree = sFree;
	}

	/**
	 * Given a board and a cell, return the octet identifying the image of the
	 * block for the cell c
	 * 
	 * @param board
	 * @param c,
	 *            the position of the block (from the board) we want to
	 *            calculate the image byte code.
	 * @return byte, the byte code of the block
	 */
	public byte byteForCell(Board board, Cell c) {
		Block b = board.blockAt(c);
		if (b.isFree() && board.blockAt(c.neighbor(Direction.W)).castsShadow()) {
			return (byte) shadedFree.ordinal();
		} else {
			return (byte) pallet.get(b).ordinal();
		}
	}

}
