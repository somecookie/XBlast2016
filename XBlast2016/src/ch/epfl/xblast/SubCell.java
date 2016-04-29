/**
 * @author Ricardo Ferreira Ribeiro (250798)
 * @author Clarise Estelle Fleurimont (246866)
 * @date 25 févr. 2016
 */
package ch.epfl.xblast;


public final class SubCell {
    private final int x, y;
    public static final int SUBCOLUMNS = 240;
    public static final int SUBROWS = 208;

    /**
     * Normalize the given coordinates x and y and constructs a sub cell with that coordinates
     * @param x
     *          given coordinate x
     * @param y
     *          given coordinate y
     */
    public SubCell(int x, int y){
        this.x = Math.floorMod(x, SUBCOLUMNS);
        this.y = Math.floorMod(y, SUBROWS);
    }

    /**
     * Return the central sub cell of the given cell
     * @param cell
     * 		  the given cell
     * @return the central sub cell of the argument cell
     */
    public static SubCell centralSubCellOf(Cell cell){
        int y = (cell.y()) * 16 + 8;
        int x = (cell.x()) * 16 + 8;
        return new SubCell(x, y);
    }

    /**
     * Return the normalized coordinate x of the sub cell
     * @return x (int)
     */
    public int x(){
        return x;
    }

    /**
     * Return the normalized coordinate y of the sub cell
     * @return y (int)
     */
    public int y(){
        return y;
    }

    /**
     * Return the distance from the sub cell in construction to the nearest central sub cell
     * @return distance (int)
     */
    public int distanceToCentral(){
        SubCell central = centralSubCellOf(this.containingCell());
        return Math.abs(x() - central.x()) + Math.abs(y() - central.y());
    }

    /**
     * Return true iff the sub cell is in construction is a central sub cell
     * @return true iff the cell is central sub cell(boolean)
     */
    public boolean isCentral() {
        return distanceToCentral() == 0;
    }

    /**
     * Return the sub cell's neighbor to the sub cell in construction, in the given direction
     * @param d
     *        the given direction
     * @return neighbor sub cell (SubCell)
     */
    public SubCell neighbor(Direction d) {
        int yY = y(), xX = x();

        switch (d) {
        case N:
            if (y() == 0) {
                yY = 208;
            }
            return new SubCell(x(), yY-1);
        case E:
            if (x() == 239) {
                xX = -1;
            }
            return new SubCell(xX+1, y());
        case S:
            if (y() == 207) {
                yY = -1;
            }
            return new SubCell(x(), yY+1);
        case W:
            if (x() == 0) {
                xX = 240;
            }
            return new SubCell(xX-1, y());
          default:
              throw new Error();
        }
    }

    /**
     * Return the cell where stands the sub cell in construction
     * @return the cell where stands the sub cell (Cell)
     */
    public Cell containingCell() {
        int xCell = (x() - (x() % 16))/16;
        int yCell = (y() - (y() % 16))/16;
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
