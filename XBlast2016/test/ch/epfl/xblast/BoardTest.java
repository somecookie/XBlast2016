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
import java.util.List;

import org.junit.Test;

import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;

public class BoardTest {

    @Test(expected = IllegalArgumentException.class)
    public void ofInnerBlocksWalledThrowsException() {
        List<List<Block>> list = new ArrayList<>();
        Board b = Board.ofInnerBlocksWalled(list);
    }
    // NEED TO MAKE CHECKBLOCKMMATRIX PUBLIC BEFORE RUN

    @Test(expected = IllegalArgumentException.class)
    public void checkBlockMatrixThrowsExceptionOnNullMatrix() {
        List<List<Block>> lol = null;
        Board.checkBlockMatrix(lol, 2, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkBlockMatrixThrowsExceptionOnIncorrectMatrix() {
        List<List<Block>> lol = new ArrayList<List<Block>>();
        for (int i = 0; i < 10; i++) {
            List<Block> lol2 = new ArrayList<Block>();
            for (int j = 0; j < 15; j++) {
                lol2.add(Block.FREE);
            }
            lol.add(lol2);
        }
        List<Block> lol2 = new ArrayList<Block>();
        for (int j = 0; j < 14; j++) {
            lol2.add(Block.FREE);
        }
        lol.add(lol2);

        Board.checkBlockMatrix(lol, 11, 15);
    }


//    @Test
//    public void testOnofQuadrantBloclsWalled(){
//        List<List<Block>> a = new ArrayList<>();
//        for (int i = 0; i < 6; i++) {
//            for (int j = 0; j < 7; j++) {
//                a.get(i).add(Block.DESTRUCTIBLE_WALL);
//            }   
//        }
//        a.get(2).set(2, Block.FREE);
//        
//        Board b = Board.ofQuadrantNWBlocksWalled(a);
//        
//        assertEquals(Block.FREE, b.blockAt(new Cell(11,2)) );
//        assertEquals(Block.FREE, b.blockAt(new Cell(11,9)) );
//        assertEquals(Block.FREE, b.blockAt(new Cell(2,9)) );
//        
//                
//        
//    }
}