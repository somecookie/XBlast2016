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
	 * retourne vrai si et seulement si la direction à laquelle on l'applique est horizontale à l'écran (E ou W)
	 * @return boolean
	 */
	public boolean isHorizontal(){
		return (this.equals(E) || this.equals(W));
	}

	/**
	 * retourne vrai si et seulement si la direction à laquelle on l'applique est parallèle à la direction that
	 * (une direction n'est parallèle qu'à elle-même et à sa direction opposée)
	 * @param Direction that
	 * @return boolean
	 */
	public boolean isParallelTo(Direction that){
		return (that.equals(this) || that.equals(this.opposite()));
	}
}
