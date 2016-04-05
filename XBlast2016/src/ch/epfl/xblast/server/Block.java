/**
 * @author Ricardo Ferreira Ribeiro (250798)
 * @author Clarise Estelle Fleurimont (246866)
 * @date 29 févr. 2016
 */
package ch.epfl.xblast.server;

import java.util.NoSuchElementException;

/**
 * Définit les différents types de blocs.
 */
public enum Block {
    /**
     * FREE, qui représente un bloc libre, INDESTRUCTIBLE_WALL, qui représente un mur indestructible,
     * DESTRUCTIBLE_WALL qui représente un mur destructible, 
     * CRUMBLING_WALL, qui représente un mur (destructible) en train de s'écrouler.
     */
    FREE, INDESTRUCTIBLE_WALL, DESTRUCTIBLE_WALL, CRUMBLING_WALL, BONUS_BOMB(Bonus.INC_BOMB), BONUS_RANGE(Bonus.INC_RANGE);

    private Bonus maybeAssociatedBonus;

    private Block(Bonus maybeAssociatedBonus) {
        this.maybeAssociatedBonus = maybeAssociatedBonus;
    }

    private Block() {
      this.maybeAssociatedBonus = null;
    }

    /**
     * Methode testant si le block est libre ou non
     * @return true sii le block est libre
     */
    public boolean isFree() {
        if (this.equals(FREE)) {
            return true;
        }
        return false;
    }

    /**
     * Méthode testant si le block peut "acceuilire" un joueur
     * @return true sii un joueur peut aller sur le block
     */
    public boolean canHostPlayer() {
        if (this.isFree() || this.isBonus()) {
            return true;
        }
        return false;
    }

    /**
     * Méthode testant si le block met une ombre sur le plateau
     * @return true sii le block met une ombre sur le plateau de jeu
     */
    public boolean castsShadow() {
        if (this.equals(INDESTRUCTIBLE_WALL)||this.equals(DESTRUCTIBLE_WALL)|| this.equals(CRUMBLING_WALL)) {
            return true;
        }
        return false;
    }

    /**
     * 
     * @return
     */
    public boolean isBonus() {
        if (maybeAssociatedBonus != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 
     * @return
     */
    public Bonus associatedBonus() {
        if (isBonus()) {
            return maybeAssociatedBonus;
        } else {
            throw new NoSuchElementException("La case ne contient pas de bonus!");
        }
    }

}
