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
			int maxBomb = 9;
			if (player.maxBombs() < maxBomb) {
				return player.withMaxBombs(player.maxBombs() + 1);
			}
			return player.withMaxBombs(player.maxBombs());
		}
	},

	INC_RANGE {
		@Override
		public Player applyTo(Player player) {
			int maxBombsRange = 9;
			if (player.bombRange() < maxBombsRange) {
				return player.withBombRange(player.bombRange() + 1);
			}
			return player.withBombRange(player.bombRange());
		}
	};

	abstract public Player applyTo(Player player);
}
