/**
 * @author Ricardo Ferreira Ribeiro (250798)
 * @author Clarise Estelle Fleurimont (246866)
 * @date 14 mars 2016
 */
package ch.epfl.xblast.server;


public enum Bonus {

	INC_BOMB {
		@Override
		public Player applyTo(Player player) {
			
			if (player.maxBombs() < 9) {
				return player.withMaxBombs(player.maxBombs() + 1);
			}
			return player;
		}
	},

	INC_RANGE {
		@Override
		public Player applyTo(Player player) {
			
			if (player.bombRange() < 9) {
				return player.withBombRange(player.bombRange() + 1);
			}
			return player;
		}
	};

	abstract public Player applyTo(Player player);

	}
