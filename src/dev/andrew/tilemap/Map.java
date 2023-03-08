package dev.andrew.tilemap;

import java.awt.Graphics2D;
import java.util.ArrayList;

import dev.andrew.entities.Player;
import dev.andrew.maths.Matrix4f;
import dev.andrew.maths.Vector3f;

public class Map {

	public float worldWidth;
	public float worldHeight;
	public Camera camera;
	public ArrayList<Tileset> tilesets = new ArrayList<Tileset>();
	public ArrayList<Layer> layers = new ArrayList<Layer>();
	public ArrayList<ObjectLayer> objectLayers = new ArrayList<ObjectLayer>();
	
	public void render(Graphics2D g) {
		for(Layer layer : layers) {
//			if(layer.name.equals("Foreground")) {
//				continue;
//			}
			for(int row = 0; row < layer.rows; row++) {
				for(int col = 0; col < layer.cols; col++) {
					for(Tileset tileset : tilesets) {
						if(tileset.contains(layer.tiles[row][col])) {
							Vector3f tmp = new Vector3f(col * tileset.tileWidth, row * tileset.tileHeight, 0);
							
							if(tmp.x < camera.x + camera.width && tmp.x + tileset.tileWidth > camera.x && tmp.y < camera.y + camera.height && tmp.y + tileset.tileHeight > camera.y) {
								g.drawImage(tileset.getTile(layer.tiles[row][col]), (int)(tmp.x), (int)(tmp.y), tileset.tileWidth, tileset.tileHeight, null);
							}
						}
					}
				}
			}
		}
	}
}
