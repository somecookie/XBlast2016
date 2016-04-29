package ch.epfl.xblast.testPerso.etape1a6;

import org.junit.Test;

import ch.epfl.xblast.server.Block;
import static org.junit.Assert.*;

public class BlockAndBonusTest {
    @Test
    public void testOnIsBonus(){
        Block b = Block.BONUS_BOMB;
        Block b2 = Block.BONUS_RANGE;
        assertTrue(b.isBonus() && b2.isBonus());
    }
}
