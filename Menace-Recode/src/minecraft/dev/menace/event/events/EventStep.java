package dev.menace.event.events;

import dev.menace.event.Event;
import dev.menace.scripting.js.JSMapping;
import dev.menace.scripting.js.MappedName;

@JSMapping(105)
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

	@MappedName(73)
	public float getStepHeight() {
		return stepHeight;
	}

	@MappedName(74)
	public void setStepHeight(float stepHeight) {
		this.stepHeight = stepHeight;
	}

	@MappedName(58)
	public StepState getState() {
		return state;
	}
}
