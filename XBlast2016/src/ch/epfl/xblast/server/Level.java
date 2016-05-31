/**
 * @author Ricardo Ferreira Ribeiro (250798)
 * @author Eleonore Pochon (262959)
 */
package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.server.Board;

public final class Level {

	private final BoardPainter boardPainter;
	private final GameState initialState;
	public final static Level DEFAULT_LEVEL = defaultLevelGenerator();
	public final static Level ARENA = Arena();
	public final static Level HUNGER_GAMES = hungerGames();
	public final static Level EMPTY = empty();
	public final static Level DEBUG = debug();
	public final static int NB_LIFES = 3;
	public final static int INITIAL_BOMBS = 2;
	public final static int INITIAL_RANGE = 3;
	public final static int MAX_BONUS = 9;

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

	/**
	 * Create the default level of the game
	 * 
	 * @return the level in function of the board painter and the initial game
	 *         state
	 */
	private static Level defaultLevelGenerator() {

		Map<Block, BlockImage> imageMap = new HashMap<>();
		imageMap.put(Block.FREE, BlockImage.IRON_FLOOR);
		imageMap.put(Block.INDESTRUCTIBLE_WALL, BlockImage.DARK_BLOCK);
		imageMap.put(Block.DESTRUCTIBLE_WALL, BlockImage.EXTRA);
		imageMap.put(Block.CRUMBLING_WALL, BlockImage.EXTRA_O);
		imageMap.put(Block.BONUS_BOMB, BlockImage.BONUS_BOMB);
		imageMap.put(Block.BONUS_RANGE, BlockImage.BONUS_RANGE);
		BlockImage sFree = BlockImage.IRON_FLOOR_S;
		BoardPainter painter = new BoardPainter(imageMap, sFree);

		Block __ = Block.FREE;
		Block xx = Block.DESTRUCTIBLE_WALL;
		Block XX = Block.INDESTRUCTIBLE_WALL;
		Board board = Board.ofQuadrantNWBlocksWalled(
				Arrays.asList(Arrays.asList(__, __, __, __, __, xx, __), Arrays.asList(__, XX, xx, XX, xx, XX, xx),
						Arrays.asList(__, xx, __, __, __, xx, __), Arrays.asList(xx, XX, __, XX, XX, XX, XX),
						Arrays.asList(__, xx, __, xx, __, __, __), Arrays.asList(xx, XX, xx, XX, xx, XX, __)));

		List<Player> players = new ArrayList<>();
		players.add(new Player(PlayerID.PLAYER_1, NB_LIFES, new Cell(1, 1), INITIAL_BOMBS, INITIAL_RANGE));
		players.add(new Player(PlayerID.PLAYER_2, NB_LIFES, new Cell(13, 1), INITIAL_BOMBS, INITIAL_RANGE));
		players.add(new Player(PlayerID.PLAYER_3, NB_LIFES, new Cell(13, 11), INITIAL_BOMBS, INITIAL_RANGE));
		players.add(new Player(PlayerID.PLAYER_4, NB_LIFES, new Cell(1, 11), INITIAL_BOMBS, INITIAL_RANGE));

		GameState gameState = new GameState(board, players);

		return new Level(painter, gameState);
	}

	private static Level hungerGames() {
		Map<Block, BlockImage> imageMap = new HashMap<>();
		imageMap.put(Block.FREE, BlockImage.IRON_FLOOR);
		imageMap.put(Block.INDESTRUCTIBLE_WALL, BlockImage.DARK_BLOCK);
		imageMap.put(Block.DESTRUCTIBLE_WALL, BlockImage.EXTRA);
		imageMap.put(Block.CRUMBLING_WALL, BlockImage.EXTRA_O);
		imageMap.put(Block.BONUS_BOMB, BlockImage.BONUS_BOMB);
		imageMap.put(Block.BONUS_RANGE, BlockImage.BONUS_RANGE);
		BlockImage sFree = BlockImage.IRON_FLOOR_S;
		BoardPainter painter = new BoardPainter(imageMap, sFree);

		Block __ = Block.FREE;
		Block xx = Block.DESTRUCTIBLE_WALL;
		Block XX = Block.INDESTRUCTIBLE_WALL;
		Board board = Board.ofQuadrantNWBlocksWalled(
				Arrays.asList(Arrays.asList(__, __, xx, xx, xx, xx, __), Arrays.asList(__, __, __, __, __, __, __),
						Arrays.asList(xx, __, xx, xx, __, __, xx), Arrays.asList(xx, __, xx, __, __, xx, xx),
						Arrays.asList(xx, __, xx, __, xx, xx, xx), Arrays.asList(xx, __, xx, __, xx, xx, xx)));

		List<Player> players = new ArrayList<>();
		players.add(new Player(PlayerID.PLAYER_1, NB_LIFES, new Cell(1, 1), INITIAL_BOMBS, INITIAL_RANGE));
		players.add(new Player(PlayerID.PLAYER_2, NB_LIFES, new Cell(13, 1), INITIAL_BOMBS, INITIAL_RANGE));
		players.add(new Player(PlayerID.PLAYER_3, NB_LIFES, new Cell(13, 11), INITIAL_BOMBS, INITIAL_RANGE));
		players.add(new Player(PlayerID.PLAYER_4, NB_LIFES, new Cell(1, 11), INITIAL_BOMBS, INITIAL_RANGE));
		GameState gameState = new GameState(board, players);

		return new Level(painter, gameState);
	}

