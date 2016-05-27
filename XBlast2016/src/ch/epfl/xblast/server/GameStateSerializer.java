/**
 * @author Ricardo Ferreira Ribeiro (250798)
 * @author Eleonore Pochon (262959)
 */
package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.RunLengthEncoder;

public final class GameStateSerializer {

	/**
	 * Constructor empty because the class isn't instanciable
	 */
	private GameStateSerializer() {
	}

	/**
	 * Return a serialized state as a byte's list in function of a board painter
	 * and the game state
	 * 
	 * @param bp
	 *            the given board painter
	 * @param gs
	 *            the given game state
	 * @return the serialized state (List<Byte>)
	 */
	public static List<Byte> serialize(BoardPainter bp, GameState gs) {
		List<Byte> serialized = new ArrayList<>();
		List<Byte> serialBoard = new ArrayList<>();
		List<Byte> serialExpNdBombs = new ArrayList<>();
		List<Byte> serialPlayers = new ArrayList<>();

		List<Cell> spiral = Cell.SPIRAL_ORDER;
		List<Cell> rowMajor = Cell.ROW_MAJOR_ORDER;

		List<Player> players = gs.players();
		Map<Cell, Bomb> bombedCells = gs.bombedCells();

		for (Cell c : spiral) {
			serialBoard.add(bp.byteForCell(gs.board(), c));
		}
		serialBoard = RunLengthEncoder.encode(serialBoard);
		serialized.add((byte) serialBoard.size());
		serialized.addAll(serialBoard);

		for (Cell c : rowMajor) {
			if (bombedCells.containsKey(c)) {
				Bomb bomb = bombedCells.get(c);
				serialExpNdBombs.add(ExplosionPainter.byteForBomb(bomb));
			} else {
				Byte b = !gs.board().blockAt(c).isFree() ? ExplosionPainter.BYTE_FOR_EMPTY
						: ExplosionPainter.byteForBlast(c, gs);
				serialExpNdBombs.add(b);

			}
		}
		serialExpNdBombs = RunLengthEncoder.encode(serialExpNdBombs);
		serialized.add((byte) serialExpNdBombs.size());
		serialized.addAll(serialExpNdBombs);

		for (Player p : players) {
			Byte lives = (byte) p.lives();
			Byte xPos = (byte) p.position().x();
			Byte yPos = (byte) p.position().y();
			Byte image = PlayerPainter.byteForPlayer(gs.ticks(), p);
			List<Byte> thisPlayer = new ArrayList<>(Arrays.asList(lives, xPos, yPos, image));

			serialPlayers.addAll(thisPlayer);
		}
		serialized.addAll(serialPlayers);

		Byte remainingTime = (byte) Math.floor(gs.remainingTime() / 2.);

		serialized.add(remainingTime);

		return serialized;

	}
}
