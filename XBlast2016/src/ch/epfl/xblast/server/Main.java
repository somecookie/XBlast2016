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
import ch.epfl.xblast.server.image.BoardPainter;

public class Main {
	private static int STD_PLAYERS = 4;
	private static Map<Byte, Optional<Direction>> directions = directions();
	private static PlayerID[] ids = PlayerID.values();
	private static PlayerAction[] plA = PlayerAction.values();

	public static void main(String[] args) throws IOException, InterruptedException {

		int minPlayers = (args.length <= 0) ? STD_PLAYERS : Integer.parseInt(args[0]);
		Map<SocketAddress, PlayerID> players = new HashMap<>();

		DatagramChannel channel = DatagramChannel.open(StandardProtocolFamily.INET);
		channel.bind(new InetSocketAddress(2016));

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
				Thread.sleep(sleepingTime);
			}

			readingNewInputs(channel, players, bombDropEvents, speedChangeEvents);

			gS = gS.next(speedChangeEvents, bombDropEvents);

		}

		System.out.println(gS.winner());
	}

	private static Map<Byte, Optional<Direction>> directions() {
		Map<Byte, Optional<Direction>> dir = new HashMap<>();

		dir.put((byte) PlayerAction.MOVE_E.ordinal(), Optional.of(Direction.E));
		dir.put((byte) PlayerAction.MOVE_N.ordinal(), Optional.of(Direction.N));
		dir.put((byte) PlayerAction.MOVE_S.ordinal(), Optional.of(Direction.S));
		dir.put((byte) PlayerAction.MOVE_W.ordinal(), Optional.of(Direction.W));
		dir.put((byte) PlayerAction.STOP.ordinal(), Optional.empty());

		return dir;
	}

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

	private static void initialization(Map<SocketAddress, PlayerID> players, int minPlayers, DatagramChannel channel)
			throws IOException {

		ByteBuffer buffer = ByteBuffer.allocate(1);
		SocketAddress senderAddress;

		while (players.size() != minPlayers) {

			senderAddress = channel.receive(buffer);
			if (buffer.get(0) == PlayerAction.JOIN_GAME.ordinal() && !players.containsValue(senderAddress)) {
				players.put(senderAddress, ids[players.size()]);
			}
		}
	}

	private static void readingNewInputs(DatagramChannel channel, Map<SocketAddress, PlayerID> players,
			Set<PlayerID> bombDropEvents, Map<PlayerID, Optional<Direction>> speedChangeEvents) throws IOException {

		ByteBuffer buffer = ByteBuffer.allocate(1);
		PlayerID id;
		Byte b;

		SocketAddress senderAddress = channel.receive(buffer);
		while (senderAddress != null) {

			id = players.get(senderAddress);
			b = buffer.get(0);

			if (b == (byte) PlayerAction.DROP_BOMB.ordinal()) {
				bombDropEvents.add(id);
			} else if (plA[b].isMove()) {

				Optional<Direction> dir = directions.get(buffer.get(0));
				speedChangeEvents.put(id, dir);
			}

			senderAddress = channel.receive(buffer);
		}
	}

	private static long sleepingTime(long startingTime, int nextTicks) {

		long nextTime = startingTime + nextTicks * Ticks.TICK_NANOSECOND_DURATION;
		long currentTime = System.nanoTime();
		long diffTime = nextTime - currentTime;

		return diffTime;
	}

}
