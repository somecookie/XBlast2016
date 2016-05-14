/**
 * @author Ricardo Ferreira Ribeiro (250798)
 * @author Eleonore Pochon (262959)
 */
package ch.xblast.client;

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

import javax.swing.CellEditor;
import javax.swing.JComponent;

import ch.epfl.xblast.PlayerID;
import ch.xblast.client.GameState.Player;

@SuppressWarnings("serial")
public final class XblastComponent extends JComponent {

	private static GameState gs;
	private static PlayerID id;
	private final static int BLOCK_WIDTH = 64;
	private final static int BLOCK_HEIGHT = 48;
	private final static int SCORE_WIDTH = 48;
	private final static int SCORE_HEIGHT = 48;
	private final static int TIME_WIDTH = 16;

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(960, 688);
	}

	@Override
	protected void paintComponent(Graphics g0) {
		Graphics2D g = (Graphics2D) g0;

		statePaint(g);
		livesPaint(g);
		playerPaint(g);

	}

	private void playerPaint(Graphics2D g) {

		Comparator<Player> c1 = (p1, p2) -> Integer.compare(p1.getPosition().y(), p2.getPosition().y());
		Comparator<Player> c2 = (p1, p2) -> {
			int i = id.ordinal();
			int a = Math.floorMod(i + p1.getId().ordinal(), 4);
			int b = Math.floorMod(i + p2.getId().ordinal(), 4);

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

	private void livesPaint(Graphics2D g) {
		List<Integer> x = Arrays.asList(96, 240, 768, 912);
		int y = 659;

		Font font = new Font("Arial", Font.BOLD, 25);
		g.setColor(Color.WHITE);
		g.setFont(font);

		for (int i = 0; i < x.size(); i++) {
			String lives = String.valueOf(gs.getPlayers().get(i).getLives());
			g.drawString(lives, x.get(i), y);
		}

	}

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

	public void setGameState(GameState newGs, PlayerID newId) {
		gs = newGs;
		id = newId;
		repaint();
	}

}
