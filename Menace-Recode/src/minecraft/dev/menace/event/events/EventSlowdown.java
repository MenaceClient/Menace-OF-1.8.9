package dev.menace.event.events;

import dev.menace.event.Event;

public class EventSlowdown extends Event {

	private float strafeMultiplier, forwardMultiplier;
	
	public EventSlowdown(float strafeMultiplier, float forwardMultiplier) {
		this.strafeMultiplier = strafeMultiplier;
		this.forwardMultiplier = forwardMultiplier;
	}

	public float getStrafeMultiplier() {
		return strafeMultiplier;
	}

	public void setStrafeMultiplier(float strafeMultiplier) {
		this.strafeMultiplier = strafeMultiplier;
	}

	public float getForwardMultiplier() {
		return forwardMultiplier;
	}

	public void setForwardMultiplier(float forwardMultiplier) {
		this.forwardMultiplier = forwardMultiplier;
	}
	
}
