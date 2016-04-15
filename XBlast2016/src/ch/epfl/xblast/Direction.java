package ch.epfl.xblast;

public enum Direction {
    N, E, S, W;
    
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
    
    public boolean isHorizontal(){
        return (this.equals(E) || this.equals(W));
    }
    
    public boolean isParallelTo(Direction that){
        return (that.equals(this) || that.equals(this.opposite()));
    }
}
