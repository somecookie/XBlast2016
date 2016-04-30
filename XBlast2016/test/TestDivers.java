import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.epfl.xblast.RunLengthEncoder;

public class TestDivers {

	public static void main(String[] args) {
		List<Integer> l = Arrays.asList(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
				2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 
				0, 0, 0, 0, 3, 1, 3, 1, 0, 0, 0, 0, 1, 1, 3, 1, 3, 1, 3, 1, 1, 0, 0, 0, 0, 1, 3, 1, 3, 0, 0, 0, 0, 
				1, 1, 1, 3, 1, 3, 1, 3, 1, 1, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 
				3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 1, 0, 0, 3, 1, 3, 1, 0, 0, 1, 1, 3, 1, 1, 0, 0, 1, 3, 1, 3, 0, 0, 1, 1, 1, 
				3, 1, 1, 2, 2, 2, 2, 2, 2, 2, 3, 2, 3, 2, 2, 2, 2, 2, 2, 2, 3, 2, 3, 1, 0, 0, 0, 0, 3, 0, 0, 0, 0, 1, 3, 2, 1, 2
				);
		
		List<Byte> newL = toByte(l);
		newL = RunLengthEncoder.encode(newL);
		System.out.println(newL);

	}
	
	public static List<Byte> toByte(List<Integer> l){
		List<Byte> newL = new ArrayList<>();
		for(Integer i : l){
			newL.add((byte)i.intValue());
		}
		return newL;
		
	}

}
