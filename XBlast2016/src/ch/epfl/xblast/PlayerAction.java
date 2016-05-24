/**
 * @author Ricardo Ferreira Ribeiro (250798)
 * @author Eleonore Pochon (262959)
 */
package ch.epfl.xblast;

public enum PlayerAction {
	JOIN_GAME, MOVE_N, MOVE_E, MOVE_S, MOVE_W, STOP, DROP_BOMB;

	public boolean isMove() {
		
		return this == MOVE_N || this == MOVE_E || this == MOVE_S || this == MOVE_W || this == STOP;
	}
}
