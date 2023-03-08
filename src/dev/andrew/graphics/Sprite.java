package dev.andrew.graphics;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Sprite {

	private BufferedImage image;

	public Sprite(BufferedImage image) {
		this.image = image;
	}
	
	public void render(Graphics2D g, float x, float y) {
//		g.drawImage(image, x - anchorX, y - anchorY, image.getWidth(), image.getHeight());
	}
}
