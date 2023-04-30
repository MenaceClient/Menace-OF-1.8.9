package dev.menace.event.events;

import dev.menace.event.Event;
import dev.menace.scripting.js.JSMapping;
import dev.menace.scripting.js.MappedName;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;

@JSMapping(93)
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

	@MappedName(3)
	public double getPosX() {
		return posX;
	}

	@MappedName(52)
	public void setPosX(double posX) {
		this.posX = posX;
	}

	@MappedName(4)
	public double getPosY() {
		return posY;
	}

	@MappedName(53)
	public void setPosY(double posY) {
		this.posY = posY;
	}

	@MappedName(5)
	public double getPosZ() {
		return posZ;
	}

	@MappedName(54)
	public void setPosZ(double posZ) {
		this.posZ = posZ;
	}

	@MappedName(55)
	public AxisAlignedBB getBoundingBox() {
		return axisAlignedBB;
	}

	@MappedName(56)
	public void setBoundingBox(AxisAlignedBB axisAlignedBB) {
		this.axisAlignedBB = axisAlignedBB;
	}

	@MappedName(57)
	public Block getBlock() {
		return block;
	}
	
}
