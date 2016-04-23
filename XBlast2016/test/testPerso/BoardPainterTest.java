package testPerso;

import static org.junit.Assert.assertEquals;

import java.security.KeyStore.Entry;
import java.util.Map;

import org.junit.Test;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.BlockImage;
import ch.epfl.xblast.server.Board;
import ch.epfl.xblast.server.BoardPainter;

public class BoardPainterTest {

	@Test
	public void testOnByteForCell(){
		Board b = Util.board();
		Cell c1 = new Cell(2, 1); //Free
		Cell c2 = new Cell(1, 1); //shaded Free
		Cell c3 = new Cell(0,0); // indestructible wall
		Cell c4 = new Cell(3,2); // destructible wall
		Cell c5 = new Cell(7,6); // crumbling, bonuses
		Map<Block, BlockImage> pallet = Util.pallet();
		
		BoardPainter bp = new BoardPainter(pallet, BlockImage.IRON_FLOOR_S);

		assertEquals((byte)000, bp.byteForCell(b, c1));
		assertEquals((byte)001, bp.byteForCell(b, c2));
		assertEquals((byte)002, bp.byteForCell(b, c3));
		assertEquals((byte)003, bp.byteForCell(b, c4));
		
		b = Util.oneElementBoard(Block.CRUMBLING_WALL);
		assertEquals((byte)004, bp.byteForCell(b, c5));
		
		b = Util.oneElementBoard(Block.BONUS_BOMB);
		assertEquals((byte)005, bp.byteForCell(b, c5));
		
		b = Util.oneElementBoard(Block.BONUS_RANGE);
		assertEquals((byte)006, bp.byteForCell(b, c5));
		
	}

}
