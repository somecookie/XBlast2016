package ch.epfl.xblast.server.debug;

import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;
import ch.epfl.xblast.server.Bomb;
import ch.epfl.xblast.server.GameState;
import ch.epfl.xblast.server.Player;

public final class GameStatePrinter {
    private GameStatePrinter() {}

    public static void printGameState(GameState s) {
        List<Player> ps = s.alivePlayers();
        Board board = s.board();
        Map<Cell, Bomb> bomb = s.bombedCells();
        Set<Cell> blasts = s.blastedCells();

        for (int y = 0; y < Cell.ROWS; ++y) {
            xLoop: for (int x = 0; x < Cell.COLUMNS; ++x) {
                Cell c = new Cell(x, y);
                for (Player p: ps) {
                    if (p.position().containingCell().equals(c)) {
                        System.out.print(stringForPlayer(p));
                        continue xLoop;
                    }
                }

                for (Cell bla : blasts) {
                    if (bla.equals(c)) {
                        System.out.print(stringForBlast(bla));
                        continue xLoop;
                    }
                }
                
                for (Cell bbs: bomb.keySet()) {
                    if (bbs.equals(c)) {
                        System.out.print(stringForBomb(bbs));
                        continue xLoop;
                    }
                }
                
                Block b = board.blockAt(c);
                System.out.print(stringForBlock(b));
            }
         System.out.println();
        }

        for(Player p: s.players()){
        	stringForState(p);
        	
        }
        System.out.println("temps restant: "+s.remainingTime()+", Ticks: "+s.ticks());
          
    }
    
    public static void printGameStateWithoutPlayers(GameState s) {
        Board board = s.board();
        Map<Cell, Bomb> bomb = s.bombedCells();
        Set<Cell> blasts = s.blastedCells();

        for (int y = 0; y < Cell.ROWS; ++y) {
            xLoop: for (int x = 0; x < Cell.COLUMNS; ++x) {
                Cell c = new Cell(x, y);
                
//                for (Cell bbs: bomb.keySet()) {
//                    if (bbs.equals(c)) {
//                        System.out.print(stringForBomb(bbs));
//                        continue xLoop;
//                    }
//                }
                
                for (Cell bla : blasts) {
                    if (bla.equals(c)) {
                        System.out.print(stringForBlast(bla));
                        continue xLoop;
                    }
                }
                
                Block b = board.blockAt(c);
                System.out.print(stringForBlock(b));
            }
            System.out.println();
        }
    }

    private static String stringForPlayer(Player p) {
        StringBuilder b = new StringBuilder();
        b.append(p.id().ordinal() + 1);
        switch (p.direction()) {
        case N: b.append('^'); break;
        case E: b.append('>'); break;
        case S: b.append('v'); break;
        case W: b.append('<'); break;
        }
        return b.toString();
    }

    private static String stringForBlock(Block b) {
        switch (b) {
        case FREE: return "  ";
        case INDESTRUCTIBLE_WALL: return "##";
        case DESTRUCTIBLE_WALL: return "??";
        case CRUMBLING_WALL: return "¿¿";
        case BONUS_BOMB: return "+b";
        case BONUS_RANGE: return "+r";
        default: throw new Error();
        }
    }
    
    private static String stringForBomb(Cell c){
        return "BB";
    }
    private static String stringForBlast(Cell c){
        return "°°";
    }
    
    private static void stringForState(Player p){
    	System.out.println(stringForID(p.id())+" : "+p.lives()+" vies, "+p.lifeState().state());
    	System.out.println("bombes max: "+p.maxBombs()+", portée: "+p.bombRange());
    	System.out.println("position(Cell): "+p.position().containingCell()+"position(SubCell): "+p.position()+", direction:"+p.direction());
    }
    private static String stringForID(PlayerID id){
    	switch(id){
    	case PLAYER_1:
    		return "J1";
    	
    	case PLAYER_2:
    		return "J2";
    	
    	case PLAYER_3:
    		return "J3";    	
    	case PLAYER_4:
    		return "J4";
    	default:
    		throw new Error();
    	}
    }
    
}
