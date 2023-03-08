package dev.andrew.fsm;

import java.awt.event.KeyEvent;

import dev.andrew.entities.Player;
import dev.andrew.input.GameInput;

public class StandState implements IState {
	
	private StateMachine stateMachine;
	private Player player;

	public StandState(Player player) {
		this.player = player;
		stateMachine = player.stateMachine;
	}
	
	@Override
	public void input(GameInput input) {
		if(input.keyDown[KeyEvent.VK_W] || input.keyDown[KeyEvent.VK_S] || input.keyDown[KeyEvent.VK_A] || input.keyDown[KeyEvent.VK_D]) {
			stateMachine.change("move");
		}else if(input.keyDown[KeyEvent.VK_SPACE]) {
			stateMachine.change("attack");
		}
	}

	@Override
	public void update() {
		
	}

	@Override
	public void enter() {
		if(player.direction == player.direction.UP) {
			player.currentAnimation = player.idleUp;
			player.currentAnimation.loop();
		}
		if(player.direction == player.direction.DOWN) {
			player.currentAnimation = player.idleDown;
			player.currentAnimation.loop();
		}
		if(player.direction == player.direction.LEFT) {
			player.currentAnimation = player.idleLeft;
			player.currentAnimation.loop();
		}
		if(player.direction == player.direction.RIGHT) {
			player.currentAnimation = player.idleRight;
			player.currentAnimation.loop();
		}
	}

	@Override
	public void exit() {
		player.currentAnimation.reset();
	}

}
