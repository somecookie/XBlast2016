package testPerso;

import org.junit.Test;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.SubCell;

public class SubCellTest {

	@Test
	public void isCentral(){
		SubCell c = new SubCell(40,24);
		System.out.println(c.isCentral());
		
	}
	@Test
	public void neighborTest(){
		SubCell c = new SubCell(0,0);
		SubCell p = c.neighbor(Direction.N);
		System.out.println(p);	
		p = c.neighbor(Direction.W);
		System.out.println(p);
	}
}
	
	


