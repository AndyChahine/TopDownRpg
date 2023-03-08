package dev.andrew.tilemap;

import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Tileset {

	public int firstgid;
	public int lastgid;
	public String name;
	public int tileWidth;
	public int tileHeight;
	public int imageWidth;
	public int imageHeight;
	public int cols;
	public int rows;
	public BufferedImage tilesetImage;
	public HashMap<Integer, BufferedImage> tiles = new HashMap<Integer, BufferedImage>();
	
	public Tileset(int firstgid, String name, int tileWidth, int tileHeight, int imageWidth, int imageHeight, BufferedImage tilesetImage){
		this.firstgid = firstgid;
		this.name = name;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		this.tilesetImage = tilesetImage;
		cols = (int) Math.floor(imageWidth / tileWidth);
		rows = (int) Math.floor(imageHeight / tileHeight);
		lastgid = (int) (cols * Math.floor(imageHeight / tileHeight) + firstgid - 1);
		
		System.out.println(tilesetImage);
		
		int currentId = firstgid;
		for(int r = 0; r < rows; r++) {
			for(int c = 0; c < cols; c++) {
				tiles.put(currentId, tilesetImage.getSubimage(c * tileWidth, r * tileHeight, tileWidth, tileHeight));
				currentId++;
			}
		}
	}
	
	public boolean contains(int id) {
		return tiles.containsKey(id);
	}
	
	public BufferedImage getTile(int id) {
		if(tiles.containsKey(id)) {
			return tiles.get(id);
		}
		
		return null;
	}
}
