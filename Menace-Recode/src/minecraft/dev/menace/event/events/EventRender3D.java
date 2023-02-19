package dev.menace.event.events;

import dev.menace.event.Event;

public class EventRender3D extends Event {

	float partialTicks;
	
	public EventRender3D(float partialTicks) {
		this.partialTicks = partialTicks;
	}
	
}
