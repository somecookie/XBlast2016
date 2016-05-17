/**
 * @author Ricardo Ferreira Ribeiro (250798)
 * @author Eleonore Pochon (262959)
 */
package ch.xblast.client;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.RunLengthEncoder;
import ch.epfl.xblast.SubCell;
import ch.xblast.client.GameState.Player;

public final class GameStateDeserializer {

	private static int TOTAL_TIME = 60;
	private static ImageCollection playerImageCollection = new ImageCollection("player");
	private static ImageCollection blockImageCollection = new ImageCollection("block");
	private static ImageCollection explosionImageCollection = new ImageCollection("explosion");
	private static ImageCollection scoreImageCollection = new ImageCollection("score");
	
	private GameStateDeserializer(){}

	public static GameState deserializerGameState (List<Byte> serializedState){
		
		//some indexes
		int size = serializedState.size();
		int compressedBoardSize = Byte.toUnsignedInt(serializedState.get(0));
		int compressedExplosionSize = Byte.toUnsignedInt(serializedState.get(compressedBoardSize+1));
		int endBoard = compressedBoardSize;
		int startExplosions = compressedBoardSize+2;
		int endExplosions = startExplosions+compressedExplosionSize-1;
		int startPlayers = endExplosions+1;
		int endPlayers = size-2;
		
		List<Byte> serializedBoard = serializedState.subList(1, endBoard+1);
		List<Byte> serializedExplosions = serializedState.subList(startExplosions, endExplosions+1);
		List<Byte> serializedPlayer = serializedState.subList(startPlayers, endPlayers+1);
		int remainingTime = Byte.toUnsignedInt(serializedState.get(size-1));
		
		List<Image> board = deserializerBoard(serializedBoard);
		List<Image> bombsAndExplosions = deserializerExplosions(serializedExplosions);
		List<Player> players = deserializerPlayer(serializedPlayer);
		List<Image> scores = deserializerScore(players);
		List<Image> time = deserializerTime(remainingTime);
		return new GameState(players, board, bombsAndExplosions, scores, time);
		
	}
	
	/**
	 * Change the list's order , from the spiral order to the row major order
	 * 
	 * @param list
	 *            the list we will transform
	 * @return the transformed list
	 */
	private static List<Byte> spiralToRowMajorOrder(List<Byte> list) {

		Byte[] rowMajorOrder = new Byte[list.size()];

		int i = 0;
		for (Cell cell : Cell.SPIRAL_ORDER) {
			rowMajorOrder[cell.rowMajorIndex()] = list.get(i++);
		}

		return Arrays.asList(rowMajorOrder);
	}
	
	private static List<Image> deserializerBoard(List<Byte> serializedBoard){
		List<Byte> desB = RunLengthEncoder.decode(serializedBoard);
		desB = spiralToRowMajorOrder(desB);
		
		List<Image> deserializedBoard = new ArrayList<>();
		for(Byte b : desB){
			deserializedBoard.add(blockImageCollection.imageOrNull(b));
		}
		
		return deserializedBoard;
	}
	
	private static List<Image> deserializerExplosions(List<Byte> serializedExplosions){
		List<Image> deserializedExplosions = new ArrayList<>();
		List<Byte> desE = RunLengthEncoder.decode(serializedExplosions);
		
		for(Byte b: desE){
			deserializedExplosions.add(explosionImageCollection.imageOrNull(b));
		}
		return deserializedExplosions;
	}
	
	private static List<Player> deserializerPlayer(List<Byte> serializedPlayer){
		PlayerID[] players = PlayerID.values();
		int nbrPlayer = PlayerID.values().length;
		List<Player> deserializedPlayer = new ArrayList<>();

		for (int i = 0; i < nbrPlayer; i++) {
			PlayerID id = players[i];
			int lives = Byte.toUnsignedInt(serializedPlayer.get(i*nbrPlayer));
			int x = Byte.toUnsignedInt(serializedPlayer.get(i*nbrPlayer+1));
			int y = Byte.toUnsignedInt(serializedPlayer.get(i*nbrPlayer+2));
			Image image = playerImageCollection.imageOrNull(Byte.toUnsignedInt(serializedPlayer.get(i*nbrPlayer+3)));
			deserializedPlayer.add(new Player(id, lives, new SubCell(x,y), image));
		}
		return deserializedPlayer;
	}
	
	private static List<Image> deserializerScore(List<Player> players){
		List<Image> desS = new ArrayList<>();
		
		Image playerFace;
		Image textMiddle = scoreImageCollection.image(10);
		Image textRight = scoreImageCollection.image(11);
		Image tileVoid = scoreImageCollection.image(12);
		int lives;
		
		for (int i = 0; i < players.size(); i++) {
			lives = players.get(i).getLives();
			playerFace = (lives > 0)? scoreImageCollection.image(i*2) : scoreImageCollection.image((i*2)+1);
			desS.addAll(Arrays.asList(playerFace, textMiddle, textRight ));
			
			if(i == 1){
				desS.addAll(Collections.nCopies(8, tileVoid));
			}
		}
		
		return desS;
		
	}
	
	private static List<Image> deserializerTime(int remainingTime){
		
		Image ledOff = scoreImageCollection.image(20);
		Image ledOn = scoreImageCollection.image(21);
		int passedTime = TOTAL_TIME - remainingTime;
		List<Image> time = new ArrayList<>(Collections.nCopies(remainingTime, ledOn));
		List<Image> pTime = Collections.nCopies(passedTime, ledOff);
		time.addAll(pTime);
		
		return time;
	}
	

}
