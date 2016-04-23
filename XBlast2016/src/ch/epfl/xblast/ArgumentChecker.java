/**
 * @author Ricardo Ferreira Ribeiro (250798)
 * @author Clarise Estelle Fleurimont (246866)
 * @date 7 mars 2016
 */
package ch.epfl.xblast;

public final class ArgumentChecker {
    private ArgumentChecker(){}

    /**
     * Retourne la valeur donnée si elle est positive ou nulle, et lève l'exception IllegalArgumentException sinon.
     * @param value
     *          La valeur donnée sous forme de int
     * @throws IllegalArgumentException
     *          Sii la valeur donnée est négative
     * @return value (int)
     *          La valeur donnée sii celle-ci est positive ou nulle
     */
    public static int requireNonNegative(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("La valeur donnée doit être positive!");
        } else {
            return value;
        }
    }
}
