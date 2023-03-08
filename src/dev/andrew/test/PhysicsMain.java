package dev.andrew.test;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;

import dev.andrew.entities.Chest;
import dev.andrew.entities.GameObject;
import dev.andrew.entities.Player;
import dev.andrew.graphics.Display;
import dev.andrew.graphics.RenderEngine;
import dev.andrew.gui.ImGui;
import dev.andrew.input.GameInput;
import dev.andrew.input.GameMouseEvent;
import dev.andrew.input.GameMouseType;
import dev.andrew.physics.Body;
import dev.andrew.physics.Circle;
import dev.andrew.physics.ImpulseMath;
import dev.andrew.physics.ImpulseScene;
import dev.andrew.physics.Manifold;
import dev.andrew.physics.Mat2;
import dev.andrew.physics.Polygon;
import dev.andrew.physics.Vec2;
import dev.andrew.tilemap.Camera;
import dev.andrew.tilemap.Map;
import dev.andrew.tilemap.MapObject;
import dev.andrew.tilemap.ObjectLayer;
import dev.andrew.tilemap.TmxParser;

public class PhysicsMain {

	//TODO: spatial tree to hold entities - query entities to render and collision check
	// send entities rigid bodies to physics engine to check collisions(impulse scene)
	// send entities to render engine to render
	
	public Display display;
	public Graphics2D g;
	public ImpulseScene scene;
	public GameInput input;
	public ImGui gui;
	public Mode mode;
	public Body p = null;
	public Map map;
	public Player player;
	public Chest chest;
	public RenderEngine renderer;
	public Camera camera;
	
	public ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();
	
	public PhysicsMain() {
		TmxParser parser = new TmxParser();
		map = parser.createTilemap("/untitled.tmx");
		
		int width = 1024;
		int height = 768;
		
		camera = new Camera(800, 600, map.worldWidth, map.worldHeight);
		map.camera = camera;
		
		display = new Display(width, height, "Game Engine");
		g = (Graphics2D)display.getGraphics();
		input = display.getInput();
		
		renderer = new RenderEngine(camera);
		
		scene = new ImpulseScene(10);
		
		gui = new ImGui(g);
		mode = Mode.SELECT;
	}
	
	public float rectCenterX;
	public float rectCenterY;
	
	public void start() {
		Body a = null;
		
		a = scene.add(new Circle(30.0f), 200, 200);
		a.setStatic();
		
		a = scene.add(new Polygon(200.0f, 10.0f), 240, 300);
		a.setOrient(0);
		
		p = scene.add(new Circle(30.0f), 250, 200);
		p.setOrient(0);
		p.restitution = 0.2f;
		p.dynamicFriction = 0.2f;
		p.staticFriction = 0.4f;
		
		float minX = Float.MAX_VALUE;
		float maxX = -Float.MAX_VALUE;
		float minY = Float.MAX_VALUE;
		float maxY = -Float.MAX_VALUE;
		
		for(ObjectLayer objLayer : map.objectLayers) {
			for(MapObject mo : objLayer.mapObjects) {
				if(mo.type == MapObject.Type.COLLIDER) {
					for(int i = 0; i < mo.polygon.vertices.length; i++) {
						Vec2 pos = mo.polygon.vertices[i];
						minX = Math.min(minX, pos.x);
						maxX = Math.max(maxX, pos.x);
						minY = Math.min(minY, pos.y);
						maxY = Math.max(maxY, pos.y);
					}
					
					rectCenterX = minX + ((maxX - minX) / 2.0f);
					rectCenterY = minY + ((maxY - minY) / 2.0f);
					
					a = scene.add(mo.polygon, (rectCenterX + mo.x), (rectCenterY + mo.y));
					a.setStatic();
					a.setOrient(0);
				}else if(mo.type == MapObject.Type.ENTITY) {
					
					if(mo.name.equals("Player")) {
						Body bod = scene.add(new Polygon(8, 16), mo.x, mo.y);
						bod.setOrient(0);
						player = new Player(bod);
					}else if(mo.name.equals("Chest")) {
						Body bod = scene.add(new Polygon(8, 10), mo.x, mo.y);
						bod.setOrient(0);
						bod.setStatic();
						chest = new Chest(bod);
					}
				}
			}
		}
		
		gameObjects.add(player);
		gameObjects.add(chest);
	}
	
