package ch.epfl.xblast;
import static org.junit.Assert.*;

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
    
    private static List<Player> players(){
        Player p = new Player(PlayerID.PLAYER_1, 3, new Cell(5,4), 5, 5);
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
    
//    @Test
//    public void testOnNext(){
//        Board board = board();
//        List<Player> players = players();
//        List<Bomb> bombs = bombs();
//        List<Sq<Sq<Cell>>> explosions = explosions();
//        List<Sq<Cell>> blasts = new ArrayList<>();
//        GameState g = new GameState(0, board, players, bombs, explosions, blasts);
//        
//        System.out.println("Etat initial:");
//        GameStatePrinter.printGameState(g);
//        
//        Set<PlayerID> bombDropEvents = new HashSet<>();
//        Map<PlayerID, Optional<Direction>> speedChangeEvents = new HashMap<>();
//        for (int i = 0; i < 100; i++) {
//            
//            
//            if(i == Ticks.EXPLOSION_TICKS+15){
//                bombDropEvents.add(PlayerID.PLAYER_1);
//                bombDropEvents.add(PlayerID.PLAYER_3);
//            }
//            System.out.println();
//            System.out.println(i+") ");
//            
//            g = g.next(speedChangeEvents, bombDropEvents);
//            
//            GameStatePrinter.printGameState(g);
//        }
//
//    }
    
    @Test
    public void testOnExplodedBonus(){
        List<Block> free = Collections.nCopies(13, Block.FREE);
        List<Block> a = Collections.nCopies(6, Block.FREE);
        List<Block> notFree = new ArrayList<>(a);
        notFree.add(Block.BONUS_BOMB);
        notFree.addAll(a);
        List<List<Block>> b = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            if(i==5){
                b.add(new ArrayList<>(notFree));
            }
            else{
                b.add(new ArrayList<>(free));
            }
        }
        Board board = Board.ofInnerBlocksWalled(b);
        List<Bomb> bombs = new ArrayList<>();
        bombs.add(new Bomb(PlayerID.PLAYER_1, new Cell(6,6), 5, 5));
        List<Sq<Sq<Cell>>> explosions = new ArrayList<>();
        List<Sq<Cell>> blasts = new ArrayList<>();
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

    
//    @Test
//    public void testOnNextBoard(){
//        Board b = board();
//        Set<Cell> consumedBonuses = new HashSet<>();
//        consumedBonuses.add(new Cell(5,2));
//        Set<Cell> blastedCells1 = new HashSet<>();
//        blastedCells1.add(new Cell(3,2));
//        blastedCells1.add(new Cell(2,7));
//        
//        List<Sq<Sq<Cell>>> explosions = new ArrayList<>();
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
