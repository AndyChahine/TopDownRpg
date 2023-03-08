package dev.andrew.entities;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import dev.andrew.fsm.AttackState;
import dev.andrew.fsm.MoveState;
import dev.andrew.fsm.StandState;
import dev.andrew.fsm.StateMachine;
import dev.andrew.graphics.Animation;
import dev.andrew.graphics.Frame;
import dev.andrew.input.GameInput;
import dev.andrew.loader.ImageLoader;
import dev.andrew.physics.Body;

public class Player implements GameObject {

	public static final int PLAYER_IMAGE_WIDTH = 160;
	public static final int PLAYER_IMAGE_HEIGHT = 128;
	public static final int HALF_WIDTH = PLAYER_IMAGE_WIDTH / 2;
	public static final int HALF_HEIGHT = PLAYER_IMAGE_HEIGHT / 2;
	
	public Animation idleUp;
	public Animation idleDown;
	public Animation idleLeft;
	public Animation idleRight;
	public Animation runDown;
	public Animation runUp;
	public Animation runLeft;
	public Animation runRight;
	public Animation attackDown;
	public Animation attackUp;
	public Animation attackLeft;
	public Animation attackRight;
	public Animation currentAnimation;
	public BufferedImage shadow;
	public Body body;
	public Direction direction;
	public StateMachine stateMachine;
	public float runSpeed = 3.0f;
	
