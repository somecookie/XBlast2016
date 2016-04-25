/**
 * @author Ricardo Ferreira Ribeiro (250798)
 * @author Eleonore Pochon (262959)
 */
package ch.epfl.xblast.server;

import java.util.Set;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;

public final class ExplosionPainter {

	public final static byte BYTE_FOR_EMPTY = 16;

	private ExplosionPainter() {

	}

	public static byte byteForBomb(Bomb bomb) {
		if (Integer.bitCount(bomb.fuseLength()) == 1) {
			return (byte) 20;
		} else {
			return (byte) 21;
		}
	}

	public static byte byteForBlast(boolean northCellIsBlasted, boolean eastCellIsBlasted, boolean southCellIsBlasted,
			boolean westCellIsBlasted) {
		byte byteForBlast = 0;
        boolean[] neighboorBlast = { northCellIsBlasted, eastCellIsBlasted, southCellIsBlasted, westCellIsBlasted };

        for (int i = 0; i < neighboorBlast.length;) {
            byteForBlast = (byte) (byteForBlast << 1);
            if (byteForBlast == (byte)byteForBlast | (neighboorBlast[i] )){
            	return 1;
            }else{
            	return 0;
            }
        }

        return byteForBlast;

	}
	
	 public static byte byteForBlast(Cell cell, GameState gameState) {
	        Set<Cell> blastedCells = gameState.blastedCells();
	        Board board = gameState.board();
	        Block currentBlock = board.blockAt(cell);
	        
	        if (!currentBlock.isFree())
	            return BYTE_FOR_EMPTY;

	        boolean northCellIsBlasted = blastedCells.contains(board.blockAt(cell.neighbor(Direction.N)));
	        boolean eastCellIsBlasted = blastedCells.contains(board.blockAt(cell.neighbor(Direction.E)));
	        boolean southCellIsBlasted = blastedCells.contains(board.blockAt(cell.neighbor(Direction.S)));
	        boolean westCellIsBlasted = blastedCells.contains(board.blockAt(cell.neighbor(Direction.W)));

	        return byteForBlast(northCellIsBlasted, eastCellIsBlasted, southCellIsBlasted, westCellIsBlasted);
	    }



}