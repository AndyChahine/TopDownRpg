package dev.andrew.physics;

import java.util.Set;

import dev.andrew.entities.Entity;

public class QuadTree {

	private QuadTree[] nodes;
	private AABB aabb;
	private Entity[] entities;
	private int numEntities;
	
	public QuadTree(AABB aabb, int numChildrenPerNode) {
		this.nodes = new QuadTree[4];
		this.entities = new Entity[numChildrenPerNode];
		this.numEntities = 0;
		this.aabb = aabb;
	}
	
	public QuadTree(QuadTree[] nodes, Entity[] entities, int numEntities, AABB aabb) {
		this.nodes = nodes;
		this.entities = entities;
		this.numEntities = numEntities;
		this.aabb = aabb;
	}
	
	public void add(Entity entity) {
		if(entity.aabb.intersectAABB(aabb)) {
			if(numEntities < entities.length) {
				entities[numEntities] = entity;
				numEntities++;
			}else {
				addToChild(entity);
			}
		}else {
			QuadTree thisAsNode = new QuadTree(nodes, entities, numEntities, aabb);
			
			float dirX = entity.body.position.x - aabb.getCenterX();
			float dirY = entity.body.position.y - aabb.getCenterY();
			
			float minX = aabb.getMinX();
			float minY = aabb.getMinY();
			float maxX = aabb.getMaxX();
			float maxY = aabb.getMaxY();
			
			float expanseX = maxX - minX;
			float expanseY = maxY - minY;
			
			nodes = new QuadTree[4];
			numEntities = 0;
			entities = new Entity[entities.length];
			
			for(int i = 0; i < entities.length; i++) {
				entities[i] = null;
			}
			
			if(dirX <= 0 && dirY <= 0) {
				nodes[1] = thisAsNode;
				aabb = new AABB(minX - expanseX, minY - expanseY, maxX, maxY);
			}else if(dirX <= 0 && dirY > 0) {
				nodes[3] = thisAsNode;
				aabb = new AABB(minX - expanseX, minY, maxX, maxY + expanseY);
			}else if(dirX > 0 && dirY > 0) {
				nodes[2] = thisAsNode;
				aabb = new AABB(minX, minY, maxX + expanseX, maxY + expanseY);
			}else if(dirX > 0 && dirY <= 0) {
				nodes[0] = thisAsNode;
				aabb = new AABB(minX, minY - expanseY, maxX + expanseX, maxY);
			}else {
				System.err.println("Error: Quad Tree direction is invalid");
				System.exit(1);
			}
			
			add(entity);
 		}
	}
	
	private void tryToAddToChildNode(Entity entity, float minX, float minY, float maxX, float maxY, int nodeIndex) {
		if(entity.aabb.intersectRect(minX, minY, maxX, maxY)) {
			if(nodes[nodeIndex] == null) {
				nodes[nodeIndex] = new QuadTree(new AABB(minX, minY, maxX, maxY), entities.length);
			}
			
			nodes[nodeIndex].add(entity);
		}
	}
	
	private void addToChild(Entity entity) {
		float minX = aabb.getMinX();
		float minY = aabb.getMinY();
		float maxX = aabb.getMaxX();
		float maxY = aabb.getMaxY();
		
		float halfXLength = (maxX - minX) / 2.0f;
		float halfYLength = (maxY - minY) / 2.0f;
		
		minY += halfYLength;
		maxX -= halfXLength;
		tryToAddToChildNode(entity, minX, minY, maxX, maxY, 0);
		
		minX += halfXLength;
		maxX += halfXLength;
		tryToAddToChildNode(entity, minX, minY, maxX, maxY, 1);
		
		minY -= halfYLength;
		maxY -= halfYLength;
		tryToAddToChildNode(entity, minX, minY, maxX, maxY, 3);
		
		minX -= halfXLength;
		maxX -= halfXLength;
		tryToAddToChildNode(entity, minX, minY, maxX, maxY, 2);
	}
	
	public boolean remove(Entity entity) {
		if(!entity.aabb.intersectAABB(aabb)) {
			return false;
		}
		
		for(int i = 0; i < numEntities; i++) {
			if(entities[i] == entity) {
				removeEntityFromList(i);
			}
		}
		
		for(int i = 0; i < nodes.length; i++) {
			if(nodes[i] != null && nodes[i].remove(entity)) {
				nodes[i] = null;
			}
		}
		
		return isThisNodeEmpty();
	}
	
	private boolean isThisNodeEmpty() {
		for(int i = 0; i < nodes.length; i++) {
			if(nodes[i] != null) {
				return false;
			}
		}
		
		return numEntities == 0;
	}
	
	private void removeEntityFromList(int index) {
		for(int i = index + 1; i < numEntities; i++) {
			entities[i - 1] = entities[i];
		}
		
		entities[numEntities - 1] = null;
		numEntities--;
	}
	
	public Set<Entity> getAll(Set<Entity> result) {
		return queryRange(aabb, result);
	}
	
	public Set<Entity> queryRange(AABB bounds, Set<Entity> result) {
		if(!bounds.intersectAABB(aabb)) {
			return result;
		}
		
		for(int i = 0; i < numEntities; i++) {
			if(entities[i].aabb.intersectAABB(bounds)) {
				result.add(entities[i]);
			}
		}
		
		for(int i = 0; i < nodes.length; i++) {
			if(nodes[i] != null) {
				nodes[i].queryRange(bounds, result);
			}
		}
		
		return result;
	}
	
	public AABB getBounds() {
		return aabb;
	}
	
	public QuadTree[] getNodes() {
		return nodes;
	}
	
	public void print() {
		print(0, "NW");
	}
	
	private void print(int depth, String location) {
		String prefix = "";
		for(int i = 0; i < depth; i++) {
			prefix += "-";
		}
		
		for(int i = 0; i < numEntities; i++) {
			System.out.println(prefix + location + " " + i + ": " + entities[i]);
		}
		
		if(nodes[0] != null) {
			nodes[0].print(depth + 1, "NW");
		}
		
		if(nodes[1] != null) {
			nodes[1].print(depth + 1, "NE");
		}
		
		if(nodes[2] != null) {
			nodes[2].print(depth + 1, "SW");
		}
		
		if(nodes[3] != null) {
			nodes[3].print(depth + 1, "SE");
		}
	}
}
