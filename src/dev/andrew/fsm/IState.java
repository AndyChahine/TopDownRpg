package dev.andrew.fsm;

import dev.andrew.input.GameInput;

public interface IState {

	public void input(GameInput input);
	public void update();
	
	public void enter();
	public void exit();
}
