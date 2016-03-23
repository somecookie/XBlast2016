package ch.epfl.xblast;

import ch.epfl.xblast.Direction;

import org.junit.Test;

class Tests {
    
    @Test()
    public void testOnOpposite(){
        assertEquals(Direction.N.opposite(), Direction.S);
        
        
    }

}
