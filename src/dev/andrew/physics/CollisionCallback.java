package dev.andrew.physics;

public interface CollisionCallback {

	public boolean handleCollision( Manifold m, Body a, Body b );
}
