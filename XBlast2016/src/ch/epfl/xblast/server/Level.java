package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ch.epfl.xblast.server.image.BlockImage;
import ch.epfl.xblast.server.image.BoardPainter;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.server.Board;

public final class Level {

	private final BoardPainter boardPainter;
	private final GameState initialState;
	public final static Level DEFAULT_LEVEL = defaultLevelGenerator();

	/**
	 * Construct the level in function of the game state(state of the game) and
	 * the board painter(the image corresponding to the board state)
	 * 
	 * @param boardPainter
	 *            the image corresponding to the state
	 * @param initialState
	 *            the game state at the ticks 0
	 */
	public Level(BoardPainter boardPainter, GameState initialState) {
		this.initialState = Objects.requireNonNull(initialState);
		this.boardPainter = Objects.requireNonNull(boardPainter);
	}

	/**
	 * Getter for the board painter
	 * 
	 * @return the board painter
	 */
	public BoardPainter boardPainter() {
		return boardPainter;
	}

	/**
	 * Getter for the initial state of the game
	 * 
	 * @return the initial state of the game
	 */
	public GameState initialState() {

		return initialState;
	}
	/**Create the default level of the game
	 * 
	 * @return the level in function of the board painter and the initial game state
	 */
	private static Level defaultLevelGenerator() {
		
		// Create the board painter
		Map<Block, BlockImage> imageMap = new HashMap<>();
		imageMap.put(Block.FREE, BlockImage.IRON_FLOOR);
		imageMap.put(Block.INDESTRUCTIBLE_WALL, BlockImage.DARK_BLOCK);
		imageMap.put(Block.DESTRUCTIBLE_WALL, BlockImage.EXTRA);
		imageMap.put(Block.CRUMBLING_WALL, BlockImage.EXTRA_O);
		imageMap.put(Block.BONUS_BOMB, BlockImage.BONUS_BOMB);
		imageMap.put(Block.BONUS_RANGE, BlockImage.BONUS_RANGE);
		BlockImage sFree = BlockImage.IRON_FLOOR_S;
		BoardPainter painter = new BoardPainter(imageMap, sFree);
		
		//Create the game state (in function of the board and the player's list
		
		//Create the board
		Block __ = Block.FREE;
		Block xx = Block.DESTRUCTIBLE_WALL;
		Block XX = Block.INDESTRUCTIBLE_WALL;
		Board board = Board.ofQuadrantNWBlocksWalled(
				Arrays.asList(Arrays.asList(__, __, __, __, __, xx, __), Arrays.asList(__, XX, xx, XX, xx, XX, xx),
						Arrays.asList(__, xx, __, __, __, xx, __), Arrays.asList(xx, XX, __, XX, XX, XX, XX),
						Arrays.asList(__, xx, __, xx, __, __, __), Arrays.asList(xx, XX, xx, XX, xx, XX, __)));
		
		//Create the player's list
		List<Player> players = new ArrayList<>();
		players.add(new Player(PlayerID.PLAYER_1, 3, new Cell(1, 1), 2, 3));
		players.add(new Player(PlayerID.PLAYER_2, 3, new Cell(13, 1), 2, 3));
		players.add(new Player(PlayerID.PLAYER_3, 3, new Cell(13, 11), 2, 3));
		players.add(new Player(PlayerID.PLAYER_4, 3, new Cell(1, 11), 2, 3));

		GameState gameState = new GameState(board, players);

		return new Level(painter, gameState);
	}

}
