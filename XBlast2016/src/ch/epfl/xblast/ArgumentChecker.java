/**
 * @author Ricardo Ferreira Ribeiro (250798)
 * @author Clarise Estelle Fleurimont (246866)
 * @date 7 mars 2016
 */
package ch.epfl.xblast;

public final class ArgumentChecker {
    private ArgumentChecker(){}

    /**
     * Return the given value if it's positive or null, otherwise throw the IllegalArgumentException 
     * @param value
     *          the given value in integer
     * @throws IllegalArgumentException
     *          Iff the given value is negative
     * @return value (int)
     *          the given value if it's positive or null
     */
    public static int requireNonNegative(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("The given value has to be positive!");
        } else {
            return value;
        }
    }
}
