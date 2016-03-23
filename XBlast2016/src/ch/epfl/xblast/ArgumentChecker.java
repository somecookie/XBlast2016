/**
 * @author Ricardo Ferreira Ribeiro (250798)
 * @author Clarise Estelle Fleurimont (246866)
 * @date 7 mars 2016
 */
package ch.epfl.xblast;

public final class ArgumentChecker {
    private ArgumentChecker(){}

    public static int requireNonNegative(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("La valeur donnée doit être positive!");
        } else {
            return value;
        }
    }
}
