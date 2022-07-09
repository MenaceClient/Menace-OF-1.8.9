package dev.menace.module.modules.movement.flight;

import dev.menace.event.events.*;
import net.minecraft.client.Minecraft;

public class FlightBase {

	public double launchX, launchY, launchZ;
	public Minecraft mc = Minecraft.getMinecraft();
	
	public void onEnable() {}
	public void onDisable() {}
	public void onUpdate() {}
	public void onCollide(EventCollide event) {}
	public void onMove(EventMove event) {}
	public void onPreMotion(EventPreMotion event) {}
	public void onPostMotion(EventPostMotion event) {}
	public void onSendPacket(EventSendPacket event) {}
	public void onReceivePacket(EventReceivePacket event) {}
	public void onJump(EventJump event) {}
	
}
