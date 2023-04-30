package dev.menace.event.events;

import dev.menace.event.Event;
import dev.menace.scripting.js.JSMapping;

@JSMapping(91)
public class EventRender3D extends Event {

	float partialTicks;
	
	public EventRender3D(float partialTicks) {
		this.partialTicks = partialTicks;
	}
	
}
