package dev.menace.module.modules.movement.flys;

import dev.menace.Menace;
import dev.menace.event.events.*;
import net.minecraft.client.Minecraft;

public abstract class FlightBase {
	
	public FlightBase() {
	}
	
	protected Minecraft MC = Menace.instance.MC;
	
	public double launchX, launchY, launchZ;
	
	public void onEnable() {}
	public void onDisable() {}

    public void onPreMotion(EventPreMotionUpdate event) {}
    public void onMotion(EventPostMotionUpdate event) {}
    public void onUpdate() {}
    public void onCollision(EventCollide event) {}
    public void onSendPacket(EventSendPacket event) {}
    public void onMove(EventMove event) {}
	public void onJump(EventJump event) {}
	public void onRender2D() {}
	public void onRecievePacket(EventReceivePacket event) {}
	public void onKey(EventKey event) {}

}
