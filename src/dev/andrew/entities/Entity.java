package dev.andrew.entities;

import dev.andrew.graphics.Animation;
import dev.andrew.physics.AABB;
import dev.andrew.physics.Body;

public class Entity {

	public AABB aabb = null;
	public Body body = null;
	public Animation animation = null;
	
	public Entity(AABB aabb) {
		this.aabb = aabb;
	}
	
	public void updateAABB() {
		float deltaX = body.position.x - aabb.getCenterX();
		float deltaY = body.position.y - aabb.getCenterY();
		
		float minX = aabb.getMinX() + deltaX;
		float minY = aabb.getMinY() + deltaY;
		float maxX = aabb.getMaxX() + deltaX;
		float maxY = aabb.getMaxY() + deltaY;
		
		aabb = new AABB(minX, minY, maxX, maxY);
	}
}
