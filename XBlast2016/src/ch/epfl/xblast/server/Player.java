/**
 * @author Ricardo Ferreira Ribeiro (250798)
 * @author Clarisse Estelle Fleurimont (246866)
 */
package ch.epfl.xblast.server;

import java.util.Objects;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.ArgumentChecker;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;
import ch.epfl.xblast.server.Player.LifeState.State;

public final class Player {

	private final PlayerID id;
	private final Sq<LifeState> lifeStates;
	private final Sq<DirectedPosition> directedPos;
	private final int maxBombs;
	private final int bombRange;

	/**
	 * Construct the player with his id, his life state, his position, his
	 * "max bombs" and their range
	 * 
	 * @param id
	 * @param lifeStates
	 * @param directedPos
	 * @param maxBombs
	 * @param bombRange
	 * @throws NullPointerException
	 *             if one of the three arguments is null
	 * @throws IllegalArgumentException
	 *             if one of the two last element is negative
	 */
	public Player(PlayerID id, Sq<LifeState> lifeStates, Sq<DirectedPosition> directedPos, int maxBombs,
			int bombRange) {
		this.id = Objects.requireNonNull(id);
		this.lifeStates = Objects.requireNonNull(lifeStates);
		this.directedPos = Objects.requireNonNull(directedPos);
		this.maxBombs = ArgumentChecker.requireNonNegative(maxBombs);
		this.bombRange = ArgumentChecker.requireNonNegative(bombRange);
	}

	/**
	 * Constructor take in arguments the first constructor
	 * 
	 * @param id
	 * @param lives
	 * @param position
	 * @param maxBombs
	 * @param bombRange
	 */
	public Player(PlayerID id, int lives, Cell position, int maxBombs, int bombRange) {
		this(id, SqCreator(ArgumentChecker.requireNonNegative(lives)),
				DirectedPosition.stopped(new DirectedPosition(SubCell.centralSubCellOf(position), Direction.S)),
				maxBombs, bombRange);
	}

	/**
	 * @return id
	 */
	public PlayerID id() {
		return id;
	}

	/**
	 * Return the sequence of width (life's number, states) of the players
	 * 
	 * @return Sq<LifeState>
	 */
	public Sq<LifeState> lifeStates() {
		return lifeStates;
	}

	/**
	 * Return the width life's number/states of the actual player
	 * 
	 * @return LifeState
	 */
	public LifeState lifeState() {
		return lifeStates.head();
	}

	/**
	 * Return the state's sequence for the player's next life (that begins with
	 * a period of Ticks.PLAYER_DYING_TICKS long during the player is dying,
	 * following with the death of the player or woth the invulnerable's state
	 * followes by a permanent vulnerable state with a number of life equals at
	 * life-1
	 * 
	 * @return Sq<LifeStates>
	 */
	public Sq<LifeState> statesForNextLife() {
		Sq<LifeState> nextState = Sq.repeat(Ticks.PLAYER_DYING_TICKS, new LifeState(lives(), State.DYING));
		if (!isAlive()) {
			return nextState.concat(SqCreator(lives()));
		} else {
			return nextState.concat(SqCreator(lives() - 1));
		}
	}

	/**
	 * Return the actual life's number of the player
	 * 
	 * @return lives
	 */
	public int lives() {
		return lifeStates.head().lives();
	}

	/**
	 * Check if the player is alive or not
	 * 
	 * @return true if the player is alive, otherwise false
	 */
	public boolean isAlive() {
		return (lives() > 0);
	}

	/**
	 * Return the sequence of the directed positions of the player
	 * 
	 * @return Sq<DirectedPosition>
	 */
	public Sq<DirectedPosition> directedPositions() {
		return directedPos;
	}

	/**
	 * Return the player's actual position
	 * 
	 * @return SubCell
	 */
	public SubCell position() {
		return directedPos.head().position();
	}

	/**
	 * Return the direction where the player is looking for
	 * 
	 * @return direction
	 */
	public Direction direction() {
		return directedPos.head().direction;
	}

	/**
	 * Return the maximum's bomb of the player
	 * 
	 * @return maxBombs (int)
	 */
	public int maxBombs() {
		return maxBombs;
	}

