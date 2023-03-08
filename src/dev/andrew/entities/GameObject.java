package dev.andrew.entities;

import java.awt.Graphics2D;

import dev.andrew.physics.Body;

public interface GameObject {

	public Body body = null;
	
	public void render(Graphics2D g);
}
