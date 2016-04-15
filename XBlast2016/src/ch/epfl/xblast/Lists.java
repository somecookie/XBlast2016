/**
 * @author Ricardo Ferreira Ribeiro (250798)
 * @author Clarise Estelle Fleurimont (246866)
 * @date 29 févr. 2016
 */
package ch.epfl.xblast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Lists {
    private Lists() {}

    /**
     * Retourne une version symétrique de la liste donnée.
     * Lève l'exception IllegalArgumentException si celle-ci est vide.
     * @param l
     * @return
     */
    public static <T> List<T> mirrored(List<T> l) {
        if (l.isEmpty()) {
            throw new IllegalArgumentException();
        }
        else {
            ArrayList<T> newL = new ArrayList<T>(l);
            ArrayList<T> lBis = new ArrayList<>(l);
            Collections.reverse(lBis);
            newL.addAll(lBis.subList(1,lBis.size()));
            List<T> imuL = Collections.unmodifiableList(new ArrayList<>(newL));
            return imuL;
        }
    }
    
    /**
     * retourne les permutations de la liste donnée en argument, dans un ordre quelconque.
     * @param l
     * @return
     */
    public static <T> List<List<T>> permutations(List<T> l){
    	List<T> newl = new ArrayList<>(l);
        List<List<T>> permut = new ArrayList<>();
        
        if(newl.isEmpty() || newl.size() == 1){
            permut.add(newl);
            return permut;
        }
        else{
            T head = newl.get(0);
            List<T> tail = newl.subList(1, newl.size());
            List<List<T>> prevPermut = permutations(tail);
            for (int i = 0; i < prevPermut.size(); i++) {
                for (int j = 0; j <= prevPermut.get(i).size() ; j++) {
                    List<T> transit = new ArrayList<>(prevPermut.get(i));
                    transit.add(j, head);
                    permut.add(transit);
                }
            }
            return permut;
        }
    }
    
    
}
