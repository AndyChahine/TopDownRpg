package dev.andrew.graphics;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Frame {

	private int length;
	private BufferedImage image;
	private int numDisplayed;
	private int index;
	
	public Frame(BufferedImage image, int length, int index){
		this.image = image;
		this.length = length;
		this.index = index;
		numDisplayed = 0;
	}
	
	public boolean update() {
		if(numDisplayed >= length){
			numDisplayed = 0;
			return true;
		}
		
		return false;
	}

	public void render(Graphics2D g, float x, float y){
		g.drawImage(image, (int)x, (int)y, null);
		numDisplayed++;
	}
	
	public int getIndex() {
		return index;
	}
	
	public int getWidth() {
		return image.getWidth();
	}
	
	public int getHeight() {
		return image.getHeight();
	}
}
