/**
 * @author Ricardo Ferreira Ribeiro (250798)
 * @author Clarisse Estelle Fleurimont (246866)
 * @date 14 mars 2016
 */
package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.ArgumentChecker;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.Lists;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;
import ch.epfl.xblast.server.Player.DirectedPosition;
import ch.epfl.xblast.server.Player.LifeState;
import ch.epfl.xblast.server.Player.LifeState.State;

public final class GameState {

	private final int ticks;
	private final Board board;
	private final List<Player> players;
	private final List<Bomb> bombs;
	private final List<Sq<Sq<Cell>>> explosions;
	private final List<Sq<Cell>> blasts;
	private final int NBR_PLAYER = 4;
	private final static int SEED = 2016;
	private final static List<List<PlayerID>> permut = Collections
			.unmodifiableList(Lists.permutations(Arrays.asList(PlayerID.values())));
	private static final Random RANDOM = new Random(SEED);

	/**
	 * Construct the game state in function of the ticks , the board, the
	 * players, the bombs, the explosions and their blasts
	 * 
	 * @param ticks
	 *            actual ticks
	 * @param board
	 *            actual board
	 * @param players
	 *            Player (alive or dead --> always a list of four elements)
	 * @param bombs
	 *            bombs dropped on the board
	 * @param explosions
	 *            actual explosions
	 * @param blasts
	 *            blasts on the board
	 * @throws IllegalArgumentException
	 *             if the ticks is negative or if the player's list doesn't
	 *             contain 4 elements
	 * @throws NullPointerException
	 *             if one of the five arguments is null
	 */
	public GameState(int ticks, Board board, List<Player> players, List<Bomb> bombs, List<Sq<Sq<Cell>>> explosions,
			List<Sq<Cell>> blasts) {

		this.ticks = ArgumentChecker.requireNonNegative(ticks);

		if (players.size() != NBR_PLAYER) {
			throw new IllegalArgumentException("There must be 4 players!");
		} else {

			for (int i = 0; i < players.size(); i++) {
				if (players.get(i).id() != PlayerID.values()[i])
					throw new IllegalArgumentException("The players are not well sorted");
			}
		}

		this.players = Collections.unmodifiableList(new ArrayList<>(Objects.requireNonNull(players)));
		this.board = Objects.requireNonNull(board);
		this.bombs = Collections.unmodifiableList(new ArrayList<>(Objects.requireNonNull(bombs)));
		this.explosions = Collections.unmodifiableList(new ArrayList<>(Objects.requireNonNull(explosions)));
		this.blasts = Collections.unmodifiableList(new ArrayList<>(Objects.requireNonNull(blasts)));
	}

	/**
	 * Construct the game state at tick's 0 (no bombs, no explosions, no blasts)
	 * 
	 * @param board
	 *            the given board
	 * @param players
	 *            the given player's list
	 * @throws same
	 *             exception as the first constructor
	 */
	public GameState(Board board, List<Player> players) {
		this(0, board, players, new ArrayList<Bomb>(), new ArrayList<Sq<Sq<Cell>>>(), new ArrayList<Sq<Cell>>());
	}

	/**
	 * Return the tick's corresponding to the state
	 * 
	 * @return ticks (int)
	 */
	public int ticks() {
		return ticks;
	}

	/**
	 * Check if the game is over or not, if Ticks.TOTAL_TICKS is null or if
	 * there is no more alive players
	 * 
	 * @return true if the game is over, otherwise false
	 */
	public boolean isGameOver() {
		return (Ticks.TOTAL_TICKS + 1 == ticks || alivePlayers().size() <= 1);
	}

	/**
	 * Return the remaining time in seconds before the end of the game
	 * 
	 * @return remainingTime (double)
	 */
	public double remainingTime() {
		return ((double) Ticks.TOTAL_TICKS - ticks) / Ticks.TICKS_PER_SECOND;
	}

	/**
	 * Return the winner's id if there is one, if it's not the case return the
	 * empty optionnal value
	 * 
	 * @return winner (Optional<PlayerID>)
	 */
	public Optional<PlayerID> winner() {
		if (alivePlayers().size() == 1) {
			return Optional.of(alivePlayers().get(0).id());
		}

		return Optional.empty();
	}

	/**
	 * Return the board
	 * 
	 * @return board (Board)
	 */
	public Board board() {
		return board;
	}

	/**
	 * Return the player as a list with always 4 elements
	 * 
	 * @return players (List<Player>)
	 */
	public List<Player> players() {
		return players;
	}

