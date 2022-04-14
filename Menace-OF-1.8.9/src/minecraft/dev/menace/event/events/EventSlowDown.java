package dev.menace.event.events;

import dev.menace.event.Event;

public class EventSlowDown extends Event {
	
	public float strafeMultiplier;
	public float forwardMultiplier;
	
	public EventSlowDown(	float strafeMultiplier, float forwardMultiplier) {
		this.strafeMultiplier = strafeMultiplier;
		this.forwardMultiplier = forwardMultiplier;
	}
	
}
