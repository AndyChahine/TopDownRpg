package dev.andrew.physics;

public class AABB {

	private float minX;
	private float minY;
	private float maxX;
	private float maxY;
	
	public AABB(float minX, float minY, float maxX, float maxY) {
		this.minX = minX;
		this.minY = minY;
		this.maxX = maxX;
		this.maxY = maxY;
	}
	
	public boolean intersectAABB(AABB other) {
		return intersectRect(other.getMinX(), other.getMinY(), other.getMaxX(), other.getMaxY());
	}
	
	public boolean intersectRect(float minX2, float minY2, float maxX2, float maxY2) {
		if(minX < maxX2 && maxX > minX2 && minY < maxY2 && maxY > minY2) {
			return true;
		}
		
		return false;
	}
	
	public float getDistanceX(AABB other) {
		
		float distance1 = other.getMinX() - getMaxX();
		float distance2 = getMinX() - other.getMaxX();
		
		float distance = distance1 > distance2 ? distance1 : distance2;
		
		return distance;
	}
	
	public float getDistanceY(AABB other) {
		float distance1 = other.getMinY() - getMaxY();
		float distance2 = getMinY() - other.getMaxY();
		
		float distance = distance1 > distance2 ? distance1 : distance2;
		
		return distance;
	}
	
	public float getCenterX() {
		return (minX + maxX) / 2.0f;
	}
	
	public float getCenterY() {
		return (minY + maxY) / 2.0f;
	}
	
	public float getMinX() {
		return minX;
	}
	
	public float getMinY() {
		return minY;
	}
	
	public float getMaxX() {
		return maxX;
	}
	
	public float getMaxY() {
		return maxY;
	}
}
