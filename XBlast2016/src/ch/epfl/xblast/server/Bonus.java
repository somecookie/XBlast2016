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
            Player p = player;
            if (p.maxBombs() < 9) {
               return p.withMaxBombs(p.maxBombs() + 1);
            }
            return p;
        }
      },

      INC_RANGE {
        @Override
        public Player applyTo(Player player) {
            Player p = player;
            if (p.bombRange() < 9) {
                return p.withBombRange(p.bombRange() + 1);
            }
            return p;
        }
      };

      abstract public Player applyTo(Player player);
}
