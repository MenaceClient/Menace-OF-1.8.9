package dev.menace.module.modules.movement.flight.vanilla;

import dev.menace.module.modules.movement.flight.FlightBase;

public class CreativeFly extends FlightBase {

	boolean allowFlying;
	
	@Override
	public void onEnable() {
		allowFlying = mc.thePlayer.capabilities.allowFlying;
		mc.thePlayer.capabilities.allowFlying = true;
		mc.thePlayer.capabilities.isFlying = true;
	}
	
	@Override
	public void onUpdate() {
		mc.thePlayer.capabilities.allowFlying = true;
		mc.thePlayer.capabilities.isFlying = true;
	}
	
	@Override
	public void onDisable() {
		mc.thePlayer.capabilities.allowFlying = allowFlying;
		mc.thePlayer.capabilities.isFlying = false;
	}
	
}