	public Player(Body body) {
		this.body = body;
		this.direction = Direction.DOWN;
		
		BufferedImage bi = ImageLoader.load("/Open World And Cave Dungeon/Player/idle/idle-side-01.png");
		AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
		tx.translate(-bi.getWidth(null), 0);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		
		int animSpeed = 15;
		idleLeft = new Animation();
		idleLeft.addFrame(new Frame(op.filter(ImageLoader.load("/Open World And Cave Dungeon/Player/idle/idle-side-01.png"), null), animSpeed, 0));
		idleLeft.addFrame(new Frame(op.filter(ImageLoader.load("/Open World And Cave Dungeon/Player/idle/idle-side-02.png"), null), animSpeed, 1));
		idleLeft.addFrame(new Frame(op.filter(ImageLoader.load("/Open World And Cave Dungeon/Player/idle/idle-side-03.png"), null), animSpeed, 2));
		idleLeft.addFrame(new Frame(op.filter(ImageLoader.load("/Open World And Cave Dungeon/Player/idle/idle-side-04.png"), null), animSpeed, 3));
		idleLeft.addFrame(new Frame(op.filter(ImageLoader.load("/Open World And Cave Dungeon/Player/idle/idle-side-05.png"), null), animSpeed, 4));
		idleLeft.addFrame(new Frame(op.filter(ImageLoader.load("/Open World And Cave Dungeon/Player/idle/idle-side-06.png"), null), animSpeed, 5));
		idleLeft.addFrame(new Frame(op.filter(ImageLoader.load("/Open World And Cave Dungeon/Player/idle/idle-side-07.png"), null), animSpeed, 6));
		idleLeft.addFrame(new Frame(op.filter(ImageLoader.load("/Open World And Cave Dungeon/Player/idle/idle-side-08.png"), null), animSpeed, 7));
		idleLeft.addFrame(new Frame(op.filter(ImageLoader.load("/Open World And Cave Dungeon/Player/idle/idle-side-09.png"), null), animSpeed, 8));
		idleLeft.addFrame(new Frame(op.filter(ImageLoader.load("/Open World And Cave Dungeon/Player/idle/idle-side-10.png"), null), animSpeed, 9));
		
		idleRight = new Animation();
		idleRight.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/idle/idle-side-01.png"), animSpeed, 0));
		idleRight.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/idle/idle-side-02.png"), animSpeed, 1));
		idleRight.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/idle/idle-side-03.png"), animSpeed, 2));
		idleRight.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/idle/idle-side-04.png"), animSpeed, 3));
		idleRight.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/idle/idle-side-05.png"), animSpeed, 4));
		idleRight.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/idle/idle-side-06.png"), animSpeed, 5));
		idleRight.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/idle/idle-side-07.png"), animSpeed, 6));
		idleRight.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/idle/idle-side-08.png"), animSpeed, 7));
		idleRight.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/idle/idle-side-09.png"), animSpeed, 8));
		idleRight.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/idle/idle-side-10.png"), animSpeed, 9));
		
		idleDown = new Animation();
		idleDown.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/idle/idle-down-01.png"), animSpeed, 0));
		idleDown.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/idle/idle-down-02.png"), animSpeed, 1));
		idleDown.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/idle/idle-down-03.png"), animSpeed, 2));
		idleDown.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/idle/idle-down-04.png"), animSpeed, 3));
		idleDown.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/idle/idle-down-05.png"), animSpeed, 4));
		idleDown.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/idle/idle-down-06.png"), animSpeed, 5));
		idleDown.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/idle/idle-down-07.png"), animSpeed, 6));
		idleDown.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/idle/idle-down-08.png"), animSpeed, 7));
		idleDown.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/idle/idle-down-09.png"), animSpeed, 8));
		
		idleUp = new Animation();
		idleUp.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/idle/idle-up-01.png"), animSpeed, 0));
		idleUp.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/idle/idle-up-02.png"), animSpeed, 1));
		idleUp.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/idle/idle-up-03.png"), animSpeed, 2));
		idleUp.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/idle/idle-up-04.png"), animSpeed, 3));
		idleUp.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/idle/idle-up-05.png"), animSpeed, 4));
		idleUp.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/idle/idle-up-06.png"), animSpeed, 5));
		idleUp.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/idle/idle-up-07.png"), animSpeed, 6));
		idleUp.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/idle/idle-up-08.png"), animSpeed, 7));
		idleUp.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/idle/idle-up-09.png"), animSpeed, 8));
		idleUp.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/idle/idle-up-10.png"), animSpeed, 9));
		
		runDown = new Animation();
		runDown.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/run/run-down-01.png"), animSpeed, 0));
		runDown.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/run/run-down-02.png"), animSpeed, 1));
		runDown.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/run/run-down-03.png"), animSpeed, 2));
		runDown.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/run/run-down-04.png"), animSpeed, 3));
		runDown.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/run/run-down-05.png"), animSpeed, 4));
		
		runUp = new Animation();
		runUp.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/run/run-up-01.png"), animSpeed, 0));
		runUp.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/run/run-up-02.png"), animSpeed, 1));
		runUp.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/run/run-up-03.png"), animSpeed, 2));
		runUp.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/run/run-up-04.png"), animSpeed, 3));
		runUp.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/run/run-up-05.png"), animSpeed, 4));
		
		runRight = new Animation();
		runRight.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/run/run-side-01.png"), animSpeed, 0));
		runRight.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/run/run-side-02.png"), animSpeed, 1));
		runRight.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/run/run-side-03.png"), animSpeed, 2));
		runRight.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/run/run-side-04.png"), animSpeed, 3));
		runRight.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/run/run-side-05.png"), animSpeed, 4));
		runRight.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/run/run-side-06.png"), animSpeed, 5));
		runRight.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/run/run-side-07.png"), animSpeed, 6));
		runRight.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/run/run-side-08.png"), animSpeed, 7));
		
		bi = ImageLoader.load("/Open World And Cave Dungeon/Player/run/run-side-01.png");
		tx = AffineTransform.getScaleInstance(-1, 1);
		tx.translate(-bi.getWidth(null), 0);
		op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		
		runLeft = new Animation();
		runLeft.addFrame(new Frame(op.filter(ImageLoader.load("/Open World And Cave Dungeon/Player/run/run-side-01.png"), null), animSpeed, 0));
		runLeft.addFrame(new Frame(op.filter(ImageLoader.load("/Open World And Cave Dungeon/Player/run/run-side-02.png"), null), animSpeed, 1));
		runLeft.addFrame(new Frame(op.filter(ImageLoader.load("/Open World And Cave Dungeon/Player/run/run-side-03.png"), null), animSpeed, 2));
		runLeft.addFrame(new Frame(op.filter(ImageLoader.load("/Open World And Cave Dungeon/Player/run/run-side-04.png"), null), animSpeed, 3));
		runLeft.addFrame(new Frame(op.filter(ImageLoader.load("/Open World And Cave Dungeon/Player/run/run-side-05.png"), null), animSpeed, 4));
		runLeft.addFrame(new Frame(op.filter(ImageLoader.load("/Open World And Cave Dungeon/Player/run/run-side-06.png"), null), animSpeed, 5));
		runLeft.addFrame(new Frame(op.filter(ImageLoader.load("/Open World And Cave Dungeon/Player/run/run-side-07.png"), null), animSpeed, 6));
		runLeft.addFrame(new Frame(op.filter(ImageLoader.load("/Open World And Cave Dungeon/Player/run/run-side-08.png"), null), animSpeed, 7));
		
		attackDown = new Animation();
		attackDown.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/main_attacks/attack-down-01.png"), 10, 0));
		attackDown.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/main_attacks/attack-down-02.png"), 10, 1));
		attackDown.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/main_attacks/attack-down-03.png"), 10, 2));
		attackDown.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/main_attacks/attack-down-04.png"), 10, 3));
		attackDown.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/main_attacks/attack-down-05.png"), 10, 4));
		attackDown.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/main_attacks/attack-down-06.png"), 10, 5));
		attackDown.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/main_attacks/attack-down-07.png"), 10, 6));
		attackDown.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/main_attacks/attack-down-08.png"), 10, 7));
		attackDown.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/main_attacks/attack-down-09.png"), 10, 8));
		attackDown.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/main_attacks/attack-down-continue-10.png"), 10, 9));
		attackDown.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/main_attacks/attack-down-continue-11.png"), 10, 10));
		attackDown.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/main_attacks/attack-down-continue-12.png"), 10, 11));
		attackDown.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/main_attacks/attack-down-continue-13.png"), 10, 12));
		
		attackUp = new Animation();
		attackUp.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/main_attacks/attack-up-01.png"), 10, 0));
		attackUp.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/main_attacks/attack-up-02.png"), 10, 1));
		attackUp.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/main_attacks/attack-up-03.png"), 10, 2));
		attackUp.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/main_attacks/attack-up-04.png"), 10, 3));
		attackUp.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/main_attacks/attack-up-05.png"), 10, 4));
		attackUp.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/main_attacks/attack-up-06.png"), 10, 5));
		attackUp.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/main_attacks/attack-up-07.png"), 10, 6));
		attackUp.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/main_attacks/attack-up-08.png"), 10, 7));
		attackUp.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/main_attacks/attack-up-09.png"), 10, 8));
		attackUp.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/main_attacks/attack-up-continue-10.png"), 10, 9));
		attackUp.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/main_attacks/attack-up-continue-11.png"), 10, 10));
		attackUp.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/main_attacks/attack-up-continue-12.png"), 10, 11));
		attackUp.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/main_attacks/attack-up-continue-13.png"), 10, 12));
		
		attackRight = new Animation();
		attackRight.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/main_attacks/attack-side-01.png"), 10, 0));
		attackRight.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/main_attacks/attack-side-02.png"), 10, 1));
		attackRight.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/main_attacks/attack-side-03.png"), 10, 2));
		attackRight.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/main_attacks/attack-side-04.png"), 10, 3));
		attackRight.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/main_attacks/attack-side-05.png"), 10, 4));
		attackRight.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/main_attacks/attack-side-06.png"), 10, 5));
		attackRight.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/main_attacks/attack-side-07.png"), 10, 6));
		attackRight.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/main_attacks/attack-side-08.png"), 10, 7));
		attackRight.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/main_attacks/attack-side-09.png"), 10, 8));
		attackRight.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/main_attacks/attack-side-continue-10.png"), 10, 9));
		attackRight.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/main_attacks/attack-side-continue-11.png"), 10, 10));
		attackRight.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/main_attacks/attack-side-continue-12.png"), 10, 11));
		attackRight.addFrame(new Frame(ImageLoader.load("/Open World And Cave Dungeon/Player/main_attacks/attack-side-continue-13.png"), 10, 12));
		
		bi = ImageLoader.load("/Open World And Cave Dungeon/Player/main_attacks/attack-side-01.png");
		tx = AffineTransform.getScaleInstance(-1, 1);
		tx.translate(-bi.getWidth(null), 0);
		op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		
		attackLeft = new Animation();
		attackLeft.addFrame(new Frame(op.filter(ImageLoader.load("/Open World And Cave Dungeon/Player/main_attacks/attack-side-01.png"), null), 10, 0));
		attackLeft.addFrame(new Frame(op.filter(ImageLoader.load("/Open World And Cave Dungeon/Player/main_attacks/attack-side-02.png"), null), 10, 1));
		attackLeft.addFrame(new Frame(op.filter(ImageLoader.load("/Open World And Cave Dungeon/Player/main_attacks/attack-side-03.png"), null), 10, 2));
		attackLeft.addFrame(new Frame(op.filter(ImageLoader.load("/Open World And Cave Dungeon/Player/main_attacks/attack-side-04.png"), null), 10, 3));
		attackLeft.addFrame(new Frame(op.filter(ImageLoader.load("/Open World And Cave Dungeon/Player/main_attacks/attack-side-05.png"), null), 10, 4));
		attackLeft.addFrame(new Frame(op.filter(ImageLoader.load("/Open World And Cave Dungeon/Player/main_attacks/attack-side-06.png"), null), 10, 5));
		attackLeft.addFrame(new Frame(op.filter(ImageLoader.load("/Open World And Cave Dungeon/Player/main_attacks/attack-side-07.png"), null), 10, 6));
		attackLeft.addFrame(new Frame(op.filter(ImageLoader.load("/Open World And Cave Dungeon/Player/main_attacks/attack-side-08.png"), null), 10, 7));
		attackLeft.addFrame(new Frame(op.filter(ImageLoader.load("/Open World And Cave Dungeon/Player/main_attacks/attack-side-09.png"), null), 10, 8));
		attackLeft.addFrame(new Frame(op.filter(ImageLoader.load("/Open World And Cave Dungeon/Player/main_attacks/attack-side-continue-10.png"), null), 10, 9));
		attackLeft.addFrame(new Frame(op.filter(ImageLoader.load("/Open World And Cave Dungeon/Player/main_attacks/attack-side-continue-11.png"), null), 10, 10));
		attackLeft.addFrame(new Frame(op.filter(ImageLoader.load("/Open World And Cave Dungeon/Player/main_attacks/attack-side-continue-12.png"), null), 10, 11));
		attackLeft.addFrame(new Frame(op.filter(ImageLoader.load("/Open World And Cave Dungeon/Player/main_attacks/attack-side-continue-13.png"), null), 10, 12));
		
		shadow = ImageLoader.load("/Open World And Cave Dungeon/Player/idle/shadow.png");
		
		stateMachine = new StateMachine();
		stateMachine.add("move", new MoveState(this));
		stateMachine.add("stand", new StandState(this));
		stateMachine.add("attack", new AttackState(this));
		stateMachine.change("stand");
		
		currentAnimation = idleDown;
		currentAnimation.loop();
	}
	
