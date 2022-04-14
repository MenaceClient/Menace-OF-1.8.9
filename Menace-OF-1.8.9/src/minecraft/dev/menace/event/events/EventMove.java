package dev.menace.event.events;

import dev.menace.event.Event;

public class EventMove extends Event {

	public double x, y, z;
	
	public EventMove(double x, double y, double z) {
		this.x = x;
        this.y = y;
        this.z = z;
	}
	
}
