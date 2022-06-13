package dev.menace.event.events;

import dev.menace.event.Event;

public class Event2D extends Event {

	int width, height;
	
	public Event2D(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
}
