package testPerso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;
import ch.epfl.xblast.server.Player;
import ch.epfl.xblast.server.image.BlockImage;

public class Util {
    public static Board board(){
        Block __ = Block.FREE;
        Block XX = Block.INDESTRUCTIBLE_WALL;
        Block xx = Block.DESTRUCTIBLE_WALL;
        Board board = Board.ofQuadrantNWBlocksWalled(
          Arrays.asList(
            Arrays.asList(__, __, __, __, __, xx, __),
            Arrays.asList(__, XX, xx, XX, xx, XX, xx),
            Arrays.asList(__, xx, __, __, __, xx, __),
            Arrays.asList(xx, XX, __, XX, XX, XX, XX),
            Arrays.asList(__, xx, __, xx, __, __, __),
            Arrays.asList(xx, XX, xx, XX, xx, XX, __)));
        
        return board;
  }
    public static Board oneElementBoard(Block b){
        //Cell (7,6)
        List<Block> free = Collections.nCopies(13, Block.FREE);
        List<Block> a = Collections.nCopies(6, Block.FREE);
        List<Block> notFree = new ArrayList<>(a);
        notFree.add(b);
        notFree.addAll(a);
        List<List<Block>> board = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            if(i==5){
                board.add(new ArrayList<>(notFree));
            }
            else{
                board.add(new ArrayList<>(free));
            }
        }
        return Board.ofInnerBlocksWalled(board);
    }
    
    public static List<Player> players(){
    	Player p = new Player(PlayerID.PLAYER_1, 3, new Cell(1,1), 2, 3);
    	Player p2 = new Player(PlayerID.PLAYER_2, 3, new Cell(13,1), 2, 3);
    	Player p3 = new Player(PlayerID.PLAYER_3, 3, new Cell(13,11), 2, 3);
    	Player p4 = new Player(PlayerID.PLAYER_4, 3, new Cell(1,11), 2, 3);
    	List<Player> players = new ArrayList<>(Arrays.asList(p,p2,p3,p4));
    	
    	return players;
    }
    public static Map<Block, BlockImage> pallet(){
    	Map<Block, BlockImage> p = new HashMap<>();
    	p.put(Block.FREE, BlockImage.IRON_FLOOR);
    	p.put(Block.INDESTRUCTIBLE_WALL, BlockImage.DARK_BLOCK);
    	p.put(Block.DESTRUCTIBLE_WALL, BlockImage.EXTRA);
    	p.put(Block.CRUMBLING_WALL, BlockImage.EXTRA_O);
    	p.put(Block.BONUS_BOMB, BlockImage.BONUS_BOMB);
    	p.put(Block.BONUS_RANGE, BlockImage.BONUS_RANGE);
    	
    	return p;
    }
}