	private static Level Arena() {

		Map<Block, BlockImage> imageMap = new HashMap<>();
		imageMap.put(Block.FREE, BlockImage.IRON_FLOOR);
		imageMap.put(Block.INDESTRUCTIBLE_WALL, BlockImage.DARK_BLOCK);
		imageMap.put(Block.DESTRUCTIBLE_WALL, BlockImage.EXTRA);
		imageMap.put(Block.CRUMBLING_WALL, BlockImage.EXTRA_O);
		imageMap.put(Block.BONUS_BOMB, BlockImage.BONUS_BOMB);
		imageMap.put(Block.BONUS_RANGE, BlockImage.BONUS_RANGE);
		BlockImage sFree = BlockImage.IRON_FLOOR_S;
		BoardPainter painter = new BoardPainter(imageMap, sFree);

		Block __ = Block.FREE;
		Block xx = Block.DESTRUCTIBLE_WALL;
		Board board = Board.ofQuadrantNWBlocksWalled(
				Arrays.asList(Arrays.asList(__, __, __, __, __, __, xx), Arrays.asList(__, __, __, __, __, __, xx),
						Arrays.asList(__, __, __, __, __, __, xx), Arrays.asList(__, __, __, __, __, __, xx),
						Arrays.asList(__, __, __, __, __, __, xx), Arrays.asList(xx, xx, xx, xx, xx, xx, xx)));

		List<Player> players = new ArrayList<>();
		players.add(new Player(PlayerID.PLAYER_1, NB_LIFES, new Cell(1, 1), INITIAL_BOMBS, 5));
		players.add(new Player(PlayerID.PLAYER_2, NB_LIFES, new Cell(13, 1), INITIAL_BOMBS, 5));
		players.add(new Player(PlayerID.PLAYER_3, NB_LIFES, new Cell(13, 11), INITIAL_BOMBS, 5));
		players.add(new Player(PlayerID.PLAYER_4, NB_LIFES, new Cell(1, 11), INITIAL_BOMBS, 5));
		GameState gameState = new GameState(board, players);

		return new Level(painter, gameState);
	}

	private static Level empty() {

		Map<Block, BlockImage> imageMap = new HashMap<>();
		imageMap.put(Block.FREE, BlockImage.IRON_FLOOR);
		imageMap.put(Block.INDESTRUCTIBLE_WALL, BlockImage.DARK_BLOCK);
		imageMap.put(Block.DESTRUCTIBLE_WALL, BlockImage.EXTRA);
		imageMap.put(Block.CRUMBLING_WALL, BlockImage.EXTRA_O);
		imageMap.put(Block.BONUS_BOMB, BlockImage.BONUS_BOMB);
		imageMap.put(Block.BONUS_RANGE, BlockImage.BONUS_RANGE);
		BlockImage sFree = BlockImage.IRON_FLOOR_S;
		BoardPainter painter = new BoardPainter(imageMap, sFree);

		Block __ = Block.FREE;
		Board board = Board.ofQuadrantNWBlocksWalled(
				Arrays.asList(Arrays.asList(__, __, __, __, __, __, __), Arrays.asList(__, __, __, __, __, __, __),
						Arrays.asList(__, __, __, __, __, __, __), Arrays.asList(__, __, __, __, __, __, __),
						Arrays.asList(__, __, __, __, __, __, __), Arrays.asList(__, __, __, __, __, __, __)));

		List<Player> players = new ArrayList<>();
		players.add(new Player(PlayerID.PLAYER_1, NB_LIFES, new Cell(1, 1), 9, 9));
		players.add(new Player(PlayerID.PLAYER_2, NB_LIFES, new Cell(13, 1), 9, 9));
		players.add(new Player(PlayerID.PLAYER_3, NB_LIFES, new Cell(13, 11), 9, 9));
		players.add(new Player(PlayerID.PLAYER_4, NB_LIFES, new Cell(1, 11), 9, 9));
		GameState gameState = new GameState(board, players);

		return new Level(painter, gameState);
	}

	private static Level debug() {

		Map<Block, BlockImage> imageMap = new HashMap<>();
		imageMap.put(Block.FREE, BlockImage.IRON_FLOOR);
		imageMap.put(Block.INDESTRUCTIBLE_WALL, BlockImage.DARK_BLOCK);
		imageMap.put(Block.DESTRUCTIBLE_WALL, BlockImage.EXTRA);
		imageMap.put(Block.CRUMBLING_WALL, BlockImage.EXTRA_O);
		imageMap.put(Block.BONUS_BOMB, BlockImage.BONUS_BOMB);
		imageMap.put(Block.BONUS_RANGE, BlockImage.BONUS_RANGE);
		BlockImage sFree = BlockImage.IRON_FLOOR_S;
		BoardPainter painter = new BoardPainter(imageMap, sFree);

		Block __ = Block.FREE;
		Block BR = Block.BONUS_RANGE;
		Block xx = Block.DESTRUCTIBLE_WALL;
		Board board = Board.ofQuadrantNWBlocksWalled(
				Arrays.asList(Arrays.asList(__, __, __, __, __, __, __), Arrays.asList(__, __, __, __, __, __, __),
						Arrays.asList(__, __, __, __, __, __, __), Arrays.asList(__, __, __, __, __, __, __),
						Arrays.asList(__, __, __, __, __, __, __), Arrays.asList(__, __, __, __, __, __, xx)));

		List<Player> players = new ArrayList<>();
		players.add(new Player(PlayerID.PLAYER_1, NB_LIFES, new Cell(1, 1), 9, 9));
		players.add(new Player(PlayerID.PLAYER_2, NB_LIFES, new Cell(13, 1), 9, 9));
		players.add(new Player(PlayerID.PLAYER_3, NB_LIFES, new Cell(13, 11), 9, 9));
		players.add(new Player(PlayerID.PLAYER_4, NB_LIFES, new Cell(1, 11), 9, 9));
		GameState gameState = new GameState(board, players);

		return new Level(painter, gameState);
	}

}
