import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.epfl.xblast.RunLengthEncoder;

public class TestDivers {

	public static void main(String[] args) {
		List<Integer> l = Arrays.asList(1,2,3,4,5,6,7,8,9);
		
		try{
			for(Integer i : l){
				if(i == 4){
					throw new IllegalArgumentException();
				}
				System.out.println(i);
			}
		}catch(IllegalArgumentException e){

		}

	}
	
	public static List<Byte> toByte(List<Integer> l){
		List<Byte> newL = new ArrayList<>();
		for(Integer i : l){
			newL.add((byte)i.intValue());
		}
		return newL;
		
	}

}
