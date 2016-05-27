package ch.epfl.xblast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public  final class Cell {
    public static final int COLUMNS = 15;
    public static final int ROWS = 13;
    public static final int COUNT = COLUMNS*ROWS;
    public static final List<Cell> ROW_MAJOR_ORDER = Collections.unmodifiableList(rowMajorOrder());
    public static final List<Cell> SPIRAL_ORDER = Collections.unmodifiableList(SpiralOrder());
    private final int x;
    private final int y;

    /**
     * Normalize the given coordinates x and y and constructs a sub cell with that coordinates
     * @param x
     *          given coordinate x
     * @param y
     *          given coordinate y
     */
    public Cell(int x, int y){
        this.x = Math.floorMod(x, COLUMNS);
        this.y = Math.floorMod(y, ROWS);
    }

    /**
     * Create a table in a form of cell's ArrayList in the spiral order (like the hand of the clock)
     * 
     * @return spiral (ArrayList<Cell>)
     *          cell's table
     */
    private static ArrayList<Cell> SpiralOrder() {
        ArrayList<Integer> ix = new ArrayList<>();
        ArrayList<Integer> iy = new ArrayList<>();
        ArrayList<Cell> spiral = new ArrayList<Cell>();
        boolean horizontal = true;

        for (int i = 0; i < COLUMNS; i++) {
            ix.add(i); 
        }
        for (int i = 0; i < ROWS; i++) {
            iy.add(i);
        }

        while(ix.isEmpty() == false  && iy.isEmpty()== false){
            ArrayList<Integer> i1 = new ArrayList<>();
            ArrayList<Integer> i2 = new ArrayList<>();
            Integer c2;
            if(horizontal){
                for (int i = 0; i < ix.size(); i++) {
                    i1.add(ix.get(i));
                }
                for (int i = 0; i < iy.size(); i++) {
                    i2.add(iy.get(i));
                }
                c2 = iy.get(0);
                iy.remove(0);
            }
            else{
                for (int i = 0; i < iy.size(); i++) {
                    i1.add(iy.get(i));
                }
                for (int i = 0; i < ix.size(); i++) {
                    i2.add(ix.get(i));
                }
                c2 = ix.get(0);
                ix.remove(0);
            }

            for (int i = 0; i < i1.size(); i++) {
                Integer c1 = i1.get(i);
                if(horizontal){
                    Cell c = new Cell(c1,c2);
                    spiral.add(c);
                }
                else{
                    Cell c = new Cell(c2,c1);
                    spiral.add(c);
                }
            }
            if (horizontal) {
                Collections.reverse(ix);
            }
            else {
                Collections.reverse(iy);
            }
            horizontal = !horizontal;
        }

        return spiral;
    }

    /**
     * Initialize the row major order array list, used to create the
     * unmodifiable List ROW_MAJOR_ORDER
     * 
     * @return an array of all cells in the row major order
     */
    private static ArrayList<Cell> rowMajorOrder() {
        ArrayList<Cell> currentRowMaj = new ArrayList<>();
        for (int i = 0; i < COUNT; i++) {
            currentRowMaj.add(null);
        }
        for (int x = 0; x < COLUMNS; x++) {
            for (int y = 0; y < ROWS; y++) {
                Cell c = new Cell(x,y);
                currentRowMaj.set(c.rowMajorIndex(), c);
            } 
        }
        return currentRowMaj;
    }

    /**
     * Returns the index of the cell, the number of it in row major ordering
     * 
     * @return the index of the cell
     */
    public int rowMajorIndex(){
       return (y()*COLUMNS +x());
    }

    /**
     * Return the normalized coordinate x of the cell
     * @return x (int)
     */
    public int x(){
        return x;
    }

    /**
     * Return the normalized coordinate y of the cell
     * @return y (int)
     */
    public int y(){
        return y;
    }

    /**
     * Return the cell's neighbor to the cell in construction, in the given direction
     * @param dir
     *        the given direction
     * @return neighbor cell (Cell)
     */
    public Cell neighbor(Direction dir){
        switch(dir){
        case N:
            if(y == 0){
                return new Cell(x(), 12);
            }
            else{
                return new Cell(x(), y-1);
            }
        case E:
			if (x == 14){
                return new Cell(0, y());
            }
            else{
                return new Cell(x()+1,y());
            }
        case S:
            if(y == 12){
                return new Cell(x(), 0);
            }
            else{
                return new Cell(x(), y()+1);
            }
        case W:
            if(x == 0){
                return new Cell(14, y());
            }
            else{
                return new Cell(x()-1, y());
            }
        default:
            throw new Error();   
        }
    }

    @Override
    public boolean equals(Object that){
    	return (that instanceof Cell) && (((Cell) that).x() == x
    			&& ((Cell) that).y() == y);
    }

    @Override
    public String toString(){
        return "("+x()+", "+y()+")";
    }

    @Override
    public int hashCode() {
        return rowMajorIndex();
    }
}
