package ch.epfl.xblast;

import java.util.Optional;

import org.junit.Test;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.server.GameState;
import ch.epfl.xblast.server.Player;
import ch.epfl.xblast.server.Player.DirectedPosition;
import ch.epfl.xblast.server.Player.LifeState;

public class NextPlayerTest {

    @Test
    public void testOnDirectedPosNormalCase() {
        Sq<LifeState> l = Player.SqCreator(5);
        DirectedPosition pos = new DirectedPosition(new SubCell(8, 5), Direction.S);
        Sq<DirectedPosition> directedPos = DirectedPosition.moving(pos);
        Player p = new Player(PlayerID.PLAYER_1, l, directedPos , 5, 5);
        
        Optional<Direction> dir = Optional.of(Direction.E);
        
        directedPos = GameState.directedPos(p, dir);
        
        for (int i = 0; i < 20; i++) {
            System.out.println(directedPos.head().position());
            directedPos = directedPos.tail();
            
        }
        
    
    }

}