	public void run() {
//		int frames = 0;
//		double frameCounter = 0;
//		
//		double lastTime = Time.getTime();
//		double unProcessedTime = 0;
//		double frameTime = 1.0 / 60.0;
//		
//		while(true){
//			boolean render = false;
//			
//			double startTime = Time.getTime();
//			double passedTime = startTime - lastTime;
//			lastTime = startTime;
//			
//			unProcessedTime += passedTime;
//			frameCounter += passedTime;
//			
//			while(unProcessedTime > frameTime){
//				render = true;
//				
//				unProcessedTime -= frameTime;
//				
//				input();
//				
//				update((float)frameTime);
//				
//				if(frameCounter >= 1.0){
//					display.setTitle("Game Engine" + "     " + frames + " fps " + Math.round(frameCounter * 1000) + " ms");
//					frames = 0;
//					frameCounter = 0;
//				}
//			}
//			
//			if(render){
//				render(1);
//				display.update();
//				frames++;
//			}else{
//				try {
//					Thread.sleep(1);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		}
		
		double t = 0.0;
		double dt = 1.0d / 60.0d;
		
		double currentTime = System.nanoTime() / 1000000000.0d;
		double accumulator = 0.0;
		
		double counter = 0.0;
		float updates = 0.0f;
		float frames = 0.0f;
		
		while(true) {
			double newTime = System.nanoTime() / 1000000000.0d;
			double frameTime = newTime - currentTime;
			if(frameTime > 0.25d) {
				frameTime = 0.25d;
			}
			currentTime = newTime;
			
			counter += frameTime;
			accumulator += frameTime;
			
			while(accumulator >= dt) {
				// prev state = current state
				// integrate
//				prevX = x;
				update((float)dt);
				t += dt;
				accumulator -= dt;
				
				input((float)dt);
				
				updates++;
			}
			
			double alpha = accumulator / dt;
			
//			newX = (float) ((x * alpha) + prevX * (1.0 - alpha));
			
			// State state = currentState * alpha + prevState * (1.0 - alpha)
			
			render(1);
			display.update();
			
			frames++;
			
			if(counter > 1.0) {
				System.out.println("UPS: " + updates + " FPS " + frames);
				updates = 0;
				frames = 0;
				counter = 0;
			}
		}
	}
	
	ArrayList<Vec2> points = new ArrayList<Vec2>();
	
	public void input(float dt) {
		gui.input(input);
		
		GameMouseEvent mouseEvent = null;
		if(input.mouseEvents.peek() != null) {
			mouseEvent = input.mouseEvents.poll();
		}
		
		
		player.input(input);
		
//		player.action = Player.Action.IDLE;
//		
//		if(input.keyDown[KeyEvent.VK_W]) {
//			player.action = Player.Action.MOVE;
//			player.body.position.y -= 3;
//			player.direction = Direction.UP;
//		}else if(input.keyDown[KeyEvent.VK_S]) {
//			player.action = Player.Action.MOVE;
//			player.body.position.y += 3;
//			player.direction = Direction.DOWN;
//		}else if(input.keyDown[KeyEvent.VK_A]) {
//			player.action = Player.Action.MOVE;
//			player.body.position.x -= 3;
//			player.direction = Direction.LEFT;
//		}else if(input.keyDown[KeyEvent.VK_D]) {
//			player.action = Player.Action.MOVE;
//			player.body.position.x += 3;
//			player.direction = Direction.RIGHT;
//		}else if(input.keyDown[KeyEvent.VK_SPACE]) {
//			player.action = Player.Action.ATTACK;
//		}
//		if(input.keyDown[KeyEvent.VK_F]) {
//			chest.state = State.OPENING;
//		}
//		
//		if(input.keyDown[KeyEvent.VK_R]) {
//			scene.clear();
//		}
		
		if(mode == Mode.SELECT) {
			// generate random polygon
//			if (mouseEvent != null && mouseEvent.type == GameMouseType.Click && mouseEvent.e.getButton() == MouseEvent.BUTTON1)
//			{
//				float r = ImpulseMath.random( 10.0f, 50.0f );
//				int vertCount = ImpulseMath.random( 3, Polygon.MAX_POLY_VERTEX_COUNT );
//
//				Vec2[] verts = Vec2.arrayOf( vertCount );
//				for (int i = 0; i < vertCount; i++)
//				{
//					verts[i].set( ImpulseMath.random( -r, r ), ImpulseMath.random( -r, r ) );
//				}
//
//				Body b = scene.add( new Polygon( verts ), input.mouseX, input.mouseY );
//				b.setOrient( ImpulseMath.random( -ImpulseMath.PI, ImpulseMath.PI ) );
//				b.restitution = 0.2f;
//				b.dynamicFriction = 0.2f;
//				b.staticFriction = 0.4f;
//			}
			
			if(input.mouseDown[MouseEvent.BUTTON1]) {
				Manifold m = null;
				boolean selected = false;
				for(Body b : scene.bodies) {
					Body a = new Body(new Circle(1), (int)(input.mouseX + camera.x), (int)(input.mouseY + camera.y));
					m = new Manifold(a, b);
					m.solve();
					if(m.contactCount >= 1) {
						selected = true;
						break;
					}
				}
				
				if(m != null && selected) {
					m.B.velocity.x = 0;
					m.B.velocity.y = 0;
					m.B.angularVelocity = 0;
					m.B.position.x = input.mouseX + camera.x;
					m.B.position.y = input.mouseY + camera.y;
				}
			}
		}else if(mode == Mode.POLYGON) {
			if(input.keyDown[KeyEvent.VK_SHIFT]) {
				if(mouseEvent != null && mouseEvent.type == GameMouseType.Click && mouseEvent.e.getButton() == MouseEvent.BUTTON1) {
					points.add(new Vec2(input.mouseX, input.mouseY));
				}
			}else {
				if(points.size() >= 3) {
					Vec2 centroid = new Vec2(0, 0);
					for(Vec2 p : points) {
						centroid.addi(p);
					}
					
					centroid.divi(points.size());
					
					Vec2[] verts = new Vec2[points.size()];
					for(int i = 0; i < points.size(); i++) {
						verts[i] = points.get(i).subi(centroid);
					}
					
					Body b = scene.add(new Polygon(verts), (int)centroid.x, (int)centroid.y);
					b.setOrient(0);
					b.restitution = 0.2f;
					b.dynamicFriction = 0.2f;
					b.staticFriction = 0.4f;
				}
				
				points.clear();
			}
		}else if(mode == Mode.RANDOM) {
			if (input.mouseDown[MouseEvent.BUTTON1])
			{
				float hw = ImpulseMath.random( 10.0f, 30.0f );
				float hh = ImpulseMath.random( 10.0f, 30.0f );
				
				Body b = scene.add( new Polygon( hw, hh ), (int)(input.mouseX + camera.x), (int)(input.mouseY + camera.y));
				b.setOrient( 0.0f );
			}
			
			if (input.mouseDown[MouseEvent.BUTTON3])
			{
				float r = ImpulseMath.random( 10.0f, 30.0f );

				scene.add( new Circle( r ), (int)(input.mouseX + camera.x), (int)(input.mouseY + camera.y));
			}
		}
	}
	
