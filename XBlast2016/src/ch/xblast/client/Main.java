package ch.xblast.client;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import ch.epfl.xblast.PlayerID;

import javax.management.ServiceNotFoundException;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import ch.epfl.xblast.PlayerAction;
import ch.epfl.xblast.Time;

public class Main {

	private static int MAX_BUFFER_SIZE = 410;
	
	/**
	 * Trade that does the graphic interface
	 * @param xbc
	 * @param channel
	 * @param hostAdress
	 */
	private static void createUI(XblastComponent xbc, DatagramChannel channel, SocketAddress hostAdress) {
		JFrame window = new JFrame("XBlast2016");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		
		xbc.setSize(xbc.getPreferredSize());

		Map<Integer, PlayerAction> kb = new HashMap<>();
		kb.put(KeyEvent.VK_UP, PlayerAction.MOVE_N);
		kb.put(KeyEvent.VK_RIGHT, PlayerAction.MOVE_E);
		kb.put(KeyEvent.VK_DOWN, PlayerAction.MOVE_S);
		kb.put(KeyEvent.VK_LEFT, PlayerAction.MOVE_W);
		kb.put(KeyEvent.VK_SPACE, PlayerAction.DROP_BOMB);
		kb.put(KeyEvent.VK_SHIFT, PlayerAction.STOP);
		ByteBuffer sendBuffer = ByteBuffer.allocate(1);
		Consumer<PlayerAction> c = (move)->{
			sendBuffer.put((byte) move.ordinal());
			try {
				sendBuffer.flip();
				channel.send(sendBuffer, hostAdress);
				sendBuffer.clear();
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
		xbc.addKeyListener(new KeyboardEventHandler(kb, c));

		window.getContentPane().add(xbc);
		window.pack();
		window.setVisible(true);

		xbc.requestFocusInWindow();
		
		

	}

	public static void main(String[] args) throws IOException, InterruptedException, InvocationTargetException {
		String hostName = (args.length <= 0) ? "localhost" : args[0];
		DatagramChannel channel = DatagramChannel.open(StandardProtocolFamily.INET);
		channel.configureBlocking(false);
		SocketAddress hostAdress = new InetSocketAddress(hostName, 2016);
		ByteBuffer sendBuffer = ByteBuffer.allocate(1);
		sendBuffer.put((byte) PlayerAction.JOIN_GAME.ordinal());

		ByteBuffer gSbuffer = ByteBuffer.allocate(MAX_BUFFER_SIZE);
		List<Byte> serialGs = new ArrayList<>();
		GameState gS;
		PlayerID id;
		XblastComponent xbc = new XblastComponent();
		
		SwingUtilities.invokeAndWait(() -> createUI(xbc, channel, hostAdress));

		while (channel.receive(gSbuffer)== null) {
			channel.send(sendBuffer, hostAdress);
			sendBuffer.rewind();
			Thread.sleep(Time.MS_PER_S);
		
		}

		channel.configureBlocking(true);

		while (true) {
			serialGs.clear();
			gSbuffer.flip(); 
			id = PlayerID.values()[gSbuffer.get()];
			
			while (gSbuffer.hasRemaining()) {
				serialGs.add(gSbuffer.get());
			}
			gSbuffer.clear();

			gS = GameStateDeserializer.deserializerGameState(serialGs);
			xbc.setGameState(gS, id);
			channel.receive(gSbuffer);
		}

	}

}
