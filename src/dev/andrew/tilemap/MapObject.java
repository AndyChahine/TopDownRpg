package dev.andrew.tilemap;

import dev.andrew.physics.Polygon;

public class MapObject {

	public int id;
	public String name;
	public float x;
	public float y;
	public Polygon polygon;
	public Type type;
	
	public enum Type {
		ENTITY,
		COLLIDER
	}
}
