package ch.epfl.xblast;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.Test;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;
import ch.epfl.xblast.server.GameState;
import ch.epfl.xblast.server.Player;
import ch.epfl.xblast.server.Player.DirectedPosition;
import ch.epfl.xblast.server.Player.LifeState;

public class NextPlayerTest {

    //Mettre directedPos et SqCreator en pubic pour le test
    
//    private static Board oneElementBoard(Block b){
//        List<Block> free = Collections.nCopies(13, Block.FREE);
//        List<Block> a = Collections.nCopies(6, Block.FREE);
//        List<Block> notFree = new ArrayList<>(a);
//        notFree.add(b);
//        notFree.addAll(a);
//        List<List<Block>> board = new ArrayList<>();
//        for (int i = 0; i < 11; i++) {
//            if(i==5){
//                board.add(new ArrayList<>(notFree));
//            }
//            else{
//                board.add(new ArrayList<>(free));
//            }
//        }
//        return Board.ofInnerBlocksWalled(board);
//    }
//    
//    @Test
//    public void testOnDirectedPosNormalCase() {
//        System.out.println("normal");
//        Sq<LifeState> l = Player.SqCreator(5);
//        DirectedPosition pos = new DirectedPosition(new SubCell(8, 5), Direction.S);
//        Sq<DirectedPosition> directedPos = DirectedPosition.moving(pos);
//        Player p = new Player(PlayerID.PLAYER_1, l, directedPos , 5, 5);
//        
//        Optional<Direction> dir = Optional.of(Direction.E);
//        
//        directedPos = GameState.directedPos(p, dir);
//        
//        for (int i = 0; i < 20; i++) {
//            System.out.println(directedPos.head().position());
//            directedPos = directedPos.tail();
//            
//        }
//        System.out.println();
//    }
//    
//    @Test
//    public void testOnDirectedPosStop() {
//        System.out.println("Stop");
//        Sq<LifeState> l = Player.SqCreator(5);
//        DirectedPosition pos = new DirectedPosition(new SubCell(8, 5), Direction.S);
//        Sq<DirectedPosition> directedPos = DirectedPosition.moving(pos);
//        Player p = new Player(PlayerID.PLAYER_1, l, directedPos , 5, 5);
//        
//        Optional<Direction> dir = Optional.empty();
//        
//        directedPos = GameState.directedPos(p, dir);
//        
//        for (int i = 0; i < 20; i++) {
//            System.out.println(directedPos.head().position());
//            directedPos = directedPos.tail();   
//        }
//        System.out.println();
//    }
//    
//    @Test
//    public void testOnDirectedPosCentralCell() {
//        System.out.println("Central");
//        Sq<LifeState> l = Player.SqCreator(5);
//        DirectedPosition pos = new DirectedPosition(new SubCell(8, 8), Direction.S);
//        Sq<DirectedPosition> directedPos = DirectedPosition.moving(pos);
//        Player p = new Player(PlayerID.PLAYER_1, l, directedPos , 5, 5);
//        
//        Optional<Direction> dir = Optional.of(Direction.E);
//        
//        directedPos = GameState.directedPos(p, dir);
//        
//        for (int i = 0; i < 20; i++) {
//            System.out.println(directedPos.head().position());
//            directedPos = directedPos.tail();
//            
//        }
//        System.out.println();
//    }
//    
//    @Test
//    public void testOnBlockedByWall(){
//        Board board1 = oneElementBoard(Block.FREE);
//        Sq<LifeState> l = Player.SqCreator(5);
//        
//        DirectedPosition pos = new DirectedPosition(new SubCell(24, 24), Direction.N);
//        
//        Sq<DirectedPosition> directedPos = DirectedPosition.moving(pos);
//        Player p = new Player(PlayerID.PLAYER_1, l, directedPos, 5 , 5);
//        Set<Cell> bombs = new HashSet<>();
//        assertTrue(GameState.blocked(p, directedPos, board1, bombs ));
//    }
//    
//    @Test
//    public void testOnBlockedByBomb(){
//        Board board1 = oneElementBoard(Block.FREE);
//        Sq<LifeState> l = Player.SqCreator(5);
//        DirectedPosition pos = new DirectedPosition(new SubCell(27, 24), Direction.W);
//        Sq<DirectedPosition> directedPos = DirectedPosition.moving(pos);
//        Player p = new Player(PlayerID.PLAYER_1, l, directedPos, 5 , 5);
//        Set<Cell> bombs = new HashSet<>();
//        bombs.add(new Cell(1,1));
//        assertTrue(GameState.blocked(p, directedPos, board1, bombs ));
//
//    }
//    @Test
//    public void testOppositeDirectionOfBomb(){
//        Board board1 = oneElementBoard(Block.FREE);
//        Sq<LifeState> l = Player.SqCreator(5);
//        DirectedPosition pos = new DirectedPosition(new SubCell(27, 24), Direction.E);
//        Sq<DirectedPosition> directedPos = DirectedPosition.moving(pos);
//        Player p = new Player(PlayerID.PLAYER_1, l, directedPos, 5 , 5);
//        Set<Cell> bombs = new HashSet<>();
//        bombs.add(new Cell(1,1));
//        assertFalse(GameState.blocked(p, directedPos, board1, bombs ));
//
//    }
//    
//    @Test
//    public void testOnUnAbleToMove(){
//        Board board1 = oneElementBoard(Block.FREE);
//        Sq<LifeState> l = Player.SqCreator(0);
//        
//        DirectedPosition pos = new DirectedPosition(new SubCell(24, 24), Direction.S);
//        
//        Sq<DirectedPosition> directedPos = DirectedPosition.moving(pos);
//        Player p = new Player(PlayerID.PLAYER_1, l, directedPos, 5 , 5);
//        Set<Cell> bombs = new HashSet<>();
//        assertTrue(GameState.blocked(p, directedPos, board1, bombs ));
//    }
//
//}
