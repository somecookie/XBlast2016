package ch.epfl.xblast.server.debug;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;
import ch.epfl.xblast.server.GameState;
import ch.epfl.xblast.server.Player;

public class randomGeneration {

    public static void main(String[] args) throws InterruptedException {
     
        Board board = board();
        Player p = new Player(PlayerID.PLAYER_1, 3, new Cell(1,1), 2, 3);
        Player p2 = new Player(PlayerID.PLAYER_2, 3, new Cell(13,1), 2, 3);
        Player p3 = new Player(PlayerID.PLAYER_3, 3, new Cell(13,11), 2, 3);
        Player p4 = new Player(PlayerID.PLAYER_4, 3, new Cell(1,11), 2, 3);
        List<Player> players = new ArrayList<>(Arrays.asList(p,p2,p3,p4));
        GameState g = new GameState(board, players);
        RandomEventGenerator events = new RandomEventGenerator(2016, 30, 100);
        
        while(!g.isGameOver()){
            g = g.next(events.randomSpeedChangeEvents(), events.randomBombDropEvents());
            GameStatePrinter.printGameState(g);
            System.out.println();
            Thread.sleep(50);
        }
        

    }
    
    private static Board board(){
        Block __ = Block.FREE;
        Block XX = Block.INDESTRUCTIBLE_WALL;
        Block xx = Block.DESTRUCTIBLE_WALL;
        Board board = Board.ofQuadrantNWBlocksWalled(
          Arrays.asList(
            Arrays.asList(__, __, __, __, __, xx, __),
            Arrays.asList(__, XX, xx, XX, xx, XX, xx),
            Arrays.asList(__, xx, __, __, __, xx, __),
            Arrays.asList(xx, XX, __, XX, XX, XX, XX),
            Arrays.asList(__, xx, __, xx, __, __, __),
            Arrays.asList(xx, XX, xx, XX, xx, XX, __)));
        
        return board;
    }

}
