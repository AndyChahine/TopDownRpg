package dev.andrew.fsm;

import dev.andrew.input.GameInput;

public class EmptyState implements IState {

	@Override
	public void input(GameInput input) {}

	@Override
	public void update() {}

	@Override
	public void enter() {}

	@Override
	public void exit() {}

}
