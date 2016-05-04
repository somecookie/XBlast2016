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

	public GameState(List<Player> players, List<Image> board, List<Image> bombsAndExplosions, List<Image> scores,
			List<Image> time) {
		this.players = Collections.unmodifiableList(new ArrayList<>(players));
		this.board = Collections.unmodifiableList(spiralToRowMajorOrder(new ArrayList<>(board)));
		this.bombsAndExplosions = Collections.unmodifiableList(new ArrayList<>(bombsAndExplosions));
        this.scores = Collections.unmodifiableList(new ArrayList<>(scores));
        this.time = Collections.unmodifiableList(new ArrayList<>(time));
	}
	
	private List<Image> spiralToRowMajorOrder(List<Image> list){
		 Image[] rowMajorOrder = new Image[list.size()];

	        int i = 0;
	        for (Cell cell : Cell.SPIRAL_ORDER) {
	            rowMajorOrder[cell.rowMajorIndex()] = list.get(i++);
	        }

	        return Arrays.asList(rowMajorOrder);
		
	}
	
	public final static class Player{
		PlayerID id;
		int lives;
		SubCell position;
		Image image;
		
		public Player (PlayerID id, int lives, SubCell position, Image image){
			this.id = id;
			this.lives = lives;
			this.position = position;
			this.image = image;
		}
		
		
		
		
	}
}
