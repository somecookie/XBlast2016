import java.awt.Image;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.RunLengthEncoder;

public class TestDivers {

	public static void main(String[] args) {
		List<Cell> spiral = Cell.SPIRAL_ORDER;
		List<Cell> rowMajorE = Cell.ROW_MAJOR_ORDER;
		List<Cell> rowMajorA = spiralToRowMajorOrder(spiral);
		for (int i = 0; i < rowMajorE.size(); i++) {
			if(!rowMajorA.get(i).equals(rowMajorE.get(i)))
				System.out.println("bug");
		}
		System.out.println("done");
	}
	
	public static List<Byte> toByte(List<Integer> l){
		List<Byte> newL = new ArrayList<>();
		for(Integer i : l){
			newL.add((byte)i.intValue());
		}
		return newL;
		
	}
	
	/**
	 * Change the list's order , from the spiral order to the row major order
	 * 
	 * @param list
	 *            the list we will transform
	 * @return the transformed list
	 */
	public static List<Cell> spiralToRowMajorOrder(List<Cell> list) {

		Cell[] rowMajorOrder = new Cell[list.size()];

		int i = 0;
		for (Cell cell : Cell.SPIRAL_ORDER) {
			rowMajorOrder[cell.rowMajorIndex()] = list.get(i++);
		}

		return Arrays.asList(rowMajorOrder);
	}

}
