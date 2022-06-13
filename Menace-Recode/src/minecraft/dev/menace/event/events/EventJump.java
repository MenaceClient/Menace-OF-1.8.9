package dev.menace.event.events;

import dev.menace.event.Event;

public class EventJump extends Event {

	private float upwardsMotion;
	
	public EventJump(float upwardsMotion) {
		this.upwardsMotion = upwardsMotion;
	}

	public float getUpwardsMotion() {
		return upwardsMotion;
	}

	public void setUpwardsMotion(float upwardsMotion) {
		this.upwardsMotion = upwardsMotion;
	}
	
}
