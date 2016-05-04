package ch.xblast.client;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;

public final class GameState {

	public final List<Player> players;
	public final List<Image> board;
	public final List<Image> bombsAndExplosions;
	public final List<Image> scores;
	public final List<Image> time;

	/**
	 * Construct the game state for the client (then he would have a
	 * representation in images) in function of the players, the board, the
	 * bombs, the explosions, the score and the time (the only informations he
	 * needs to play)
	 * 
	 * @param players
	 *            list of the player
	 * @param board
	 *            the board
	 * @param bombsAndExplosions
	 *            the bombs and explosions on the board
	 * @param scores
	 *            the score of the game (changing in function of the time)
	 * @param time
	 *            time of the game
	 */
	public GameState(List<Player> players, List<Image> board, List<Image> bombsAndExplosions, List<Image> scores,
			List<Image> time) {
		this.players = Collections.unmodifiableList(new ArrayList<>(players));
		this.board = Collections.unmodifiableList(spiralToRowMajorOrder(new ArrayList<>(board)));
		this.bombsAndExplosions = Collections.unmodifiableList(new ArrayList<>(bombsAndExplosions));
		this.scores = Collections.unmodifiableList(new ArrayList<>(scores));
		this.time = Collections.unmodifiableList(new ArrayList<>(time));
	}

	/**
	 * Change the list's order , from the spiral order to the row major order
	 * 
	 * @param list
	 *            the list we will transform
	 * @return the transformed list
	 */
	private List<Image> spiralToRowMajorOrder(List<Image> list) {

		Image[] rowMajorOrder = new Image[list.size()];

		int i = 0;
		for (Cell cell : Cell.SPIRAL_ORDER) {
			rowMajorOrder[cell.rowMajorIndex()] = list.get(i++);
		}

		return Arrays.asList(rowMajorOrder);
	}

	public final static class Player {
		PlayerID id;
		int lives;
		SubCell position;
		Image image;

		/**
		 * Construct the player for the player (representations in images) in
		 * function of his id, his number of life, his position and teh
		 * corresponding image to the player's state
		 * 
		 * @param id
		 *            id of the player
		 * @param lives
		 *            life's number of the player
		 * @param position
		 *            player's position
		 * @param image
		 *            the corresponding image to the player's state
		 */
		public Player(PlayerID id, int lives, SubCell position, Image image) {
			this.id = id;
			this.lives = lives;
			this.position = position;
			this.image = image;
		}
	}
}
