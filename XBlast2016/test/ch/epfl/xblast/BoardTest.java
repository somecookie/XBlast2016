/**
 * @author Ricardo Ferreira Ribeiro (250798)
 * @author Clarise Estelle Fleurimont (246866)
 * @date 4 mars 2016
 */
/**
 * 
 */
package ch.epfl.xblast;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;

public class BoardTest {

    @Test(expected = IllegalArgumentException.class)
    public void testOnExeptionOfOfRows(){
        List<List<Block>> bl = new ArrayList<>();
        List<Block> bl1 = new ArrayList<>();
        
        for (int i = 0; i < 16; i++) {
            bl1.add(Block.FREE);
        }
        for (int i = 0; i < 13; i++) {
            bl.add(bl1);
        }
       
        Board b = Board.ofRows(bl);
    }
    
    @Test
    public void testOnOfRows(){
        List<List<Block>> bl = new ArrayList<>();
        List<Block> bl1 = new ArrayList<>();
        List<Block> bl2 = new ArrayList<>();
        
        for (int i = 0; i < 15; i++) {
            bl1.add(Block.FREE);
            bl2.add(Block.CRUMBLING_WALL);
        }
        for (int i = 0; i < 13; i++) {
            if(i%2 == 0){
                bl.add(bl1);
            }
            else{
                bl.add(bl2);
            }
        }
        Board b = Board.ofRows(bl);
        assertEquals(Block.FREE, b.blockAt(new Cell(0,0)));
        assertEquals(Block.FREE, b.blockAt(new Cell(14,12)));
        assertEquals(Block.CRUMBLING_WALL, b.blockAt(new Cell(0,1)));
        assertEquals(Block.FREE, b.blockAt(new Cell(3,6)));
        assertEquals(Block.CRUMBLING_WALL, b.blockAt(new Cell(5,9)));
        assertEquals(Block.CRUMBLING_WALL, b.blockAt(new Cell(14,11)));   
    }
    
    @Test
    public void testOnConstantOfRows(){
        List<List<Block>> bl = new ArrayList<>();
        List<Block> bl1 = new ArrayList<>();
        List<Block> bl2 = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            bl1.add(Block.FREE);
            bl2.add(Block.CRUMBLING_WALL);
        }
        for (int i = 0; i < 13; i++) {
            if(i%2 == 0){
                bl.add(bl1);
            }
            else{
                bl.add(bl2);
            }
        }
        Board b = Board.ofRows(bl);
        Sq<Block> a = b.blocksAt(new Cell(0,0));
        Sq<Block> a2 = b.blocksAt(new Cell(5,9));

        for (int i = 0; i < 100; i++) {
            assertEquals(Block.FREE, a.head());
            assertEquals(Block.CRUMBLING_WALL, a2.head());
            a = a.tail();
            a2 = a2.tail();
        }
    }
    
    @Test
    public void testOnOfInnersBlocksWalled(){
        List<List<Block>> bl = new ArrayList<>();
        List<Block> bl1 = new ArrayList<>();
        
        for (int i = 0; i < 13; i++) {
            bl1.add(Block.FREE);
        }
        for (int i = 0; i < 11; i++) {
            bl.add(bl1);
        }
        
        Board b = Board.ofInnerBlocksWalled(bl);
        
        assertEquals(Block.INDESTRUCTIBLE_WALL, b.blockAt(new Cell(0,0)));
        assertEquals(Block.INDESTRUCTIBLE_WALL, b.blockAt(new Cell(14,0)));
        assertEquals(Block.INDESTRUCTIBLE_WALL, b.blockAt(new Cell(14,12)));
        assertEquals(Block.INDESTRUCTIBLE_WALL, b.blockAt(new Cell(0,12)));
        assertEquals(Block.INDESTRUCTIBLE_WALL, b.blockAt(new Cell(1,12)));
        assertEquals(Block.INDESTRUCTIBLE_WALL, b.blockAt(new Cell(3,0)));
        assertEquals(Block.INDESTRUCTIBLE_WALL, b.blockAt(new Cell(14,6)));
        assertEquals(Block.FREE, b.blockAt(new Cell(6,6)));
        
    }
    
    @Test
    public void  ofQuadrantNWBlocksWalled(){
        List<List<Block>> bl = new ArrayList<>();
        List<Block> bl1 = new ArrayList<>();
        
        for (int i = 0; i < 7; i++) {
            bl1.add(Block.FREE);
        }
        for (int i = 0; i < 6; i++) {
            bl.add(bl1);
        }
        Board b = Board.ofQuadrantNWBlocksWalled(bl);
    }
}