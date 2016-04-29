package ch.epfl.xblast.testPerso.etape1a6;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;
import ch.epfl.xblast.server.Level;
import ch.epfl.xblast.server.Player;

public class LevelTest {

    private static Block __ = Block.FREE;
    private static Block XX = Block.INDESTRUCTIBLE_WALL;
    private static Block xx = Block.DESTRUCTIBLE_WALL;
    private static Board initialBoard = Board.ofQuadrantNWBlocksWalled(
            Arrays.asList(Arrays.asList(__, __, __, __, __, xx, __),
                    Arrays.asList(__, XX, xx, XX, xx, XX, xx),
                    Arrays.asList(__, xx, __, __, __, xx, __),
                    Arrays.asList(xx, XX, __, XX, XX, XX, XX),
                    Arrays.asList(__, xx, __, xx, __, __, __),
                    Arrays.asList(xx, XX, xx, XX, xx, XX, __)));

    @Test
    public void defaultBoardCorrect() {
        for (int y = 0; y < Cell.ROWS; ++y) {
            for (int x = 0; x < Cell.COLUMNS; ++x) {
                assertEquals(initialBoard.blockAt(new Cell(x, y)),
                        Level.DEFAULT_LEVEL.initialState().board()
                                .blockAt(new Cell(x, y)));
            }
        }
    }


    @Test
    public void defaultLevelCorrectBlockImages() {


        assertEquals(1, Level.DEFAULT_LEVEL.boardPainter()
                .byteForCell(initialBoard, new Cell(1, 1)));
        assertEquals(2, Level.DEFAULT_LEVEL.boardPainter()
                .byteForCell(initialBoard, new Cell(0, 0)));
        assertEquals(0, Level.DEFAULT_LEVEL.boardPainter()
                .byteForCell(initialBoard, new Cell(2, 1)));
        assertEquals(3, Level.DEFAULT_LEVEL.boardPainter()
                .byteForCell(initialBoard, new Cell(3, 2)));
    }

    @Test
    public void defaultPlayersCorrect() {

        assertEquals(PlayerID.PLAYER_1,
                Level.DEFAULT_LEVEL.initialState().players().get(0).id());
        assertEquals(new Cell(1, 1), Level.DEFAULT_LEVEL.initialState()
                .players().get(0).position().containingCell());

        assertEquals(PlayerID.PLAYER_2,
                Level.DEFAULT_LEVEL.initialState().players().get(1).id());
        assertEquals(new Cell(13, 1), Level.DEFAULT_LEVEL.initialState()
                .players().get(1).position().containingCell());

        assertEquals(PlayerID.PLAYER_3,
                Level.DEFAULT_LEVEL.initialState().players().get(2).id());
        assertEquals(new Cell(13, 11), Level.DEFAULT_LEVEL.initialState()
                .players().get(2).position().containingCell());

        assertEquals(PlayerID.PLAYER_4,
                Level.DEFAULT_LEVEL.initialState().players().get(3).id());
        assertEquals(new Cell(1, 11), Level.DEFAULT_LEVEL.initialState()
                .players().get(3).position().containingCell());

    }

}
