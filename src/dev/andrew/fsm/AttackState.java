package dev.andrew.fsm;

import java.awt.event.KeyEvent;

import dev.andrew.entities.Player;
import dev.andrew.input.GameInput;

public class AttackState implements IState {
	
	private StateMachine stateMachine;
	private Player player;

	public AttackState(Player player) {
		this.player = player;
		this.stateMachine = player.stateMachine;
	}
	
	@Override
	public void input(GameInput input) {
		
	}

	@Override
	public void update() {
		if(player.currentAnimation.isStopped()) {
			stateMachine.change("stand");
		}
	}

	@Override
	public void enter() {
		if(player.direction == player.direction.UP) {
			player.currentAnimation = player.attackUp;
			player.currentAnimation.start();
		}else if(player.direction == player.direction.DOWN) {
			player.currentAnimation = player.attackDown;
			player.currentAnimation.start();
		}else if(player.direction == player.direction.LEFT) {
			player.currentAnimation = player.attackLeft;
			player.currentAnimation.start();
		}else if(player.direction == player.direction.RIGHT) {
			player.currentAnimation = player.attackRight;
			player.currentAnimation.start();
		}
	}

	@Override
	public void exit() {
		player.currentAnimation.reset();
	}

}
