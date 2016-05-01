package ch.epfl.xblast.testPerso.etape8;


import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import ch.epfl.xblast.server.GameStateSerializer;
import ch.epfl.xblast.server.Level;

public class GameStateSerializerTests {

	private List<Integer> expected = Arrays.asList(
			//Block
			121, -50, 2, 1, -2, 0, 3, 1, 3, 1, -2, 0, 1, 1, 3, 1, 3,
			 1, 3, 1, 1, -2, 0, 1, 3, 1, 3, -2, 0, -1, 1, 3, 1, 3, 1,
			 3, 1, 1, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3,
			 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2,
			 3, 1, 0, 0, 3, 1, 3, 1, 0, 0, 1, 1, 3, 1, 1, 0, 0, 1, 3,
			 1, 3, 0, 0, -1, 1, 3, 1, 1, -5, 2, 3, 2, 3, -5, 2, 3, 2,
			 3, 1, -2, 0, 3, -2, 0, 1, 3, 2, 1, 2,
			 //Explosions and Bombs
			 4, -128, 16, -63, 16,
			 //Players
			 3, 24, 24, 6,
			 3, -40, 24, 26,
			 3, -40, -72, 46,
			 3, 24, -72, 66,
			 //time
			 60
			 );
	@Test
	public void testOnDefaultInitialGameState(){
		Level lev = Level.DEFAULT_LEVEL; 
		List<Byte> actual = GameStateSerializer.serialize(lev.boardPainter(), lev.initialState);
		assertEquals(expected.size(), actual.size());
		for (int i = 0; i < expected.size(); i++) {
			assertEquals(expected.get(i).intValue(), actual.get(i).intValue());
		}
	}

}
