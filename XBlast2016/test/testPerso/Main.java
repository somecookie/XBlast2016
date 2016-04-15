package testPerso;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.SubCell;

public class Main {

	public static void main(String[] args) {
		Cell c = new Cell(4, 4);
		SubCell sc = new SubCell(24, 45); 
		System.out.println(c.equals(24));

	}

}
