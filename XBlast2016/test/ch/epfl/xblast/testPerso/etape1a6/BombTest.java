/**
 * @author Ricardo Ferreira Ribeiro (250798)
 * @author Clarise Estelle Fleurimont (246866)
 * @date 7 mars 2016
 */
package ch.epfl.xblast.testPerso.etape1a6;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.junit.Test;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;
import ch.epfl.xblast.server.Bomb;
import ch.epfl.xblast.server.GameState;
import ch.epfl.xblast.server.Ticks;

public class BombTest {
    

    private Bomb bombSimulator() {
        return new Bomb(PlayerID.PLAYER_1, new Cell(3, 3), 5, 3);
    }
    
    @Test
    public void testOnExplosion(){
        Bomb b = bombSimulator();
        List<Sq<Sq<Cell>>> expl = b.explosion();
        Sq<Sq<Cell>> e = expl.get(0);
        
        for (int i = 0; i < Ticks.EXPLOSION_TICKS+2; i++) {
            System.out.println(i);
            if(!e.isEmpty()){
                System.out.println(e.head().head());
                e = e.tail();
            }
        }
        assertEquals(4, expl.size());
    }
    @Test
    public void testOnCOnstruction(){
        int fuseLength = 10;
        Sq<Integer> fuseLengths = Objects.requireNonNull(Sq.iterate(fuseLength, u -> u - 1).limit(fuseLength));
        
        for (int i = 0; i < 10; i++) {
            assertEquals(10-i, fuseLengths.head().intValue());
            fuseLengths = fuseLengths.tail();
        }
    }
    
  
    
//    @Test
//    public void testOnExplosionArmTowards(){
//        Bomb b = bombSimulator();
//        Sq<Sq<Cell>> bomb = b.explosionArmTowards(Direction.E);
//        for (int i = 0; i < b.range(); i++) {
//            Sq<Cell> bomb1 = bomb.head();
//            for (int j = 0; j < 5; j++) {
//               System.out.print(bomb1.head()+", ");
//               bomb1 = bomb1.tail();
//            }
//            System.out.println();
//            bomb = bomb.tail();
//        }
//    }

    @Test(expected = IllegalArgumentException.class)
    public void secondBuilderThrowsCorrectExeption_1() {
        Bomb test = new Bomb(PlayerID.PLAYER_1, new Cell(0, 0), 4, -2);
    }

    @Test(expected = NullPointerException.class)
    public void secondBuilderThrowsCorrectExeption_2() {
        Bomb test = new Bomb(PlayerID.PLAYER_1, new Cell(0, 0), null, 2);
    }

    @Test(expected = NullPointerException.class)
    public void secondBuilderThrowsCorrectExeption_3() {
        Bomb test = new Bomb(PlayerID.PLAYER_1, null, 4, 2);
    }

    @Test(expected = NullPointerException.class)
    public void secondBuilderThrowsCorrectExeption_4() {
        Bomb test = new Bomb(null, new Cell(0, 0), 5, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void firstBuilderThrowsCorrectExeption_1() {
        
        Bomb test = new Bomb(PlayerID.PLAYER_1, new Cell(0, 0), Sq.constant(2), -2);
    }

    @Test(expected = NullPointerException.class)
    public void firstBuilderThrowsCorrectExeption_2() {
        Bomb test = new Bomb(PlayerID.PLAYER_1, new Cell(0, 0), null, 2);
    }

    @Test(expected = NullPointerException.class)
    public void firstBuilderThrowsCorrectExeption_3() {
        Bomb test = new Bomb(PlayerID.PLAYER_1, null, Sq.constant(2), 2);
    }

    @Test(expected = NullPointerException.class)
    public void firstBuilderThrowsCorrectExeption_4() {
        Bomb test = new Bomb(null, new Cell(0, 0), Sq.constant(2), 2);
    }

    @Test
    public void gettersCorectlyGetVariables() {
        Bomb b = bombSimulator();
        assertEquals(b.ownerId(), PlayerID.PLAYER_1);
        assertEquals(b.position(), new Cell(3, 3));
        assertEquals(b.fuseLength(), 5);
        assertEquals(b.range(), 3);
    }
}