	public void update(float dt) {
		scene.step(dt);
		player.update();
		chest.update();
		camera.update(player.body.position.x, player.body.position.y, 16, 32);
	}
	
	public void render(float alpha) {
		display.clear(Color.BLACK);
		
		g.translate(-camera.x, -camera.y);
		
		map.render(g);
		
		if(mode == Mode.POLYGON) {
			for(Vec2 p : points) {
				g.setColor(new Color(255, 243, 153));
				g.fillOval((int)p.x, (int)p.y, 10, 10);
			}
		}
		
//		for (Body b : scene.bodies)
//		{
//			if(b.position.x < camera.x || b.position.x > camera.x + camera.width || b.position.y < camera.y || b.position.y > camera.y + camera.height) {
//				continue;
//			}
//			
//			float interX = b.prevPosition.x * alpha + b.position.x * (1.0f - alpha);
//			float interY = b.prevPosition.y * alpha + b.position.y * (1.0f - alpha);
//			b.prevPosition.set(b.position);
//			
//			float interOrient = b.prevOrient * alpha + b.orient * (1.0f - alpha);
//			b.prevOrient = b.orient;
//			
//			Mat2 interU = new Mat2(interOrient);
//			
//			g.setColor(Color.GREEN);
//			g.fillOval((int)(interX - 5f / 2f), (int)(interY - 5f / 2f), 5, 5);
//			
//			if (b.shape instanceof Circle)
//			{
//				Circle c = (Circle)b.shape;
//
//				float rx = (float)StrictMath.cos( interOrient ) * c.radius;
//				float ry = (float)StrictMath.sin( interOrient ) * c.radius;
//				
//				g.setColor(Color.red);
//				g.draw(new Ellipse2D.Float(interX - c.radius, interY - c.radius, c.radius * 2, c.radius * 2 ));
//				g.draw(new Line2D.Float(interX, interY, interX + rx, interY + ry ));
//			}
//			else if (b.shape instanceof Polygon)
//			{
//				Polygon p = (Polygon)b.shape;
//				
//				Path2D.Float path = new Path2D.Float();
//				for (int i = 0; i < p.vertexCount; i++)
//				{
//					Vec2 v = new Vec2( p.vertices[i] );
//					interU.muli( v );
//					v.addi(new Vec2(interX, interY));
//
//					if (i == 0)
//					{
//						path.moveTo( v.x, v.y );
//					}
//					else
//					{
//						path.lineTo( v.x, v.y );
//					}
//				}
//				path.closePath();
//
//				g.setColor( Color.blue );
//				g.draw( path );
//			}
//		}

		g.setColor( Color.white );
		for (Manifold m : scene.contacts)
		{
			for (int i = 0; i < m.contactCount; i++)
			{
				Vec2 v = m.contacts[i];
				Vec2 n = m.normal;

				g.draw( new Line2D.Float( v.x, v.y, v.x + n.x * 4.0f, v.y + n.y * 4.0f ) );
			}
		}
		
		chest.render(g);
		
		player.render(g);
		
		for(GameObject go : gameObjects) {
			go.render(g);
		}
		
		g.translate(camera.x, camera.y);
		
		display.update();
	}
	
	public enum Mode{
		SELECT, POLYGON, RANDOM
	}
	
	public static void main(String[] args) {
		PhysicsMain m = new PhysicsMain();
		m.start();
		m.run();
	}
}
