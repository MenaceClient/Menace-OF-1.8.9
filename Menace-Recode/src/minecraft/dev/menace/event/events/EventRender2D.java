package dev.menace.event.events;

import dev.menace.event.Event;
import dev.menace.scripting.js.JSMapping;

@JSMapping(90)
public class EventRender2D extends Event {

	int width, height;
	
	public EventRender2D(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
}
