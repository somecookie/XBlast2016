package ch.epfl.xblast.testPerso.etape7;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.Test;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;
import ch.epfl.xblast.server.Bomb;
import ch.epfl.xblast.server.GameState;
import ch.epfl.xblast.server.Player;
import ch.epfl.xblast.server.image.ExplosionPainter;
import ch.epfl.xblast.server.debug.*;

public class ExplosionPainterTest {

	@Test
	public void explosionPainterByte() {
		assertEquals(16, ExplosionPainter.BYTE_FOR_EMPTY);
	}

	@Test
	public void byteForBombsTest() {
		List<Integer> powList = Arrays.asList(1, 2, 4, 8, 16);
		for (int i = 17; i > 0; --i) {
			Bomb bombTest = new Bomb(PlayerID.PLAYER_1, new Cell(0, 0), i, 3);
			if (powList.contains(i))
				assertEquals(21, ExplosionPainter.byteForBomb(bombTest));
			else
				assertEquals(20, ExplosionPainter.byteForBomb(bombTest));
		}

	}

	@Test
	public void byteForBlastTest() {
		// N,E,S,W
		assertEquals(0, ExplosionPainter.byteForBlast(false, false, false, false));
		assertEquals(5, ExplosionPainter.byteForBlast(false, true, false, true));
		assertEquals(9, ExplosionPainter.byteForBlast(true, false, false, true));
		assertEquals(13, ExplosionPainter.byteForBlast(true, true, false, true));
		assertEquals(15, ExplosionPainter.byteForBlast(true, true, true, true));
	}

	@Test
	public void test() {
		List<Player> players = new ArrayList<>(Arrays.asList(new Player(PlayerID.PLAYER_1, 3, new Cell(1, 1), 5, 5),
				new Player(PlayerID.PLAYER_2, 3, new Cell(2, 2), 5, 5),
				new Player(PlayerID.PLAYER_3, 3, new Cell(3, 3), 4, 5),
				new Player(PlayerID.PLAYER_4, 3, new Cell(4, 4), 5, 5)));
		List<Block> block = Collections.nCopies(7, Block.FREE);
		List<List<Block>> bl = Collections.nCopies(6, block);
		Board board = Board.ofQuadrantNWBlocksWalled(bl);

		List<Bomb> bombs = new ArrayList<>(Arrays.asList(new Bomb(PlayerID.PLAYER_1, new Cell(7,6), 3, 5)));
		List<Sq<Sq<Cell>>> explosions = new ArrayList<>();
		List<Sq<Cell>> blasts = new ArrayList<>();
		GameState g = new GameState(0, board, players, bombs, explosions, blasts);
		Map<PlayerID, Optional<Direction>> speedChangeEvents= new HashMap<>();
		Set<PlayerID> bombDropEvents = new HashSet<>();
		
		for (int i = 0; i < 5; i++) {
			System.out.println(g.ticks());
			g = g.next(speedChangeEvents, bombDropEvents);
		}
		
		assertEquals(15, ExplosionPainter.byteForBlast(new Cell(7,6), g));

	}

}
