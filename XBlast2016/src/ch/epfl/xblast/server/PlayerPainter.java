/**
 * @author Ricardo Ferreira Ribeiro (250798)
 * @author Eleonore Pochon (262959)
 */

package ch.epfl.xblast.server;

import ch.epfl.xblast.server.Player.LifeState.State;

public final class PlayerPainter {

	public final static byte BYTE_FOR_DEATH = 15;

	private PlayerPainter() {

	}

	public static byte byteForPlayer(int ticks, Player player) {
		byte byteForPlayer = 0;
		State playerState = player.lifeState().state();
		int position;

		byteForPlayer += player.id().ordinal() * 20;

		if (player.lifeState().canMove()) {

			if (playerState == State.INVULNERABLE && ((ticks % 1) == 1)) {
				byteForPlayer = 80;
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
					return (byte) 13;
				} else {
					return (byte) 12;
				}
			} else {
				byteForPlayer = BYTE_FOR_DEATH;
			}

		}

		return byteForPlayer;
	}

}
