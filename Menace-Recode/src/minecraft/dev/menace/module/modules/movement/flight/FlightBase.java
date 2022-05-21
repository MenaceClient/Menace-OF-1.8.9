package dev.menace.module.modules.movement.flight;

import dev.menace.event.events.EventMove;
import net.minecraft.client.Minecraft;

public class FlightBase {

	protected Minecraft MC = Minecraft.getMinecraft();
	
	public void onEnable() {}
	public void onDisable() {}
	public void onUpdate() {}
	public void onMove(EventMove event) {}
	
}
