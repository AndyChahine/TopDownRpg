package dev.andrew.entities;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import dev.andrew.graphics.Animation;
import dev.andrew.graphics.Frame;
import dev.andrew.loader.ImageLoader;
import dev.andrew.physics.Body;

public class Chest implements GameObject {

	public Body body = null;
	public BufferedImage closed = null;
	public BufferedImage open = null;
	public Animation opening = null;
	public State state;
	
	public Chest(Body body) {
		this.body = body;
		this.state = State.CLOSED;
		
		closed = ImageLoader.load("/Open World And Cave Dungeon/Anim objects/Chest A/chestA_0004_chest-A-closed.png");
		open = ImageLoader.load("/Open World And Cave Dungeon/Anim objects/Chest A/chestA_0000_chest-A-open-static.png");
		
		opening = new Animation();
		opening.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Anim objects/Chest A/chestA_0003_chest-A-open-anim-01.png"), 5, 0));
		opening.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Anim objects/Chest A/chestA_0002_chest-A-open-anim-02.png"), 5, 1));
		opening.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Anim objects/Chest A/chestA_0001_chest-A-open-anim-03.png"), 5, 2));
	}
	
	public void update() {
		if(opening.getCurrentFrame() >= opening.getFrames().size() - 1) {
			state = State.OPEN;
		}
	}
	
	public void render(Graphics2D g) {
		if(state == State.CLOSED) {
			g.drawImage(closed, (int)body.position.x - 16, (int)body.position.y - 16, null);
		}else if(state == State.OPENING) {
			opening.render(g, (int)body.position.x - 16, (int)body.position.y - 16);
		}else if(state == State.OPEN) {
			g.drawImage(open, (int)body.position.x - 16, (int)body.position.y - 16, null);
		}
	}
	
	public enum State {
		CLOSED,
		OPENING,
		OPEN
	}
}
