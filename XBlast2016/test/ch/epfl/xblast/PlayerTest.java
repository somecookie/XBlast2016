package ch.epfl.xblast;
import static org.junit.Assert.*;

import org.junit.Test;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.server.Player;
import ch.epfl.xblast.server.Player.DirectedPosition;
import ch.epfl.xblast.server.Player.LifeState;
import ch.epfl.xblast.server.Player.LifeState.State;
import ch.epfl.xblast.server.Ticks;

public class PlayerTest {

    @Test(expected = IllegalArgumentException.class)
    public void negativeLiveTest() {
        Cell c = new Cell(0, 0);
        Player p = new Player(PlayerID.PLAYER_1, -1, c, 5, 5);
    }
    @Test
    public void deadPlayer(){
        SubCell c = new SubCell(0, 0);
        
        Sq<DirectedPosition> a = Sq.constant(new DirectedPosition(c, Direction.E));
        Player p = new Player(PlayerID.PLAYER_1, 0, c.containingCell(), 5, 5);
        assertEquals(State.DEAD, p.lifeState().state());
        for (int i = 0; i < 2*Ticks.PLAYER_DYING_TICKS; i++) {
            p = new Player(PlayerID.PLAYER_1, p.lifeStates().tail() ,a, 5, 5);
        }
        assertEquals(State.DEAD, p.lifeState().state());
        
    }
//    @Test
//    public void SqCreatorTest(){
//        Sq<LifeState> a = Player.SqCreator(5);
//        assertEquals(State.INVULNERABLE, a.head().state());
//        for (int i = 1; i <= 70; i++) {
//            a = a.tail();
//            System.out.println(i+") et "+a.head().state());
//            
//        }
//        assertEquals(State.VULNERABLE, a.head().state());
//    }
    
     @Test 
     public void normalSecondaryConstructor(){
         Cell c = new Cell(0, 0);
         SubCell sC = SubCell.centralSubCellOf(c);
         Player p = new Player(PlayerID.PLAYER_1, 5, c, 4, 3);
         assertEquals(5, p.lives());
         assertEquals(4, p.maxBombs() );
         assertEquals(3, p.bombRange());
         assertEquals(State.INVULNERABLE, p.lifeState().state());

         for (int i = 0; i < 2*Ticks.PLAYER_INVULNERABLE_TICKS; i++) {
             p = new Player(PlayerID.PLAYER_1, p.lifeStates().tail(),p.directedPositions().tail() , 4,3);
             
        }
     }
     
     @Test
     public void nextLifeDead(){
         Player p = new Player(PlayerID.PLAYER_1, 0, new Cell(0, 0), 5, 5);
         Sq<LifeState> a = p.statesForNextLife();
         assertEquals(State.DYING, a.head().state());
         for (int i = 0; i < Ticks.PLAYER_DYING_TICKS+2; i++) {
            a = a.tail();
        }
         assertEquals(State.DEAD, a.head().state());
     }
     @Test
     public void nextLifeNormal(){
         Player p = new Player(PlayerID.PLAYER_1, 5, new Cell(0, 0), 5, 5);
         Sq<LifeState> a = p.statesForNextLife();
         
         assertEquals(State.DYING, a.head().state());
         for (int i = 0; i < Ticks.PLAYER_DYING_TICKS+2; i++) {
            a = a.tail();
        }
         assertEquals(State.INVULNERABLE, a.head().state());
         for (int i = 0; i < Ticks.PLAYER_INVULNERABLE_TICKS; i++) {
             a = a.tail();
         }
         assertEquals(4, a.head().lives());
         assertEquals(State.VULNERABLE, a.head().state());
     }
     @Test
     public void isAliveTest(){
         Player p = new Player(PlayerID.PLAYER_1, 0, new Cell(0,0), 0, 0);
         assertFalse(p.isAlive());
         p = new Player(PlayerID.PLAYER_1, 1, new Cell(0,0), 0, 0);
         assertTrue(p.isAlive());
     }
     
      @Test
      public void idTest(){
          Player p = new Player(PlayerID.PLAYER_1, 5, new Cell(0,0), 5, 5);
          assertTrue(p.id().equals(PlayerID.PLAYER_1));
      }
}


