package ch.epfl.xblast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class RunLengthEncoder {
	
	private RunLengthEncoder(){}
	
	static public List<Byte> encode(List<Byte> notEncoded){
		List<Byte> encoded = new ArrayList<>();

		if(notEncoded.isEmpty() || notEncoded.size() <= 2){
			return notEncoded;
		}
		else{
			int count = 0;
			Byte previous = notEncoded.get(0);
			Iterator<Byte> nE = notEncoded.iterator();
			while(nE.hasNext()){
				count++;
				Byte b = nE.next();
				if(b < 0){
					throw new IllegalArgumentException();
				}
				
				if(b == previous){
					if(count == Math.abs(Byte.MIN_VALUE)+2){
						encoded.add(Byte.MIN_VALUE);
						encoded.add(b);
						count = 0;
						previous = b;
					}else if(!nE.hasNext()){
						if(count <= 2){
							for (int i = 0; i < count; i++) {
								encoded.add(previous);
							}
						}else{
							Byte length = (byte) -(count-2);
							encoded.add(length);
							encoded.add(b);
						}
					}
				}else{
					count--;
					if(count <= 2){
						for (int i = 0; i < count; i++) {
							encoded.add(previous);
						}
						if(!nE.hasNext()){
							encoded.add(b);
						}else{
							count = 1;
							previous = b;
						}
					}else{
						Byte length = (byte) -(count-2);
						encoded.add(length);
						encoded.add(previous);
						if(nE.hasNext()){
							count = 1;
							previous = b;
						}
						
					}
				}
			}
		}
		
		return encoded;
	}
	
	static public List<Byte> decode(List<Byte> encoded){
		List<Byte> notEncoded = new ArrayList<>();
		if(encoded.isEmpty()){
			return encoded;
		}else{
			if(encoded.get(encoded.size()-1)<0){
				throw new IllegalArgumentException();
			}
			Iterator<Byte> en = encoded.iterator();
			while(en.hasNext()){
				Byte b = en.next();
				if(b < 0){
					int index = Math.abs(b)+2;
					Byte nextB = en.next();
					for (int i = 0; i < index; i++) {
						notEncoded.add(nextB);
					}
				}
				else{
					notEncoded.add(b);
				}
			}
		}
		return notEncoded;
	}
	

}
