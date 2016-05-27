/**
 * @author Ricardo Ferreira Ribeiro (250798)
 * @author Clarisse Estelle Fleurimont (246866)
 * @date 29 févr. 2016
 */

package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Lists;

public final class Board {
	public static final int COLUMNS = 15;
	public static final int ROWS = 13;
	public static final int COUNT = COLUMNS * ROWS;
	private final List<Sq<Block>> board;

	public Board(List<Sq<Block>> blocks) {
		if (blocks.size() != COUNT) {
			throw new IllegalArgumentException("Illegal Argument: bloc's number different of 195 :" + blocks.size());
		} else {
			board = Collections.unmodifiableList(new ArrayList<>(blocks));
		}
	}

	/**
	 * Construct the constant board with the given matrix block
	 * 
	 * @param rows
	 *            block's list used to construct the board
	 * @return the board constructed with the given block's list
	 * @throws IllegalArgumentException
	 *             if the given list is not constituted of 13 rows of 15
	 *             elements each
	 */
	public static Board ofRows(List<List<Block>> rows) {
		checkBlockMatrix(rows, ROWS, COLUMNS);

		List<Sq<Block>> buildingBoard = new ArrayList<>();
		for (int i = 0; i < rows.size(); i++) {
			for (int j = 0; j < rows.get(i).size(); j++) {
				buildingBoard.add(Sq.constant(rows.get(i).get(j)));
			}
		}

		return new Board(buildingBoard);
	}

	/**
	 * Constructs a walled board with the given inner blocks, throw the
	 * IllegalArgumentException if the list is not constitute of 11 rows of 13
	 * elements each
	 * 
	 * @param innerBlocks
	 *            inner block's list
	 * @return board construct from the given block's list
	 * @throws IllegalArgumentException
	 *             if the list is not constitute of 11 rows of 13 elements each
	 */
	public static Board ofInnerBlocksWalled(List<List<Block>> innerBlocks) {
		checkBlockMatrix(innerBlocks, ROWS - 2, COLUMNS - 2);

		List<List<Block>> buildingBoard = new ArrayList<>();

		for (int i = 0; i < innerBlocks.size(); i++) {
			buildingBoard.add(new ArrayList<>());
			buildingBoard.get(i).add(Block.INDESTRUCTIBLE_WALL);
			for (int j = 0; j < innerBlocks.get(i).size(); j++) {
				buildingBoard.get(i).add(innerBlocks.get(i).get(j));
			}
			buildingBoard.get(i).add(Block.INDESTRUCTIBLE_WALL);
		}

		List<Block> walls = Collections.nCopies(buildingBoard.get(0).size(), Block.INDESTRUCTIBLE_WALL);
		buildingBoard.add(0, new ArrayList<>(walls));
		buildingBoard.add(new ArrayList<>(walls));

		return ofRows(buildingBoard);
	}

	/**
	 * Construct a walled symmetric board with the north-west given blocks, the
	 * board is separated in four symmetric parts
	 * 
	 * @param quadrantNWBlocks
	 *            Block's list in the north-west's quadrant of the board, used
	 *            to construct the board
	 * @return the board constructed from the given list
	 * @throws IllegalArgumentException
	 *             if the list isn't constitute of 6 lists of 7 elements each
	 */
	public static Board ofQuadrantNWBlocksWalled(List<List<Block>> quadrantNWBlocks) {
		checkBlockMatrix(quadrantNWBlocks, (ROWS - 1) / 2, (COLUMNS - 1) / 2);
		List<List<Block>> buildingBoard = new ArrayList<>();
		for (int i = 0; i < quadrantNWBlocks.size(); i++) {
			List<Block> tmp = Lists.mirrored(quadrantNWBlocks.get(i));
			buildingBoard.add(new ArrayList<>(tmp));
		}

		for (int i = buildingBoard.size() - 2; i >= 0; i--) {
			buildingBoard.add(new ArrayList<>(buildingBoard.get(i)));
		}

		return ofInnerBlocksWalled(buildingBoard);

	}

	/**
	 * Check if the given matrix has the right size (numbers of rows and
	 * columns)
	 * 
	 * @param matrix
	 *            given matrix (must be checked)
	 * @param rows
	 *            number of expected rows
	 * @param columns
	 *            number of expected columns
	 * @throws IllegalArgumentException
	 *             if the matrix doesn't contain the correct expected rows or
	 *             columns
	 */
	public static void checkBlockMatrix(List<List<Block>> matrix, int rows, int columns) {
		if (matrix == null || matrix.isEmpty()) {
			throw new IllegalArgumentException("The list is empty.");
		}

		int rowSize = matrix.size();
		if (rowSize != rows) {
			throw new IllegalArgumentException("Illegal Argument: must have " + rows + "rows and not " + rowSize);
		}
		for (int i = 0; i < rowSize; i++) {
			if (matrix.get(i).size() != columns) {
				throw new IllegalArgumentException("Illegal Argument: must have " + columns + " columns and not "
						+ matrix.get(i).size() + " at rows " + i);
			}
		}
	}

	/**
	 * Return the block's sequence for a given cell
	 * 
	 * @param c
	 *            the given cell containing the block's sequence
	 * @return (Sq<Block>) the block's sequence contains in the given cell
	 */
	public Sq<Block> blocksAt(Cell c) {
		int index = c.rowMajorIndex();
		return board.get(index);
	}

	/**
	 * Return the block for the given cell (the head of the sequence from the
	 * precede method)
	 * 
	 * @param c
	 *            the given cell
	 * @return Block
	 */
	public Block blockAt(Cell c) {
		int index = c.rowMajorIndex();
		return board.get(index).head();
	}
}
