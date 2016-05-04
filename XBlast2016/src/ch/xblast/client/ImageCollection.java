package ch.xblast.client;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.imageio.ImageIO;

public final class ImageCollection {
	private final String dirName;
	private final File dir;
	private final Map<Integer, Image> images;
	
	

	public ImageCollection(String name) {
		dirName = name;
		try{
			dir = new File(ImageCollection.class.getClassLoader().getResource(dirName).toURI());
			images = loadingImages(dir);
			
		}
		catch(URISyntaxException e){
			throw new Error();
		}
	}
	
	public Image image(int index){
		if(images.containsKey(index)){
			return images.get(index);
		}
		return null;
	}
	
	public Image imageOrNull(int index){
		Image image = image(index);
		if(image == null){
			throw new NoSuchElementException();
		}
		return image;
	}
	
	private static Map<Integer, Image> loadingImages(File dir){
		Map<Integer, Image> images = new HashMap<>();
		File[] pathNames = dir.listFiles();

		for(File p : pathNames){

			try {
				Image image = ImageIO.read(p);
				String name = p.getName();
				name = name.substring(0, 3);
				int i = Integer.parseInt(name);
				images.put(i, image);
			} catch (IOException e) {

			}
		}
		return images;
	}
}
