package testPerso;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
import ch.epfl.xblast.server.Ticks;
import ch.epfl.xblast.server.debug.GameStatePrinter;

public class GameStateTest {
    

    private static Board board(){
        List<List<Block>> board = new ArrayList<>();
        List<Block> l1 = new ArrayList<>(Collections.nCopies(7, Block.FREE));
        List<Block> l2 = new ArrayList<>(Arrays.asList(Block.FREE, Block.DESTRUCTIBLE_WALL, Block.DESTRUCTIBLE_WALL, Block.DESTRUCTIBLE_WALL,
                Block.BONUS_BOMB,Block.DESTRUCTIBLE_WALL, Block.DESTRUCTIBLE_WALL));
        List<Block> l3 = new ArrayList<>(Collections.nCopies(7, Block.FREE));
        List<Block> l4 = new ArrayList<>(Arrays.asList(Block.FREE, Block.FREE, Block.DESTRUCTIBLE_WALL,Block.FREE, Block.FREE,Block.FREE, Block.FREE));
        List<Block> l5 = new ArrayList<>(Arrays.asList(Block.FREE, Block.BONUS_RANGE, Block.DESTRUCTIBLE_WALL,Block.FREE, Block.FREE,Block.FREE, Block.FREE));
        List<Block> l6 = new ArrayList<>(Arrays.asList(Block.FREE, Block.FREE, Block.DESTRUCTIBLE_WALL,Block.FREE, Block.FREE,Block.FREE, Block.FREE));
        board.add(l1);  board.add(l2);  board.add(l3);  board.add(l4);  board.add(l5);  board.add(l6);
        return Board.ofQuadrantNWBlocksWalled(board);
    }
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
    
    private static List<Bomb> bombs(){
        Bomb b1 = new Bomb(PlayerID.PLAYER_1, new Cell(8,9), 5, 5);
        Bomb b2 = new Bomb(PlayerID.PLAYER_2, new Cell(6,4),5,5);
        Bomb b3 = new Bomb(PlayerID.PLAYER_3, new Cell(7,8), 5, 5);
        Bomb b4 = new Bomb(PlayerID.PLAYER_4, new Cell(12,11),5,5);
        return new ArrayList<>(Arrays.asList(b1,b2,b3,b4));
    }
    