	/**
	 * Return the player whose applying the bonus max bomb
	 * 
	 * @param newMaxBombs
	 * @return Player
	 */
	public Player withMaxBombs(int newMaxBombs) {
		return new Player(id(), lifeStates(), directedPositions(), newMaxBombs, bombRange());
	}

	/**
	 * Return the range (in numbers of cell) of the bombs
	 * 
	 * @return bombRange (int)
	 */
	public int bombRange() {
		return bombRange;
	}

	/**
	 * Return the player whose applying the bonus bomb range
	 * 
	 * @param newBombRange
	 * @return Player
	 */
	public Player withBombRange(int newBombRange) {
		return new Player(id(), lifeStates(), directedPositions(), maxBombs(), newBombRange);
	}

	/**
	 * Return a new bomb in function of her player (= owner id)
	 * 
	 * @return the given bomb
	 */
	public Bomb newBomb() {
		return new Bomb(id(), position().containingCell(), Ticks.BOMB_FUSE_TICKS, bombRange());
	}

	/**
	 * Create a life state's sequence
	 * @param lives
	 * @return the sequence for life states
	 */
	private static Sq<LifeState> SqCreator(int lives) {
		if (lives == 0) {
			return Sq.constant(new LifeState(lives, State.DEAD));
		} else {
			Sq<LifeState> invuln = Sq.repeat(Ticks.PLAYER_INVULNERABLE_TICKS, new LifeState(lives, State.INVULNERABLE));
			LifeState vulState = new LifeState(lives, State.VULNERABLE);
			Sq<LifeState> vuln = Sq.constant(vulState);
			return invuln.concat(vuln);
		}

	}

	public static final class LifeState {
		private final int lives;
		private final State state;

		/**
		 * Construct the width life's number/state with the given values
		 * 
		 * @param lives
		 * @param state
		 * @throws IllegalArgumentException
		 *             if the number of life is negative
		 * @throws NullPointerException
		 *             if the state is null
		 */
		public LifeState(int lives, State state) {
			this.lives = ArgumentChecker.requireNonNegative(lives);
			this.state = Objects.requireNonNull(state);
		}

		/**
		 * Return the life's number of the width
		 * 
		 * @return lives
		 */
		public int lives() {
			return lives;
		}

		/**
		 * Return the state
		 * 
		 * @return state
		 */
		public State state() {
			return state;
		}

		/**
		 * Return true iff the state permit the player to move, equals to the
		 * fact that the player can move if he's in the vulnerable or
		 * invulnerable state
		 * 
		 * @return true if the player can move, otherwise false
		 */
		public boolean canMove() {
			return (state.equals(State.INVULNERABLE) || state.equals(State.VULNERABLE));
		}

		public enum State {
			INVULNERABLE, VULNERABLE, DYING, DEAD;
		}
	}

	public static final class DirectedPosition {
		private final SubCell position;
		private final Direction direction;

		public DirectedPosition(SubCell position, Direction direction) {
			this.position = Objects.requireNonNull(position);
			this.direction = Objects.requireNonNull(direction);
		}

		/**
		 * Return an infinite sequence composed with the given directed position
		 * an represent the player stopped in that position
		 * 
		 * @param p
		 *            the given directed position
		 * @return the infinite sequence
		 */
		public static Sq<DirectedPosition> stopped(DirectedPosition p) {
			return Sq.constant(p);
		}

		/**
		 * Return the infinite sequence of the directed position representing
		 * the player moving in the direction he's looking for. The first
		 * element is the given directed position , the second is the neighbor's
		 * sub cell from the first element in the "look" direction and so on
		 * 
		 * @return the infinite sequence of directed position
		 */
		public static Sq<DirectedPosition> moving(DirectedPosition p) {
			return Sq.iterate(p, x -> x.withPosition(x.position().neighbor(x.direction())));

		}

		/*Getters and setters for the class*/

		public SubCell position() {
			return position;
		}

		public DirectedPosition withPosition(SubCell newPosition) {
			return new DirectedPosition(newPosition, direction());
		}

		public Direction direction() {
			return direction;
		}

		public DirectedPosition withDirection(Direction newDirection) {
			return new DirectedPosition(position(), newDirection);
		}
	}
}
