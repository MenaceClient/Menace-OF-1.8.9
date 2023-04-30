package dev.menace.event.events;

import dev.menace.event.Event;
import dev.menace.scripting.js.JSMapping;
import dev.menace.scripting.js.MappedName;

@JSMapping(104)
public class EventSlowdown extends Event {

	private float strafeMultiplier, forwardMultiplier;
	
	public EventSlowdown(float strafeMultiplier, float forwardMultiplier) {
		this.strafeMultiplier = strafeMultiplier;
		this.forwardMultiplier = forwardMultiplier;
	}

	@MappedName(69)
	public float getStrafeMultiplier() {
		return strafeMultiplier;
	}

	@MappedName(70)
	public void setStrafeMultiplier(float strafeMultiplier) {
		this.strafeMultiplier = strafeMultiplier;
	}

	@MappedName(71)
	public float getForwardMultiplier() {
		return forwardMultiplier;
	}

	@MappedName(72)
	public void setForwardMultiplier(float forwardMultiplier) {
		this.forwardMultiplier = forwardMultiplier;
	}
	
}