	/**
	 * Return the alive player's list (they have at least on life)
	 * 
	 * @return alivePlayers (List<Player>)
	 */
	public List<Player> alivePlayers() {
		List<Player> alivePlayers = new ArrayList<>();
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).isAlive()) {
				alivePlayers.add(players.get(i));
			}
		}
		return Collections.unmodifiableList(new ArrayList<>(alivePlayers));
	}

	/**
	 * Calculate the blasts for the next state of the game
	 * 
	 * @param blasts0
	 *            actual blast
	 * @param board0
	 *            actual board
	 * @param explosions0
	 *            actual explosions
	 * @return nextBlasts (List<Sq<Cell>>) list of the actual blasts
	 */
	private static List<Sq<Cell>> nextBlasts(List<Sq<Cell>> blasts0, Board board0, List<Sq<Sq<Cell>>> explosions0) {

		List<Sq<Cell>> blasts1 = new LinkedList<>();
		for (Sq<Cell> bla : blasts0) {
			if (!bla.tail().isEmpty() && board0.blockAt(bla.head()).isFree()) {
				blasts1.add(bla.tail());
			}
		}

		for (Sq<Sq<Cell>> exp : explosions0) {
			if (!exp.isEmpty()) {
				blasts1.add(exp.head());
			}
		}
		return blasts1;
	}

	private static Map<Cell, Bomb> bombedCells(List<Bomb> bombs) {
		Map<Cell, Bomb> bombedCells = new HashMap<>();
		for (Bomb b : bombs) {
			bombedCells.put(b.position(), b);
		}
		return bombedCells;
	}

	/**
	 * Return the map containing the cells containing a bomb and the
	 * corresponding bombs
	 * 
	 * @return bombedCells (Map<Cell, Bomb>)
	 */
	public Map<Cell, Bomb> bombedCells() {
		return Collections.unmodifiableMap(new HashMap<>(bombedCells(bombs)));
	}

	private static Set<Cell> blastedCells(List<Sq<Cell>> blasts) {
		Set<Cell> blastedCells = new HashSet<>();
		for (Sq<Cell> cell : blasts) {
			if (!cell.isEmpty()) {
				blastedCells.add(cell.head());
			}
		}
		return blastedCells;
	}

	/**
	 * Return the set containing the cell where there is at least one blast
	 * 
	 * @return blastedCells (Set<Cell>)
	 */
	public Set<Cell> blastedCells() {
		return Collections.unmodifiableSet(new HashSet<>(blastedCells(blasts)));
	}

	private static List<Cell> bonus(Board board) {
		List<Cell> bonus = new ArrayList<>();
		for (int i = 0; i < Board.ROWS; i++) {
			for (int j = 0; j < Board.COLUMNS; j++) {
				Cell c = new Cell(j, i);
				if (board.blockAt(c).isBonus()) {
					bonus.add(c);
				}
			}
		}
		return bonus;
	}

	/**
	 * Return a set containing the bonus consumed by the players
	 * 
	 * @param board0
	 * @param blastedCells1
	 * @param players
	 * @return consumedBonuses (Set<Cell>)
	 */
	private static Set<Cell> consumedBonuses(List<Cell> bonus, List<Player> players) {

		Set<Cell> consumedBonuses = new HashSet<>();
		for (Player p : players) {
			for (Cell b : bonus) {
				if (p.position().equals(SubCell.centralSubCellOf(b))) {

					consumedBonuses.add(b);
				}
			}
		}
		return consumedBonuses;

	}

	private static List<Player> sortedPlayers(List<PlayerID> currentPermut, List<Player> player) {
		List<Player> prioList = new ArrayList<>();
		for (PlayerID id : currentPermut) {
			for (Player p : player) {
				if (id.equals(p.id())) {
					prioList.add(p);
				}
			}
		}
		return prioList;
	}

	/**
	 * Return the game state at the next ticks in function of the actual game
	 * state and the given events (speedChangeEvents and bombDropEvents)
	 * 
	 * @param speedChangeEvents
	 *            direction changes for players
	 * 
	 * @param bombDropEvents
	 *            player's list who dropped bombs
	 * @return next (GameState)
	 */
	public GameState next(Map<PlayerID, Optional<Direction>> speedChangeEvents, Set<PlayerID> bombDropEvents) {

		List<PlayerID> currentPermut = permut.get(ticks % permut.size());
		List<Player> sortedPlayers = sortedPlayers(currentPermut, players());

		List<Sq<Cell>> blasts1 = nextBlasts(blasts, board, explosions);

		List<Cell> bonus = bonus(board);
		Set<Cell> consumedBonuses = consumedBonuses(bonus, players);
		Set<Cell> consumedBonusesBis = new HashSet<>(consumedBonuses);
		Map<PlayerID, Bonus> playerBonuses = new HashMap<>();

		for (Player p : sortedPlayers) {
			if (consumedBonusesBis.contains(p.position().containingCell())) {
				Bonus b = board.blockAt(p.position().containingCell()).associatedBonus();
				playerBonuses.put(p.id(), b);
				consumedBonusesBis.remove(p.position().containingCell());
			}
		}

		Board board1 = nextBoard(board, consumedBonuses, blastedCells(blasts1));

		List<Sq<Sq<Cell>>> explosions1 = nextExplosions(explosions);

		List<Bomb> bombs1 = new ArrayList<>();
		List<Bomb> tmpBomb = new ArrayList<>(bombs);

		tmpBomb.addAll(newlyDroppedBombs(sortedPlayers, bombDropEvents, bombs));

		for (Bomb b : tmpBomb) {
			if (b.fuseLengths().tail().isEmpty()) {
				explosions1.addAll(b.explosion());
			} else {
				bombs1.add(new Bomb(b.ownerId(), b.position(), b.fuseLength() - 1, b.range()));
			}

		}

		Iterator<Bomb> b = bombs1.iterator();
		while (b.hasNext()) {
			Bomb bomb = b.next();
			if (blastedCells(blasts1).contains(bomb.position())) {
				explosions1.addAll(bomb.explosion());
				b.remove();
			}
		}

		List<Player> players1 = nextPlayers(players(), playerBonuses, bombedCells(bombs1).keySet(), board1,
				blastedCells(blasts1), speedChangeEvents);

		return new GameState(ticks() + 1, board1, players1, bombs1, explosions1, blasts1);
	}

	/**
	 * Calculate the next board in function of the actual board, the consumed
	 * bonus and the new given blasted cells
	 * 
	 * @param board0
	 *            actual board
	 * @param consumedBonuses
	 *            bonus consumed by the player
	 * @param blastedCells1
	 *            new blasted cells
	 * @return board1 (Board) next board
	 */
	private static Board nextBoard(Board board0, Set<Cell> consumedBonuses, Set<Cell> blastedCells1) {
		List<Sq<Block>> board1 = new ArrayList<>();
		List<Cell> rowMajor = Cell.ROW_MAJOR_ORDER;

		for (Cell c : rowMajor) {
			if (consumedBonuses.contains(c)) {
				board1.add(Sq.constant(Block.FREE));
			} else if (blastedCells1.contains(c)) {
				if (board0.blockAt(c).equals(Block.DESTRUCTIBLE_WALL)) {
					int random = RANDOM.nextInt(3);
					Sq<Block> newWall = Sq.repeat(Ticks.WALL_CRUMBLING_TICKS, Block.CRUMBLING_WALL);

					switch (random) {
					case 0:
						newWall = newWall.concat(Sq.constant(Block.BONUS_BOMB));
						break;
					case 1:
						newWall = newWall.concat(Sq.constant(Block.BONUS_RANGE));
						break;
					case 2:
						newWall = newWall.concat(Sq.constant(Block.FREE));
						break;
					default:
						throw new IllegalArgumentException(
								"The random number must be between 0 and 2, but was " + random);
					}
					board1.add(newWall);
				} else if (board0.blockAt(c).isBonus()) {
					Sq<Block> newBonus;
					newBonus = board0.blocksAt(c).tail().limit(Ticks.BONUS_DISAPPEARING_TICKS);
					newBonus = newBonus.concat(Sq.constant(Block.FREE));
					board1.add(newBonus);
				} else {
					board1.add(board0.blocksAt(c).tail());
				}
			} else {
				board1.add(board0.blocksAt(c).tail());
			}
		}
		return new Board(board1);

	}

	/**
	 * 
	 * @param players0
	 * @param playerBonuses
	 * @param map
	 * @param board1
	 * @param blastedCells1
	 * @param speedChangeEvents
	 * @return
	 */
	private static List<Player> nextPlayers(List<Player> players0, Map<PlayerID, Bonus> playerBonuses,
			Set<Cell> bombedCells1, Board board1, Set<Cell> blastedCells1,
			Map<PlayerID, Optional<Direction>> speedChangeEvents) {

		List<Player> players1 = new ArrayList<>();

		for (Player p : players0) {

			Sq<DirectedPosition> directedPos1 = p.directedPositions();

			if (speedChangeEvents.containsKey(p.id())) {
				Optional<Direction> dir = speedChangeEvents.get(p.id());
				directedPos1 = nextDirectedPos(p, dir);
			}

			if (!blocked(p, directedPos1, board1, bombedCells1)) {
				directedPos1 = directedPos1.tail();
			}

			Sq<LifeState> lifeStates = p.lifeStates();
			if (blastedCells1.contains(directedPos1.head().position().containingCell())
					&& p.lifeState().state().equals(State.VULNERABLE)) {

				lifeStates = p.statesForNextLife();
			} else {
				lifeStates = lifeStates.tail();
			}

			if (playerBonuses.containsKey(p.id())) {
				Set<Entry<PlayerID, Bonus>> bonus = playerBonuses.entrySet();
				for (Entry<PlayerID, Bonus> b : bonus) {

					if (b.getKey().equals(p.id())) {

						p = b.getValue().applyTo(p);

					}
				}
			}
			players1.add(new Player(p.id(), lifeStates, directedPos1, p.maxBombs(), p.bombRange()));
		}

		return players1;

	}

	/**
	 * Calculate the next sequence of directed positions
	 * 
	 * @param p
	 *            the given player
	 * @param dir
	 *            the given direction
	 * @return nextDirectedPos (Sq<DirectedPosition>)
	 */
	private static Sq<DirectedPosition> nextDirectedPos(Player p, Optional<Direction> dir) {
		Sq<DirectedPosition> directedPos1;

		if (!dir.isPresent()) {
			if (p.position().isCentral()) {
				directedPos1 = DirectedPosition.stopped(p.directedPositions().head());
			} else {

				Sq<DirectedPosition> toCentral = p.directedPositions().takeWhile(c -> !c.position().isCentral());
				DirectedPosition central = p.directedPositions().findFirst(c -> c.position().isCentral());
				Sq<DirectedPosition> fromCentral = DirectedPosition
						.stopped(new DirectedPosition(central.position(), central.direction()));

				directedPos1 = toCentral.concat(fromCentral);

			}

		} else if (dir.get().isParallelTo(p.direction())) {
			directedPos1 = DirectedPosition.moving(new DirectedPosition(p.position(), dir.get()));
		} else {

			Sq<DirectedPosition> toCentral = p.directedPositions().takeWhile(c -> !c.position().isCentral());
			SubCell central = p.directedPositions().findFirst(c -> c.position().isCentral()).position();
			Sq<DirectedPosition> fromCentral = DirectedPosition.moving(new DirectedPosition(central, dir.get()));

			directedPos1 = toCentral.concat(fromCentral);
		}

		return directedPos1;

	}

	/**
	 * Check if the player is blocked or if he can move
	 * 
	 * @param p
	 *            the given player
	 * @param directedPos
	 *            the given directed position
	 * @param board1
	 *            the board
	 * @param bombs
	 *            the set of bombs
	 * @return true if the player is blocked, otherwise false
	 */
	private static boolean blocked(Player p, Sq<DirectedPosition> directedPos, Board board1, Set<Cell> bombs) {
		Direction dir = directedPos.head().direction();
		SubCell pos = directedPos.head().position();

		if (!p.lifeState().canMove()) {
			return true;
		}

		if (bombs.contains(pos.containingCell()) && pos.distanceToCentral() == 6) {
			SubCell nextSub = pos.neighbor(dir);
			if (pos.distanceToCentral() > nextSub.distanceToCentral()) {
				return true;
			}
		}

		if (p.position().isCentral()) {

			Cell c = pos.containingCell().neighbor(dir);
			if (!board1.blockAt(c).canHostPlayer()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Calculate the next state of the explosions
	 * 
	 * @param explosions0
	 * @return explosions1 (the next explosions)
	 */
	private static List<Sq<Sq<Cell>>> nextExplosions(List<Sq<Sq<Cell>>> explosions0) {
		List<Sq<Sq<Cell>>> explosions1 = new ArrayList<>();

		for (Sq<Sq<Cell>> e : explosions0) {
			if (!e.isEmpty()) {

				explosions1.add(e.tail());
			}
		}

		return explosions1;
	}

	/**
	 * Return the new bomb's list posed by the players, in function of the
	 * actual players, the newly dropped bomb event and the given actuals bombs
	 * 
	 * @param players0
	 * @param bombDropEvents
	 * @param bombs0
	 * @return newlyDroppedBombs (List<Bomb>)
	 */
	private static List<Bomb> newlyDroppedBombs(List<Player> players0, Set<PlayerID> bombDropEvents,
			List<Bomb> bombs0) {
		List<Bomb> newBombs = new ArrayList<>();

		for (Player p : players0) {
			if (p.isAlive() && bombDropEvents.contains(p.id())) {
				boolean taken = false;
				int nbrBombs = 0;

				for (Bomb b : bombs0) {
					if (b.ownerId().equals(p.id())) {
						nbrBombs++;
					}
					if (b.position().equals(p.position().containingCell())) {
						taken = true;
					}
				}
				if (nbrBombs < p.maxBombs() && !taken) {
					newBombs.add(p.newBomb());
				}
			}
		}

		return newBombs;
	}
}
