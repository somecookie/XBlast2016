/**
 * @author Ricardo Ferreira Ribeiro (250798)
 * @author Clarise Estelle Fleurimont (246866)
 * @date 7 mars 2016
 */
package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.ArgumentChecker;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;

public final class Bomb {
    private final PlayerID ownerID;
    private final Cell position;
    private final Sq<Integer> fuseLengths;
    private final int range;

    /**
     * construit une bombe avec le propriétaire, la position, la séquence de longueurs de mèche et la portée donnés
     * Lève l'exception  si l'un des trois premiers arguments est nul
     * Lève l'exception
     * @param ownerId
     * @param position
     * @param fuseLengths
     * @param range
     * @throws NullPointerException
     * @throws IllegalArgumentException
     */
    public Bomb(PlayerID ownerId, Cell position, Sq<Integer> fuseLengths, int range){
        if (fuseLengths.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.ownerID = Objects.requireNonNull(ownerId);;
        this.fuseLengths = Objects.requireNonNull(fuseLengths);
        this.position = Objects.requireNonNull(position);
        this.range = ArgumentChecker.requireNonNegative(range);
    }

    /**
     * construit une bombe avec le propriétaire, la position et la portée donnés, et la séquence de longueur de mèche (fuseLength, fuseLength - 1, fuseLength - 2, …, 1).
     * Lève les mêmes exceptions que le constructeur précédent, dans les mêmes situations.
     * @param ownerId
     * @param position
     * @param fuseLength
     * @param range
     */
    public Bomb(PlayerID ownerId, Cell position, int fuseLength, int range) {
        this.ownerID = Objects.requireNonNull(ownerId);;
        this.position = Objects.requireNonNull(position);
        this.range = ArgumentChecker.requireNonNegative(range);
        this.fuseLengths = Objects.requireNonNull(Sq.iterate(fuseLength, u -> u - 1).limit(fuseLength));
    }

    public PlayerID ownerId() {
        return ownerID;
    }

    public Cell position() {
        return position;
    }

    public Sq<Integer> fuseLengths() {
        return fuseLengths;
    }

    public int fuseLength() {
        return fuseLengths().head();
    }

    public int range() {
        return range;
    }

    /**
     * retourne l'explosion correspondant à la bombe, sous la forme d'un tableau de 4 éléments, chacun représentant un bras
     * la durée de cette explosion est donnée par la constante Ticks.EXPLOSION_TICKS définie lors de l'étape précédente.
     * @return
     */
    public List<Sq<Sq<Cell>>> explosion() {
        Sq<Integer> ticks = Sq.iterate(Ticks.EXPLOSION_TICKS, n -> n - 1).limit(Ticks.EXPLOSION_TICKS);
        List<Sq<Sq<Cell>>> explosion = new ArrayList<>();
        Sq<Sq<Cell>> north = explosionArmTowards(Direction.N);
        Sq<Sq<Cell>> est = explosionArmTowards(Direction.E).tail();
        Sq<Sq<Cell>> south = explosionArmTowards(Direction.S).tail();
        Sq<Sq<Cell>> west = explosionArmTowards(Direction.W).tail();

        explosion.add(Sq.repeat(Ticks.EXPLOSION_TICKS - range, north.head()));
        north = north.tail();
        explosion.add(Sq.repeat(Ticks.EXPLOSION_TICKS - range, est.head()));
        est = est.tail();
        explosion.add(Sq.repeat(Ticks.EXPLOSION_TICKS - range, south.head()));
        south = south.tail();
        explosion.add(Sq.repeat(Ticks.EXPLOSION_TICKS - range, west.head()));
        west = west.tail();

        for (int i = 0; i < range-1; i++) {
            explosion.get(0).concat(Sq.repeat(Ticks.EXPLOSION_TICKS - range, north.head()));
            north = north.tail();
            explosion.get(1).concat(Sq.repeat(Ticks.EXPLOSION_TICKS - range, est.head()));
            est = est.tail();
            explosion.get(2).concat(Sq.repeat(Ticks.EXPLOSION_TICKS - range, south.head()));
            south = south.tail();
            explosion.get(3).concat(Sq.repeat(Ticks.EXPLOSION_TICKS - range, west.head()));
            west = west.tail();
            ticks = ticks.tail();
        }

        return explosion;
    }

    /**
     * 
     * @param dir
     * @return
     */
    private Sq<Sq<Cell>> explosionArmTowards(Direction dir) {
        return Sq.constant(Sq.iterate(position, c -> c.neighbor(dir)));
    }
}
