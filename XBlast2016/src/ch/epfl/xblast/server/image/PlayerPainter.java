/**
 * @author Ricardo Ferreira Ribeiro (250798)
 * @author Eleonore Pochon (262959)
 */

package ch.epfl.xblast.server.image;

import ch.epfl.xblast.server.Player;
import ch.epfl.xblast.server.Player.LifeState;
import ch.epfl.xblast.server.Player.LifeState.State;

public final class PlayerPainter {

	public final static byte BYTE_FOR_DEATH = 15;
	
	/**
	 * Constructor of the player painter, it's empty because the class isn't instanciable
	 */
	private PlayerPainter() {
		
	}
	
	/**Calculate the image do we have to use for the player
	 * 
	 * @param ticks
	 * 		  the actual ticks
	 * @param player
	 * 		  the player used for the image
	 * @return the corresponding image in byte
	 */
	public static byte byteForPlayer(int ticks, Player player) {
		byte byteForPlayer = 0;
		State playerState = player.lifeState().state();
		int position;
		
		//Choose the id of the player and its corresponding image
		byteForPlayer += player.id().ordinal() * 20;

		if (player.lifeState().canMove()) {
			
			//of we have to use the black or white image
			if (playerState == State.INVULNERABLE && ((ticks % 1) == 1)) {
				byteForPlayer = 80;
			}

			byteForPlayer += player.direction().ordinal() * 3;
			//Calculate the feet's position
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
			//the case when the player is dying 
			if (playerState == State.DYING) {
				if (player.lives() <= 1) {
					return (byte) 13;
				} else {
					return (byte) 12;
				}
			 //the case when the player is dead
			} else {
				byteForPlayer = BYTE_FOR_DEATH;
			}

		}

		return byteForPlayer;
	}

}
