package dev.menace.event.events;

import dev.menace.event.Event;

public class EventStep extends Event {

	private float stepHeight;
	StepState stepState;
	
	public EventStep(float stepHeight, StepState stepState) {
		this.setStepHeight(stepHeight);
		this.stepState = stepState;
	}
	
	
	public float getStepHeight() {
		return stepHeight;
	}

	public void setStepHeight(float stepHeight) {
		this.stepHeight = stepHeight;
	}

	public StepState getStepState() {
		return stepState;
	}

	public enum StepState {
		PRE,
		POST
	}
	
}
