package dev.menace.event.events;

import dev.menace.event.Event;

public class EventStep extends Event {

	private float stepHeight;
	private final StepState state;
	
	public EventStep(float stepHeight, StepState state) {
		this.stepHeight = stepHeight;
		this.state = state;
	}
	
	public enum StepState {
		PRE,
		POST;
	}

	public float getStepHeight() {
		return stepHeight;
	}

	public void setStepHeight(float stepHeight) {
		this.stepHeight = stepHeight;
	}

	public StepState getState() {
		return state;
	}
}
