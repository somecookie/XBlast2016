/**
 * @author Ricardo Ferreira Ribeiro (250798)
 * @author Eleonore Pochon (262959)
 */
package ch.epfl.xblast.server;

public enum BlockImage {
	IRON_FLOOR, IRON_FLOOR_S, DARK_BLOCK, EXTRA, EXTRA_O, BONUS_BOMB, BONUS_RANGE;
	
	/**
	 * return the byte code corresponding to this
	 * @return byte, the byte code of the BlockImage
	 */
	public byte byteImage(){
		switch(this){
		case IRON_FLOOR:
			return 000;
		case IRON_FLOOR_S:
			return 001;
		case DARK_BLOCK:
			return 002;
		case EXTRA:
			return 003;
		case EXTRA_O:
			return 004;
		case BONUS_BOMB:
			return 005;
		case BONUS_RANGE:
			return 006;
		default:
			throw new Error();
		}
	}
}
