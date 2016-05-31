/**
 * @author Ricardo Ferreira Ribeiro (250798)
 * @author Eleonore Pochon (262959)
 */
package ch.epfl.xblast.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import ch.epfl.xblast.Cell;

import javax.swing.JComponent;

import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.client.GameState.Player;

@SuppressWarnings("serial")
public final class XblastComponent extends JComponent {

	private static GameState gs;
	private static PlayerID id;
	private final static int BLOCK_WIDTH = 64;
	private final static int BLOCK_HEIGHT = 48;
	private final static int SCORE_WIDTH = 48;
	private final static int SCORE_HEIGHT = 48;
	private final static int TIME_WIDTH = 16;
	private final static int PS_WIDTH = 960;
	private final static int PS_HEIGHT = 688;
	private final static List<Integer> COORDINATES_LIFE_WIDTH = Arrays.asList(96, 240, 768, 912);
	private final static int COORDINATES_LIFE_HEIGHT = 659;

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(PS_WIDTH, PS_HEIGHT);
	}

	@Override
	protected void paintComponent(Graphics g0) {
		Graphics2D g = (Graphics2D) g0;
		if (gs == null) {
			g.setFont(new Font("Arial", Font.BOLD, 25));
			g.drawString("Attempting to connect.", this.getWidth() / 2, this.getHeight() / 2);
		} else {
			statePaint(g);
			livesPaint(g);
			playerPaint(g);
		}

	}

	/**
	 * Construct the graph's interface for the players
	 * 
	 * @param g
	 */
	private void playerPaint(Graphics2D g) {

		Comparator<Player> c1 = (p1, p2) -> Integer.compare(p1.getPosition().y(), p2.getPosition().y());
		Comparator<Player> c2 = (p1, p2) -> {
			int i = id.ordinal();
			int a = Math.floorMod(i + 1 + p1.getId().ordinal() + 1, PlayerID.NB_PLAYERS);
			int b = Math.floorMod(i + 1 + p2.getId().ordinal() + 1, PlayerID.NB_PLAYERS);

			return Integer.compare(a, b);

		};

		List<Player> l = new ArrayList<>(gs.getPlayers());
		l.sort(c1.thenComparing(c2));

		for (Player p : l) {
			int xs = 4 * p.getPosition().x() - 24;
			int ys = 3 * p.getPosition().y() - 52;
			g.drawImage(p.getImage(), xs, ys, null);

		}

	}

	/**
	 * Construct the graph's interface for the lives
	 * 
	 * @param g
	 */
	private void livesPaint(Graphics2D g) {
		List<Integer> x = COORDINATES_LIFE_WIDTH;
		int y = COORDINATES_LIFE_HEIGHT;

		Font font = new Font("Arial", Font.BOLD, 25);
		g.setColor(Color.WHITE);
		g.setFont(font);

		for (int i = 0; i < x.size(); i++) {
			String lives = String.valueOf(gs.getPlayers().get(i).getLives());
			g.drawString(lives, x.get(i), y);
		}

	}

	/**
	 * Construct the graph's interface for the state
	 * 
	 * @param g
	 */
	private static void statePaint(Graphics2D g) {
		int x = 0;
		int y = 0;
		int col = 0;
		int row = 0;

		Iterator<Image> board = gs.getBoard().iterator();
		Iterator<Image> bomb = gs.getBombsAndExplosions().iterator();

		while (board.hasNext() && bomb.hasNext()) {
			if (col == Cell.COLUMNS) {
				col = 0;
				row++;
			}

			x = col * BLOCK_WIDTH;
			y = row * BLOCK_HEIGHT;
			Image bo = board.next();
			Image bmb = bomb.next();

			g.drawImage(bo, x, y, null);
			g.drawImage(bmb, x, y, null);

			col++;
		}

		Iterator<Image> score = gs.getScores().iterator();
		col = 0;
		y += BLOCK_HEIGHT;

		while (score.hasNext()) {
			Image sc = score.next();
			x = col * SCORE_WIDTH;
			g.drawImage(sc, x, y, null);
			col++;
		}

		Iterator<Image> time = gs.getTime().iterator();
		col = 0;
		y += SCORE_HEIGHT;

		while (time.hasNext()) {
			Image t = time.next();
			x = col * TIME_WIDTH;
			g.drawImage(t, x, y, null);
			col++;
		}
	}

	/**
	 * Permit to change the game state printed by the component (that is
	 * initially non-existent)
	 * 
	 * @param newGs
	 * @param newId
	 */
	public void setGameState(GameState newGs, PlayerID newId) {
		gs = newGs;
		id = newId;
		repaint();
	}

}
