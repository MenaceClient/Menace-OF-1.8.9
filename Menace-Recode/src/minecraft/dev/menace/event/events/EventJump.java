package dev.menace.event.events;

import dev.menace.event.Event;
import dev.menace.scripting.js.JSMapping;
import dev.menace.scripting.js.MappedName;

@JSMapping(95)
public class EventJump extends Event {

	private float upwardsMotion;
	
	public EventJump(float upwardsMotion) {
		this.upwardsMotion = upwardsMotion;
	}

	@MappedName(60)
	public float getUpwardsMotion() {
		return upwardsMotion;
	}

	@MappedName(61)
	public void setUpwardsMotion(float upwardsMotion) {
		this.upwardsMotion = upwardsMotion;
	}
	
}
