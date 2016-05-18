package ch.epfl.xblast.server.debug;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MoveAction;

import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.server.GameStateSerializer;
import ch.epfl.xblast.server.Level;
import ch.epfl.xblast.server.image.BoardPainter;
import ch.xblast.client.GameState;
import ch.xblast.client.GameStateDeserializer;
import ch.xblast.client.KeyboardEventHandler;
import ch.xblast.client.XblastComponent;
import ch.epfl.xblast.*;

public class InterfaceTest {

	private static void createUI(XblastComponent xb) {
		JFrame window = new JFrame("Image viewer");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		xb.setSize(xb.getPreferredSize());

		window.add(xb);

		window.pack();
		window.setVisible(true);
	}

	public static void main(String[] args) throws InterruptedException {
		Level l = Level.DEFAULT_LEVEL;
		BoardPainter bp = l.boardPainter();
		ch.epfl.xblast.server.GameState gs = l.initialState();
		List<Byte> serial = GameStateSerializer.serialize(bp, gs);
		GameState g = GameStateDeserializer.deserializerGameState(serial);

		XblastComponent xbc = new XblastComponent();
		Map<Integer, PlayerAction> kb = new HashMap<>();
		kb.put(KeyEvent.VK_UP, PlayerAction.MOVE_N);
		kb.put(KeyEvent.VK_RIGHT, PlayerAction.MOVE_E);
		kb.put(KeyEvent.VK_DOWN, PlayerAction.MOVE_S);
		kb.put(KeyEvent.VK_LEFT, PlayerAction.MOVE_E);
		kb.put(KeyEvent.VK_SPACE, PlayerAction.DROP_BOMB);
		kb.put(KeyEvent.VK_SHIFT, PlayerAction.STOP);
		
		Consumer<PlayerAction> c = System.out::println;
		xbc.addKeyListener(new KeyboardEventHandler(kb, c));
		xbc.requestFocusInWindow();
		
		xbc.setGameState(g, PlayerID.PLAYER_1);

		SwingUtilities.invokeLater(() -> createUI(xbc));

		RandomEventGenerator events = new RandomEventGenerator(2016, 30, 100);

		while (!gs.isGameOver() || !gs.blastedCells().isEmpty()) {
			gs = gs.next(events.randomSpeedChangeEvents(), events.randomBombDropEvents());
			serial = GameStateSerializer.serialize(bp, gs);
			g = GameStateDeserializer.deserializerGameState(serial);
			xbc.setGameState(g, PlayerID.PLAYER_1);
			Thread.sleep(20);
		}
		

	}
}
