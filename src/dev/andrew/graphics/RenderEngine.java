package dev.andrew.graphics;

import java.awt.Graphics2D;

import dev.andrew.entities.Entity;
import dev.andrew.tilemap.Camera;

public class RenderEngine {
	
	public Camera camera;

	public RenderEngine(Camera camera) {
		this.camera = camera;
	}
	
	public void render(Graphics2D g, Entity e) {
		
	}
}
