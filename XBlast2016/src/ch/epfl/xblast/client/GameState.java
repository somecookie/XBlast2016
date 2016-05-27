/**
 * @author Ricardo Ferreira Ribeiro (250798)
 * @author Eleonore Pochon (262959)
 */
package ch.epfl.xblast.client;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;

public final class GameState {

	private final List<Player> players;
	private final List<Image> board;
	private final List<Image> bombsAndExplosions;
	private final List<Image> scores;
	private final List<Image> time;

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

		if (players.size() != PlayerID.NB_PLAYERS) {
			throw new IllegalArgumentException("There must be 4 players!");
		} else {

			for (int i = 0; i < players.size(); i++) {
				if (players.get(i).id != PlayerID.values()[i])
					throw new IllegalArgumentException("The players are not well sorted");
			}
		}
		this.board = Collections.unmodifiableList(new ArrayList<>(board));
		this.bombsAndExplosions = Collections.unmodifiableList(new ArrayList<>(bombsAndExplosions));
		this.scores = Collections.unmodifiableList(new ArrayList<>(scores));
		this.time = Collections.unmodifiableList(new ArrayList<>(time));
	}

	/* Getters for the class */
	public List<Player> getPlayers() {
		return players;
	}

	public List<Image> getBoard() {
		return board;
	}

	public List<Image> getBombsAndExplosions() {
		return bombsAndExplosions;
	}

	public List<Image> getScores() {
		return scores;
	}

	public List<Image> getTime() {
		return time;
	}

	public final static class Player {
		private final PlayerID id;
		private final int lives;
		private final SubCell position;
		private final Image image;

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

		/* Getters for the class */
		public int getLives() {
			return lives;
		}

		public PlayerID getId() {
			return id;
		}

		public SubCell getPosition() {
			return position;
		}

		public Image getImage() {
			return image;
		}
	}
}
