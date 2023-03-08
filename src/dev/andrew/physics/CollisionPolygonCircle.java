package dev.andrew.physics;

public class CollisionPolygonCircle implements CollisionCallback {

	public static final CollisionPolygonCircle instance = new CollisionPolygonCircle();
	
	@Override
	public boolean handleCollision(Manifold m, Body a, Body b) 
	{
		boolean res = CollisionCirclePolygon.instance.handleCollision(m, b, a); 
		
		if ( m.contactCount > 0 )
		{
			m.normal.negi();
		}
		
		return res;
	}
}
