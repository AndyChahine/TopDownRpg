package dev.andrew.tilemap;

import java.util.ArrayList;

public class ObjectLayer {

	public int id;
	public String name;
	public ArrayList<MapObject> mapObjects = new ArrayList<MapObject>();
	
	public ObjectLayer(int id, String name) {
		this.id = id;
		this.name = name;
	}
}
