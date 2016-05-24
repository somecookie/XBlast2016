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
	public static void main(String[] args) {
		Map<Integer, Character> m = new HashMap<>();
		m.put(5, 'c');
		m.put(5, 'q');
		m.put(6, '3');
		System.out.println(m);
	}
}
