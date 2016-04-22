/**
 * @author Ricardo Ferreira Ribeiro (250798)
 * @author Clarise Estelle Fleurimont (246866)
 * @date 25 févr. 2016
 */
package ch.epfl.xblast;


/**
 * Immuable. Represente une sous-case. Elle offre une unique méthode publiques et statique
 */
public final class SubCell {
    private final int x, y;
    private static final int GANULARITY = 16;
    private static final int SUBCOLUMNS = GANULARITY*Cell.COLUMNS;
    private static final int SUBROWS = GANULARITY*Cell.ROWS;

    /**
     * Normalise les coordonnées x et y données et construit une sous-case avec ses dernières
     * @param x
     *          coordonnée x donnée
     * @param y
     *          coordonnée x donnée
     */
    public SubCell(int x, int y){
        this.x = Math.floorMod(x, SUBCOLUMNS);
        this.y = Math.floorMod(y, SUBROWS);
    }

    /**
     * Retourne la sous-case centrale de la case donnée
     * @param cell
     * @return la sous-case centrale de la case donnée comme argument
     */
    public static SubCell centralSubCellOf(Cell cell){
        int y = (cell.y()) * GANULARITY + (GANULARITY/2);
        int x = (cell.x()) * GANULARITY + (GANULARITY/2);
        return new SubCell(x, y);
    }

    /**
     * Retourne la coordonnée x normalisée de la sous-case
     * @return x (int)
     */
    public int x(){
        return x;
    }

    /**
     * Retourne la coordonnée y normalisée de la sous-case
     * @return y (int)
     */
    public int y(){
        return y;
    }

    /**
     * Retourne la distance de la sous-case en construction à la sous-case centrale la plus proche 
     * @return distance (int)
     */
    public int distanceToCentral(){
        SubCell central = centralSubCellOf(this.containingCell());
        return Math.abs(x() - central.x()) + Math.abs(y() - central.y());
    }

    /**
     * Retourne vrai sii la sous-case en construction est une sous-case centrale
     * @return vrai sii le case est une sous-case centrale (boolean)
     */
    public boolean isCentral() {
        return distanceToCentral() == 0;
    }

    /**
     * Retourne la sous-case voisine de la sous-case en construction, dans la direction donnée
     * @param d
     *          Direction donnée
     * @return sous-case voisine (SubCell)
     * @author Eleonore Pochon (262959)
     */
    public SubCell neighbor(Direction d) {
    	
            switch (d) {
            case N:
                return new SubCell(x(), y() - 1);
            case S:
                return new SubCell(x(), y() + 1);
            case E:
                return new SubCell(x() + 1, y());
            case W:
                return new SubCell(x() - 1, y());
            default:
                throw new Error();
            }
    }

    /**
     * Retourne la case dans laquelle la souscase en construction se situe
     * @return case dans laquelle la sous-case se trouve (Cell)
     */
    public Cell containingCell() {
        int xCell = (x() - (x() % GANULARITY))/GANULARITY;
        int yCell = (y() - (y() % GANULARITY))/GANULARITY;
        return new Cell(xCell, yCell);
    }

    @Override
    public boolean equals(Object that) {
        
    	return (that instanceof SubCell)
    			&& (((SubCell)that).x() == x() && ((SubCell)that).y() == y());
    }

    @Override
    public String toString() {
        return "(" + x() + " ," + y() + ")";
    }

    @Override
    public int hashCode() {
        return  y()*SUBCOLUMNS+x();
        }
}
