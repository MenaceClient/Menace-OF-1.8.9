package dev.menace.event.events;

import dev.menace.event.Event;
import dev.menace.scripting.js.JSMapping;
import dev.menace.scripting.js.MappedName;

@JSMapping(99)
public class EventMove extends Event {

	private double x, y, z;
	
	public EventMove(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@MappedName(19)
	public double getX() {
		return x;
	}

	@MappedName(22)
	public void setX(double x) {
		this.x = x;
	}

	@MappedName(20)
	public double getY() {
		return y;
	}

	@MappedName(23)
	public void setY(double y) {
		this.y = y;
	}

	@MappedName(21)
	public double getZ() {
		return z;
	}

	@MappedName(24)
	public void setZ(double z) {
		this.z = z;
	}
	
}
