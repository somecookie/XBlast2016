package testPerso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;
import ch.epfl.xblast.server.Bomb;
import ch.epfl.xblast.server.GameState;
import ch.epfl.xblast.server.Player;
import ch.epfl.xblast.server.debug.GameStatePrinter;

public class Test {
    private static Board oneElementBoard(Block b){
        //Cell (7,6)
        List<Block> free = Collections.nCopies(13, Block.FREE);
        List<Block> a = Collections.nCopies(6, Block.FREE);
        List<Block> notFree = new ArrayList<>(a);
        notFree.add(b);
        notFree.addAll(a);
        List<List<Block>> board = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            if(i==5){
                board.add(new ArrayList<>(notFree));
            }
            else{
                board.add(new ArrayList<>(free));
            }
        }
        return Board.ofInnerBlocksWalled(board);
    }
    
    private static List<Player> players(){
        Player p = new Player(PlayerID.PLAYER_1, 3, new Cell(5,4), 1, 5);
        Player p2 = new Player(PlayerID.PLAYER_2, 5, new Cell(9,6), 5, 5);
        Player p3 = new Player(PlayerID.PLAYER_3, 3, new Cell(7,5), 5, 5);
        Player p4 = new Player(PlayerID.PLAYER_4, 5, new Cell(9,7), 5, 5);
        List<Player> pl = new ArrayList<>(Arrays.asList(p,p2,p3,p4));
        
        return pl;
    }
    
    public static void main(String[] args) {
        Board b = oneElementBoard(Block.FREE);
        
        List<Bomb> bombs = new ArrayList<>(Arrays.asList(new Bomb(PlayerID.PLAYER_1, new Cell(6,6), 3, 5)));
        Bomb b2 = new Bomb(PlayerID.PLAYER_2, new Cell(3, 6), 1, 5);
        
        List<Sq<Sq<Cell>>> explosions = new ArrayList<>();
        explosions.addAll(b2.explosion());
        
        List<Sq<Cell>> blasts = new ArrayList<>();
        GameState g = new GameState(0, b, players(), bombs , explosions , blasts);
        Map<PlayerID, Optional<Direction>> speedChangeEvents = new HashMap<>();
        Set<PlayerID> bombDropEvents = new HashSet<>();
        
        for (int i = 0; i <100; i++) {
            GameStatePrinter.printGameStateWithoutPlayers(g);

            g = g.next(speedChangeEvents, bombDropEvents);
        }
    }

}
