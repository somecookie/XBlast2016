import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.server.GameStateSerializer;
import ch.epfl.xblast.server.Level;
import ch.epfl.xblast.server.debug.RandomEventGenerator;
import ch.epfl.xblast.server.image.BoardPainter;
import ch.xblast.client.GameState;
import ch.xblast.client.GameStateDeserializer;
import ch.xblast.client.KeyboardEventHandler;
import ch.xblast.client.XblastComponent;

public class TestDivers {


	public static List<Byte> toByte(List<Integer> l) {
		List<Byte> newL = new ArrayList<>();
		for (Integer i : l) {
			newL.add((byte) i.intValue());
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
