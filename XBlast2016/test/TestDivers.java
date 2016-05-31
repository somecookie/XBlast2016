import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.RunLengthEncoder;

public class TestDivers {

	public static void main(String[] args) {
		List<Integer> list = new ArrayList<>(Arrays.asList(50, 2, 1, 0, -2, 3, 1, -2, 3, 1, 0, 0, -5, 3, 0, 0, 1, -2, 3,
				0, 0, 1, 3, 3, 0, 1, 1, 3, -5, 1, -9, 0, -5, 1, -9, 0, 1, -5, 0, 5, 0, 0, 3, 1, 0, -7, 3, 0, 1, 3, 0, 0,
				1, 3, -6, 0, 3, 3, 1, 0, -1, 1, 0, 1, 3, -8, 0, -3, 3, 6, -3, 0));
		List<Byte> l = new ArrayList<>();
		for (Integer i : list) {
			l.add((byte) i.intValue());
		}
		l = RunLengthEncoder.decode(l);

		l.addAll(Collections.nCopies(51, (byte)0));
		System.out.println(l.size());
		l = spiralToRowMajorOrder(l);

		for (int i = 0; i < l.size(); i++) {
			if (i % Cell.COLUMNS == 0)
				System.out.println();
			else {
				byte b = l.get(i);
				switch (b) {
				case 0:
					System.out.print("  ");
					break;
				case 1:
					System.out.print("  ");
					break;
				case 2:
					System.out.print("XX");
					break;
				case 3:
					System.out.print("??");
					break;
				case 4:
					System.out.print("§§");
					break;
				case 5:
					System.out.print("B+");
					break;
				case 6:
					System.out.print("R+");
					break;
				default:
					throw new Error();
				}
			}
		}

	}

	private static List<Byte> spiralToRowMajorOrder(List<Byte> list) throws ArrayIndexOutOfBoundsException {
		Byte[] rowMajorOrder = new Byte[list.size()];

		int i = 0;
		System.out.println(Cell.SPIRAL_ORDER.size());
		for (Cell cell : Cell.SPIRAL_ORDER) {
			rowMajorOrder[cell.rowMajorIndex()] = list.get(i++);
		}

		return Arrays.asList(rowMajorOrder);
	}

}
