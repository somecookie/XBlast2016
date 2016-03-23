/**
 * @author Ricardo Ferreira Ribeiro (250798)
 * @author Clarise Estelle Fleurimont (246866)
 * @date 7 mars 2016
 */
package ch.epfl.xblast;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.server.Bomb;

public class BombTest {

    private Bomb bombSimulator() {
        return new Bomb(PlayerID.PLAYER_1, new Cell(3, 3), 5, 3);
    }

    @Test
    public void explosionCreatesCorrectExplosion() {
        Bomb bomb = bombSimulator();
        List<Sq<Sq<Cell>>> list = bomb.explosion();
    }

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
