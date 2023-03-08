package dev.andrew.tilemap;

public class Camera {

	public float x;
	public float y;
	public float width;
	public float height;
	public float worldWidth;
	public float worldHeight;
	public float easing;
	
	public Camera(float width, float height, float worldWidth, float worldHeight) {
		this.width = width;
		this.height = height;
		this.worldWidth = worldWidth;
		this.worldHeight = worldHeight;
		this.easing = 0.09f;
	}
	
	public void update(float playerX, float playerY, float playerWidth, float playerHeight) {
		float targetX = playerX - width / 2 + playerWidth / 2;
		float targetY = playerY - height / 2 + playerHeight / 2;
		targetX = Math.min(worldWidth - width, Math.max(0, targetX));
		targetY = Math.min(worldHeight - height, Math.max(0, targetY));
		x += (targetX - x) * easing;
		y += (targetY - y) * easing;
	}
}
