package dev.menace.event.events;

import dev.menace.event.Event;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;

public class EventCollide extends Event {

	private double posX, posY, posZ;
	private AxisAlignedBB axisAlignedBB;
	private Block block;
	
	public EventCollide(double posX, double posY, double posZ, AxisAlignedBB axisAlignedBB, Block block, Entity collidingEntity) {
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
		this.axisAlignedBB = axisAlignedBB;
		this.block = block;
	}

	public double getPosX() {
		return posX;
	}

	public void setPosX(double posX) {
		this.posX = posX;
	}

	public double getPosY() {
		return posY;
	}

	public void setPosY(double posY) {
		this.posY = posY;
	}

	public double getPosZ() {
		return posZ;
	}

	public void setPosZ(double posZ) {
		this.posZ = posZ;
	}

	public AxisAlignedBB getBoundingBox() {
		return axisAlignedBB;
	}

	public void setBoundingBox(AxisAlignedBB axisAlignedBB) {
		this.axisAlignedBB = axisAlignedBB;
	}

	public Block getBlock() {
		return block;
	}
	
}
