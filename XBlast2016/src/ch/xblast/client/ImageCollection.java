package ch.xblast.client;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.imageio.ImageIO;

/*
 * Represent an images' collection from a repertoire , the images are indexed by an
 * integer
 */
public final class ImageCollection {

	private final String dirName;
	private final File dir;
	private final Map<Integer, Image> images;

	/**
	 * 
	 * @param name
	 */
	public ImageCollection(String name) {
		dirName = name;
		try {
			dir = new File(ImageCollection.class.getClassLoader().getResource(dirName).toURI());
			images = loadingImages(dir);

		} catch (URISyntaxException e) {
			throw new Error();
		}
	}

	/**
	 * Get the image for a given index, in form of a sub classes image, we use
	 * that method in the case when we're not sure that the index is valid. If
	 * the image doesn't correspond to the index in the collections return null.
	 * 
	 * @param index
	 *            the given index
	 * @return the corresponding image to the index if it's the case
	 */
	public Image image(int index) {
		if (images.containsKey(index)) {
			return images.get(index);
		}
		return null;
	}

	/**
	 * Get the image for a given index, in form of a sub classes image, we use
	 * that method in the case when we're sure that the index is valid. If the
	 * image doesn't correspond to the index in the collections throw no such
	 * element exception.
	 * 
	 * @param index
	 *            the given index
	 * @return the corresponding image to the index if it's the case
	 */
	public Image imageOrNull(int index) {
		Image image = image(index);
		if (image == null) {
			throw new NoSuchElementException();
		}
		return image;
	}

	/**
	 * Can load the images from a repertoire who permits the access of the
	 * images
	 * 
	 * @param dir
	 *            the given file
	 * @return the loaded images
	 */
	private static Map<Integer, Image> loadingImages(File dir) {
		Map<Integer, Image> images = new HashMap<>();
		File[] pathNames = dir.listFiles();

		for (File p : pathNames) {

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
