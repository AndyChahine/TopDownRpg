package dev.andrew.fsm;

import java.util.HashMap;
import java.util.Map;

import dev.andrew.input.GameInput;

public class StateMachine {

	private Map<String, IState> states;
	private IState currentState;
	
	public StateMachine() {
		currentState = new EmptyState();
		states = new HashMap<String, IState>();
	}
	
	public void input(GameInput input) {
		currentState.input(input);
	}
	
	public void update() {
		currentState.update();
	}
	
	public void change(String id) {
		currentState.exit();
		IState next = states.get(id);
		next.enter();
		currentState = next;
	}
	
	public void add(String id, IState state) {
		states.put(id, state);
	}
	
	public void remove(String id) {
		states.remove(id);
	}
	
	public void clear() {
		states.clear();
	}
	
	public IState currentState() {
		return currentState;
	}
}