    private static List<Sq<Sq<Cell>>> explosions(){
        List<Sq<Sq<Cell>>> exploded = new ArrayList<>();
        Bomb b1 = new Bomb(PlayerID.PLAYER_1, new Cell(5,9), 5, 5);
        Bomb b2 = new Bomb(PlayerID.PLAYER_2, new Cell(12,4),5,5);
        Bomb b3 = new Bomb(PlayerID.PLAYER_3, new Cell(2,8), 5, 5);
        exploded.addAll(b1.explosion());
        exploded.addAll(b2.explosion());
        exploded.addAll(b3.explosion());
        return exploded;
        
    }
    

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
        Player p = new Player(PlayerID.PLAYER_1, 0, new Cell(4,8), 5, 5);
        Player p2 = new Player(PlayerID.PLAYER_2, 0, new Cell(3,4), 5, 5);
        Player p3 = new Player(PlayerID.PLAYER_3, 0, new Cell(7,5), 5, 5);
        Player p4 = new Player(PlayerID.PLAYER_4, 5, new Cell(9,3), 5, 5);
        List<Player> pl = new ArrayList<>(Arrays.asList(p,p2,p3,p4));
        GameState g = new GameState(b, pl);
        List<Player> alPl = g.alivePlayers();
        assertEquals(1,alPl.size());
    }
    @Test
    public void maxBombs(){
        Board board = board();
        List<Player> players = players();
        List<Bomb> bombs = new ArrayList<Bomb>(Arrays.asList(new Bomb(PlayerID.PLAYER_1, new Cell(9,6), 5, 5)));
        List<Sq<Sq<Cell>>> explosions = new ArrayList<>();
        List<Sq<Cell>> blasts = new ArrayList<>();
        GameState g = new GameState(0, board, players, bombs, explosions, blasts);  
        
        Map<PlayerID, Optional<Direction>> speedChangeEvents = new HashMap<>();
        Set<PlayerID> bombDropEvents = new HashSet<>();
        bombDropEvents.add(PlayerID.PLAYER_1);
        g = g.next(speedChangeEvents , bombDropEvents );
        assertEquals(1, g.bombedCells().size());
    }
    
    @Test
    public void testOnTakenCell(){
        Board board = board();
        List<Player> players = players();
        List<Bomb> bombs = new ArrayList<Bomb>(Arrays.asList(new Bomb(PlayerID.PLAYER_1, new Cell(9,6), 5, 5)));
        List<Sq<Sq<Cell>>> explosions = new ArrayList<>();
        List<Sq<Cell>> blasts = new ArrayList<>();
        
        GameState g = new GameState(0, board, players, bombs, explosions, blasts);
        Map<PlayerID, Optional<Direction>> speedChangeEvents = new HashMap<>();
        Set<PlayerID> bombDropEvents = new HashSet<>();
        bombDropEvents.add(PlayerID.PLAYER_2);
        g = g.next(speedChangeEvents , bombDropEvents );
        Collection<Bomb> b = g.bombedCells().values();
        assertEquals(1, g.bombedCells().size());
        assertEquals(PlayerID.PLAYER_1, b.iterator().next().ownerId());
        
    }
    
    @Test
    public void testOn2PlayersDeposing(){
        Board board = oneElementBoard(Block.FREE);
        List<Player> players = new ArrayList<>(Arrays.asList(new Player(PlayerID.PLAYER_1, 5, new Cell(5, 5), 5, 5),
                new Player(PlayerID.PLAYER_2, 5, new Cell(5, 5), 5, 5),
                new Player(PlayerID.PLAYER_3, 5, new Cell(5, 5), 5, 5),
                new Player(PlayerID.PLAYER_4, 5, new Cell(5, 5), 5, 5)));
        

        Set<PlayerID> bombDropEvents = new HashSet<>();
        bombDropEvents.add(PlayerID.PLAYER_1); bombDropEvents.add(PlayerID.PLAYER_2);
        
        Set<PlayerID> bombDropEvents1 = new HashSet<>(bombDropEvents);
        
        Map<PlayerID, Optional<Direction>> speedChangeEvents = new HashMap<>();
        
        List<Bomb> bombs = new ArrayList<>();
        List<Sq<Sq<Cell>>> explosions = new ArrayList<>();
        List<Sq<Cell>> blasts = new ArrayList<>();
        
        GameState g = new GameState(0, board, players, bombs, explosions, blasts);
        g = g.next(speedChangeEvents, new HashSet<>(bombDropEvents));
        Collection<Bomb> b = g.bombedCells().values();
        assertEquals(PlayerID.PLAYER_1, b.iterator().next().ownerId());
     
        
        g = new GameState(2, board, players, bombs, explosions, blasts);
        g = g.next(speedChangeEvents, new HashSet<>(bombDropEvents));
        b = g.bombedCells().values();
        assertEquals(PlayerID.PLAYER_2, b.iterator().next().ownerId());
        
        
        g = new GameState(23, board, players, bombs, explosions, blasts);
        g = g.next(speedChangeEvents, new HashSet<>(bombDropEvents));
        b = g.bombedCells().values();
        assertEquals(PlayerID.PLAYER_2, b.iterator().next().ownerId());
        
        g = new GameState(27, board, players, bombs, explosions, blasts);
        g = g.next(speedChangeEvents, new HashSet<>(bombDropEvents));
        b = g.bombedCells().values();
        assertEquals(PlayerID.PLAYER_2, b.iterator().next().ownerId());
        
        bombDropEvents1.add(PlayerID.PLAYER_3);
        g = new GameState(0, board, players, bombs, explosions, blasts);
        g = g.next(speedChangeEvents, new HashSet<>(bombDropEvents1));
        b = g.bombedCells().values();
        assertEquals(1, g.bombedCells().size());
        assertEquals(PlayerID.PLAYER_1, b.iterator().next().ownerId());
    }
   
    
    @Test
    public void testOnNext(){
        System.out.println("testOnNext");
        Board board = board();
        List<Player> players = players();
        List<Bomb> bombs = bombs();
        List<Sq<Sq<Cell>>> explosions = explosions();
        List<Sq<Cell>> blasts = new ArrayList<>();
        GameState g = new GameState(0, board, players, bombs, explosions, blasts);
        
        System.out.println("Etat initial:");
        GameStatePrinter.printGameState(g);
        
        Set<PlayerID> bombDropEvents = new HashSet<>();
        Map<PlayerID, Optional<Direction>> speedChangeEvents = new HashMap<>();
        for (int i = 0; i < 100; i++) {
            
            
            if(i == Ticks.EXPLOSION_TICKS+15){
                bombDropEvents.add(PlayerID.PLAYER_1);
                bombDropEvents.add(PlayerID.PLAYER_3);
            }
            System.out.println();
            System.out.println(i+") ");
            
            g = g.next(speedChangeEvents, bombDropEvents);
            
            GameStatePrinter.printGameState(g);
        }

    }
  
    @Test
    public void testOnExplodedBonus(){
        System.out.println("testOnExplodedBonus");
        
        List<Bomb> bombs = new ArrayList<>();
        bombs.add(new Bomb(PlayerID.PLAYER_1, new Cell(6,6), 5, 5));
        List<Sq<Sq<Cell>>> explosions = new ArrayList<>();
        List<Sq<Cell>> blasts = new ArrayList<>();
        Board board = oneElementBoard(Block.BONUS_BOMB);
        GameState g = new GameState(0, board, players(), bombs, explosions, blasts);
        System.out.println("Etat inital");
        GameStatePrinter.printGameStateWithoutPlayers(g);
        
        Map<PlayerID, Optional<Direction>> speedChangeEvents = new HashMap<>();
        Set<PlayerID> bombDropEvents = new HashSet<>();
        for (int i = 1; i <= 100; i++) {
            System.out.println(i+") ");
            g = g.next(speedChangeEvents, bombDropEvents);
            GameStatePrinter.printGameStateWithoutPlayers(g);
        }
        
    }
    @Test
    public void DeadDeposingBomb(){
        System.out.println("DeadDeposingBomb");
        List <Player> players = players();
        players.set(0, new Player(PlayerID.PLAYER_1, 0, new Cell(5, 5), 5, 5));
        Board board = oneElementBoard(Block.FREE);
        GameState g = new GameState(board, players);
        Map<PlayerID, Optional<Direction>> speedChangeEvents = new HashMap<>();
        Set<PlayerID> bombDropEvents = new HashSet<>();
        bombDropEvents.add(PlayerID.PLAYER_1);
        g = g.next(speedChangeEvents, bombDropEvents);
        GameStatePrinter.printGameStateWithoutPlayers(g);
    }
    
    @Test
    public void conflictOnBonus(){
        Board board = oneElementBoard(Block.BONUS_RANGE);
        List<Player> players = new ArrayList<>(Arrays.asList(new Player(PlayerID.PLAYER_1, 5, new Cell(7, 6), 5, 5),
                new Player(PlayerID.PLAYER_2, 5, new Cell(7, 6), 5, 5),
                new Player(PlayerID.PLAYER_3, 5, new Cell(7, 6), 5, 5),
                new Player(PlayerID.PLAYER_4, 5, new Cell(7, 6), 5, 5)));

        List<Bomb> bombs = new ArrayList<>();
        List<Sq<Sq<Cell>>> explosions = new ArrayList<>();
        List<Sq<Cell>> blasts = new ArrayList<>();
        GameState g = new GameState(0, board, players, bombs, explosions, blasts);
        
        GameStatePrinter.printGameStateWithoutPlayers(g);
        
        Map<PlayerID, Optional<Direction>> speedChangeEvents = new HashMap<>();
        Set<PlayerID> bombDropEvents = new HashSet<>();
        g = g.next(speedChangeEvents, bombDropEvents);

        assertEquals(6, g.players().get(0).bombRange());
        
 
        board = oneElementBoard(Block.BONUS_BOMB);
        g = new GameState(0, board, players, bombs, explosions, blasts);
        g = g.next(speedChangeEvents, bombDropEvents);
        
        assertEquals(6, g.players().get(0).maxBombs());

    }
    
    
//    @Test
//    public void testOnNextBoard(){
//        Board b = board();
//        Set<Cell> consumedBonuses = new HashSet<>();
//        consumedBonuses.add(new Cell(5,2));
//        Set<Cell> blastedCells1 = new HashSet<>();
//        blastedCells1.add(new Cell(3,2));
//        blastedCells1.add(new Cell(2,7));
//        
////        List<Sq<Sq<Cell>>> explosions = new ArrayList<>();
//        List<Sq<Cell>> blasts = new ArrayList<>();
//        GameState g = new GameState(0, b, players(), bombs(), explosions, blasts);
//        GameStatePrinter.printGameState(g);
//        System.out.println();
//        for (int i = 0; i < Ticks.BONUS_DISAPPEARING_TICKS+5; i++) {
//            b = GameState.nextBoard(b, consumedBonuses, blastedCells1);
//            g = new GameState(i, b, players(), bombs(), explosions, blasts);
//            GameStatePrinter.printGameState(g);
//            System.out.println(i+" "+ GameState.touchedBonus.size());
//        }
}
