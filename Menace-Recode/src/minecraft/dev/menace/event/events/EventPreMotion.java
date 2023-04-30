package dev.menace.event.events;

import dev.menace.event.Event;
import dev.menace.scripting.js.JSMapping;
import dev.menace.scripting.js.MappedName;

@JSMapping(101)
public class EventPreMotion extends Event {
	
	private double posX;
	private double posY;
	private double posZ;
	private float yaw;
	private float pitch;
	private boolean onGround;
	
	public EventPreMotion(double posX, double posY, double posZ, float yaw, float pitch, boolean onGround) {
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
		this.yaw = yaw;
		this.pitch = pitch;
		this.onGround = onGround;
	}

	@MappedName(3)
	public double getPosX() {
		return posX;
	}

	@MappedName(4)
	public void setPosX(double posX) {
		this.posX = posX;
	}

	@MappedName(5)
	public double getPosY() {
		return posY;
	}

	@MappedName(52)
	public void setPosY(double posY) {
		this.posY = posY;
	}

	@MappedName(53)
	public double getPosZ() {
		return posZ;
	}

	@MappedName(54)
	public void setPosZ(double posZ) {
		this.posZ = posZ;
	}

	@MappedName(64)
	public float getYaw() {
		return yaw;
	}

	@MappedName(66)
	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	@MappedName(65)
	public float getPitch() {
		return pitch;
	}

	@MappedName(67)
	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	@MappedName(33)
	public boolean isOnGround() {
		return onGround;
	}

	@MappedName(34)
	public void setOnGround(boolean onGround) {
		this.onGround = onGround;
	}
	
}
