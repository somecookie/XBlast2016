/**
 * @author Ricardo Ferreira Ribeiro (250798)
 * @author Eleonore Pochon (262959)
 */

package ch.epfl.xblast.server;

import ch.epfl.xblast.server.Player.LifeState.State;

public final class PlayerPainter {

	public final static byte BYTE_FOR_DEATH = 15;
	public final static byte BYTE_FOR_LOSING_LIFE = 12;
	public final static byte BYTE_FOR_DYING = 13;
	public final static byte WHITE_PLAYER = 80;

	/**
	 * Constructor of the player painter, it's empty because the class isn't
	 * instanciable
	 */
	private PlayerPainter() {

	}

	/**
	 * Calculate the image do we have to use for the player
	 * 
	 * @param ticks
	 *            the actual ticks
	 * @param player
	 *            the player used for the image
	 * @return the corresponding image in byte
	 */
	public static byte byteForPlayer(int ticks, Player player) {
		byte byteForPlayer = 0;
		State playerState = player.lifeState().state();
		int position;

		byteForPlayer += player.id().ordinal() * 20;

		if (player.lifeState().canMove()) {

			if (playerState == State.INVULNERABLE && ((ticks % 2) == 1)) {
				byteForPlayer = WHITE_PLAYER;
			}

			byteForPlayer += player.direction().ordinal() * 3;

			if (player.direction().isHorizontal()) {
				position = player.position().x();
			} else {
				position = player.position().y();
			}

			switch (position % 4) {
			case 1:
				byteForPlayer += 1;
				break;

			case 3:
				byteForPlayer += 2;
				break;

			default:
				break;
			}

		} else {
			if (playerState == State.DYING) {
				if (player.lives() <= 1) {
					return BYTE_FOR_DYING;
				} else {
					return BYTE_FOR_LOSING_LIFE;
				}
			} else {
				byteForPlayer = BYTE_FOR_DEATH;
			}

		}

		return byteForPlayer;
	}

}
