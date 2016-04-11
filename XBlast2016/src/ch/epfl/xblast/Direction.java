package ch.epfl.xblast;

public enum Direction {
    N, E, S, W;

    /**
     * Retourne la direction opposée de celle à laquelle on l'applique (S pour N, W pour E, etc.)
     * @return opposite (Direction)
     *          Direction opposée de celle à laquelle la méthode est appliquée
     */
    public Direction opposite(){
        switch(this){
        case N:
            return S;
        case E:
            return W;
        case S:
            return N;
        case W:
            return E;
        default:
           throw new Error();        
        } 
    }

    /**
     * 
     * @return
     */
    public boolean isHorizontal(){
        if(this.equals(E) || this.equals(W)){
            return true;
        }
        return false;
    }

    /**
     * 
     * @param that
     * @return
     */
    public boolean isParallelTo(Direction that){
        if (that.equals(this) || that.equals(this.opposite())){
            return true;
        }
        return false;
    }
}
