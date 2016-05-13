import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.server.GameStateSerializer;
import ch.epfl.xblast.server.Level;
import ch.epfl.xblast.server.debug.GameStatePrinter;
import ch.epfl.xblast.server.debug.RandomEventGenerator;
import ch.epfl.xblast.server.image.BoardPainter;
import ch.xblast.client.GameState;
import ch.xblast.client.GameStateDeserializer;
import ch.xblast.client.XblastComponent;

public class TestDivers {

    private static void createUI(XblastComponent xb) {
        JFrame window = new JFrame("Image viewer");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        xb.setSize(xb.getPreferredSize());
        
        window.add(xb);
        
        window.pack();
        window.setVisible(true);
    }
    
    public static void main(String[] args) throws InterruptedException {
    	Level l = Level.DEFAULT_LEVEL;
    	BoardPainter bp = l.boardPainter();
    	ch.epfl.xblast.server.GameState gs = l.initialState();
        List<Byte> serial = GameStateSerializer.serialize(bp, gs);
    	GameState g = GameStateDeserializer.deserializerGameState(serial);
    	
    	XblastComponent xb = new XblastComponent();
    	xb.setGameState(g, PlayerID.PLAYER_1);
    	
    	SwingUtilities.invokeLater(() -> createUI(xb));

    	
    	RandomEventGenerator events = new RandomEventGenerator(2016, 30, 100);

	        while(!gs.isGameOver()){
	            gs = gs.next(events.randomSpeedChangeEvents(), events.randomBombDropEvents());
	            serial = GameStateSerializer.serialize(bp, gs);
	            g = GameStateDeserializer.deserializerGameState(serial);
	            xb.setGameState(g, PlayerID.PLAYER_1);
	            Thread.sleep(50);
	        }
	        GameStatePrinter.printGameState(gs);
	       
	        System.out.println(gs.winner());
    	

    }
	
	public static List<Byte> toByte(List<Integer> l){
		List<Byte> newL = new ArrayList<>();
		for(Integer i : l){
			newL.add((byte)i.intValue());
		}
		return newL;
		
	}
	
	/**
	 * Change the list's order , from the spiral order to the row major order
	 * 
	 * @param list
	 *            the list we will transform
	 * @return the transformed list
	 */
	public static List<Cell> spiralToRowMajorOrder(List<Cell> list) {

		Cell[] rowMajorOrder = new Cell[list.size()];

		int i = 0;
		for (Cell cell : Cell.SPIRAL_ORDER) {
			rowMajorOrder[cell.rowMajorIndex()] = list.get(i++);
		}

		return Arrays.asList(rowMajorOrder);
	}

}
