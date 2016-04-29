package ch.epfl.xblast.testPerso.etape1a6;

import org.junit.Test;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.SubCell;

public class SubCellTest {

	@Test
	public void isCentral(){
		SubCell c = new SubCell(40,24);
		System.out.println(c.isCentral());
		
	}

}
