package ch.epfl.xblast;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;
import ch.epfl.xblast.server.GameState;
import ch.epfl.xblast.server.Player;

public class GameStateTest {

    @Test
    public void testOnalivePlayers() {
        List<List<Block>> bl = new ArrayList<>();
        List<Block> bl1 = new ArrayList<>();
        
        for (int i = 0; i < 7; i++) {
            bl1.add(Block.FREE);
        }
        for (int i = 0; i < 6; i++) {
            bl.add(bl1);
        }
        Board b = Board.ofQuadrantNWBlocksWalled(bl);
        Player p = new Player(PlayerID.PLAYER_1, 0, new Cell(0,0), 5, 5);
        Player p2 = new Player(PlayerID.PLAYER_2, 0, new Cell(3,0), 5, 5);
        Player p3 = new Player(PlayerID.PLAYER_3, 0, new Cell(7,5), 5, 5);
        Player p4 = new Player(PlayerID.PLAYER_4, 5, new Cell(9,0), 5, 5);
        List<Player> pl = new ArrayList<>(Arrays.asList(p,p2,p3,p4));
        GameState g = new GameState(b, pl);
        List<Player> alPl = g.alivePlayers();
        assertEquals(1,alPl.size());
    }
    

}
