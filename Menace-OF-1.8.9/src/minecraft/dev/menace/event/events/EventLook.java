package dev.menace.event.events;

import dev.menace.event.Event;

public class EventLook extends Event {

	float[] rotations;
	
	public EventLook(float[] rotations) {
		this.rotations = rotations;
	}

	public float[] getRotations() {
		return rotations;
	}
	
	public void setRotations(float[] rotations) {
		this.rotations = rotations;
	}
	
}
