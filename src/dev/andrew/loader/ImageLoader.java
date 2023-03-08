package dev.andrew.loader;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageLoader {

	public static BufferedImage load(String path) {
		try {
			return ImageIO.read(ImageLoader.class.getResourceAsStream(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