	public void input(GameInput input) {
		stateMachine.input(input);
	}
	
	public void update() {
		stateMachine.update();
		currentAnimation.update();
	}
	
	public void render(Graphics2D g) {
		g.drawImage(shadow, (int)body.position.x - HALF_WIDTH, (int)body.position.y - HALF_HEIGHT, null);
		currentAnimation.render(g, (int)body.position.x - HALF_WIDTH, (int)body.position.y - HALF_HEIGHT);
		
//		if(action == Action.MOVE) {
//			if(direction == Direction.UP) {
//				currentAnimation = runUp;
//				currentAnimation.start();
//				currentAnimation.loop();
//			}else if(direction == Direction.LEFT) {
//				currentAnimation = runLeft;
//				currentAnimation.start();
//				currentAnimation.loop();
//			}else if(direction == Direction.DOWN) {
//				currentAnimation = runDown;
//				currentAnimation.start();
//				currentAnimation.loop();
//			}else if(direction == Direction.RIGHT) {
//				currentAnimation = runRight;
//				currentAnimation.start();
//				currentAnimation.loop();
//			}
//		}else if(action == Action.ATTACK) {
//			if(direction == Direction.UP) {
//				currentAnimation = attackUp;
//				currentAnimation.start();
//				currentAnimation.loop();
//			}else if(direction == Direction.LEFT) {
//				currentAnimation = attackLeft;
//				currentAnimation.start();
//				currentAnimation.loop();
//			}else if(direction == Direction.DOWN) {
//				currentAnimation = attackDown;
//				currentAnimation.start();
//				currentAnimation.loop();
//			}else if(direction == Direction.RIGHT) {
//				currentAnimation = attackRight;
//				currentAnimation.start();
//				currentAnimation.loop();
//			}
//		}else if(action == Action.IDLE){
//			if(direction == Direction.UP) {
//				currentAnimation = idleUp;
//				currentAnimation.start();
//				currentAnimation.loop();
//			}else if(direction == Direction.LEFT) {
//				currentAnimation = idleLeft;
//				currentAnimation.start();
//				currentAnimation.loop();
//			}else if(direction == Direction.DOWN) {
//				currentAnimation = idleDown;
//				currentAnimation.start();
//				currentAnimation.loop();
//			}else if(direction == Direction.RIGHT) {
//				currentAnimation = idleRight;
//				currentAnimation.start();
//				currentAnimation.loop();
//			}
//		}
		
		
	}
	
	public enum Direction {
		UP,
		DOWN,
		LEFT,
		RIGHT
	}
}
