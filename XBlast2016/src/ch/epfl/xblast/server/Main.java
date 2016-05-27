/**
 * @author Ricardo Ferreira Ribeiro (250798)
 * @author Eleonore Pochon (262959)
 */
package ch.epfl.xblast.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerAction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.Time;

public class Main {

	private static Map<Byte, Optional<Direction>> directions = directions();
	private static PlayerAction[] plA = PlayerAction.values();
	private static int PORT = 2016;

	public static void main(String[] args) throws IOException, InterruptedException {

		int minPlayers = (args.length <= 0) ? PlayerID.NB_PLAYERS : Integer.parseInt(args[0]);
		Map<SocketAddress, PlayerID> players = new HashMap<>();

		DatagramChannel channel = DatagramChannel.open(StandardProtocolFamily.INET);
		channel.bind(new InetSocketAddress(PORT));

		initialization(players, minPlayers, channel);

		Level level = Level.DEFAULT_LEVEL;
		GameState gS = level.initialState();
		BoardPainter bP = level.boardPainter();
		Map<PlayerID, Optional<Direction>> speedChangeEvents = new HashMap<>();
		Set<PlayerID> bombDropEvents = new HashSet<>();

		channel.configureBlocking(false);

		long sleepingTime = 0;
		long startingTime = System.nanoTime();

		while (!gS.isGameOver()) {

			sendGameState(channel, gS, bP, players);

			sleepingTime = sleepingTime(startingTime, gS.ticks() + 1);

			if (sleepingTime > 0) {

				long millis = (long) Math.floor(sleepingTime / Time.US_PER_S);
				int nanos = (int) (sleepingTime - millis * Time.US_PER_S);
				Thread.sleep(millis, nanos);
			}

			readingNewInputs(channel, players, bombDropEvents, speedChangeEvents);

			gS = gS.next(speedChangeEvents, bombDropEvents);
			speedChangeEvents.clear();
			bombDropEvents.clear();

		}

		if (gS.winner().isPresent()) {
			System.out.println("The winner is the player " + (gS.winner().get().ordinal() + 1));
		} else {
			System.out.println("It's a draw!!!");
		}
	}

	/**
	 * Calculate the player's action and its directions
	 * 
	 * @return the direction in function of the player's action
	 */
	private static Map<Byte, Optional<Direction>> directions() {
		Map<Byte, Optional<Direction>> dir = new HashMap<>();

		dir.put((byte) PlayerAction.MOVE_E.ordinal(), Optional.of(Direction.E));
		dir.put((byte) PlayerAction.MOVE_N.ordinal(), Optional.of(Direction.N));
		dir.put((byte) PlayerAction.MOVE_S.ordinal(), Optional.of(Direction.S));
		dir.put((byte) PlayerAction.MOVE_W.ordinal(), Optional.of(Direction.W));
		dir.put((byte) PlayerAction.STOP.ordinal(), Optional.empty());

		return dir;
	}

	/**
	 * Do the game state as he can be sent to the client
	 * 
	 * @param channel
	 *            the given channel
	 * @param gS
	 *            the game state that will be sent
	 * @param bP
	 *            the board painter that will be sent
	 * @param players
	 *            the given players
	 * @throws IOException
	 */
	private static void sendGameState(DatagramChannel channel, GameState gS, BoardPainter bP,
			Map<SocketAddress, PlayerID> players) throws IOException {

		List<Byte> gameState = GameStateSerializer.serialize(bP, gS);
		ByteBuffer buffer = ByteBuffer.allocate(gameState.size() + 1);
		buffer.put((byte) 0);

		for (Byte b : gameState) {
			buffer.put(b);
		}

		for (Entry<SocketAddress, PlayerID> p : players.entrySet()) {

			byte id = (byte) p.getValue().ordinal();
			buffer.put(0, id);
			buffer.flip();
			channel.send(buffer, p.getKey());

		}
	}

	/**
	 * Initialize the game (permit to add players to the game)
	 * 
	 * @param players
	 * @param minPlayers
	 * @param channel
	 * @throws IOException
	 */
	private static void initialization(Map<SocketAddress, PlayerID> players, int minPlayers, DatagramChannel channel)
			throws IOException {

		ByteBuffer buffer = ByteBuffer.allocate(1);
		SocketAddress senderAddress;

		while (players.size() < minPlayers) {

			senderAddress = channel.receive(buffer);

			if (buffer.get(0) == (byte) PlayerAction.JOIN_GAME.ordinal() && !players.containsKey(senderAddress)) {
				players.put(senderAddress, PlayerID.values()[players.size()]);
			}
			buffer.clear();
		}
	}

	/**
	 * Read the inputs
	 * 
	 * @param channel
	 * @param players
	 * @param bombDropEvents
	 * @param speedChangeEvents
	 * @throws IOException
	 */
	private static void readingNewInputs(DatagramChannel channel, Map<SocketAddress, PlayerID> players,
			Set<PlayerID> bombDropEvents, Map<PlayerID, Optional<Direction>> speedChangeEvents) throws IOException {

		ByteBuffer buffer = ByteBuffer.allocate(1);
		PlayerID id;
		Byte b;

		SocketAddress senderAddress = channel.receive(buffer);
		while (senderAddress != null) {
			id = players.get(senderAddress);
			buffer.flip();
			b = buffer.get();
			buffer.clear();

			if (b == (byte) PlayerAction.DROP_BOMB.ordinal()) {
				bombDropEvents.add(id);
			} else if (plA[b].isMove()) {

				Optional<Direction> dir = directions.get(b);
				speedChangeEvents.put(id, dir);
			}

			senderAddress = channel.receive(buffer);
		}
	}

	/**
	 * Calculate the sleeping time of the game
	 * 
	 * @param startingTime
	 * @param nextTicks
	 * @return
	 */
	private static long sleepingTime(long startingTime, int nextTicks) {

		long nextTime = startingTime + (long) nextTicks * Ticks.TICK_NANOSECOND_DURATION;
		long currentTime = System.nanoTime();
		long diffTime = nextTime - currentTime;

		return diffTime;
	}

}
