package dev.andrew.fsm;

import java.awt.event.KeyEvent;

import dev.andrew.entities.Player;
import dev.andrew.entities.Player.Direction;
import dev.andrew.input.GameInput;
import dev.andrew.physics.Vec2;

public class MoveState implements IState {
	
	private Player player;
	private StateMachine stateMachine;
	
	public MoveState(Player player) {
		this.player = player;
		stateMachine = player.stateMachine;
	}
	
	@Override
	public void input(GameInput input) {
		if(input.keyDown[KeyEvent.VK_SPACE]) {
			stateMachine.change("attack");
		}
		
		if(!input.keyDown[KeyEvent.VK_W] && !input.keyDown[KeyEvent.VK_S] && !input.keyDown[KeyEvent.VK_A] && !input.keyDown[KeyEvent.VK_D]) {
			stateMachine.change("stand");
		}
		
		if(input.keyDown[KeyEvent.VK_W]) {
			player.direction = Direction.UP;
			player.body.position.y -= player.runSpeed;
		}
		if(input.keyDown[KeyEvent.VK_S]) {
			player.direction = Direction.DOWN;
			player.body.position.y += player.runSpeed;
		}
		if(input.keyDown[KeyEvent.VK_A]) {
			player.direction = Direction.LEFT;
			player.body.position.x -= player.runSpeed;
		}
		if(input.keyDown[KeyEvent.VK_D]) {
			player.direction = Direction.RIGHT;
			player.body.position.x += player.runSpeed;
		}
	}

	@Override
	public void update() {
		
		if(player.direction == player.direction.UP) {
			player.runDown.reset();
			player.runLeft.reset();
			player.runRight.reset();
			player.currentAnimation = player.runUp;
			player.currentAnimation.loop();
		}
		if(player.direction == player.direction.DOWN) {
			player.runUp.reset();
			player.runLeft.reset();
			player.runRight.reset();
			player.currentAnimation = player.runDown;
			player.currentAnimation.loop();
		}
		if(player.direction == player.direction.LEFT) {
			player.runUp.reset();
			player.runDown.reset();
			player.runRight.reset();
			player.currentAnimation = player.runLeft;
			player.currentAnimation.loop();
		}
		if(player.direction == player.direction.RIGHT) {
			player.runUp.reset();
			player.runDown.reset();
			player.runLeft.reset();
			player.currentAnimation = player.runRight;
			player.currentAnimation.loop();
		}
	}

	@Override
	public void enter() {
		
	}

	@Override
	public void exit() {
		player.runUp.reset();
		player.runDown.reset();
		player.runLeft.reset();
		player.runRight.reset();
		player.currentAnimation.reset();
	}

}
