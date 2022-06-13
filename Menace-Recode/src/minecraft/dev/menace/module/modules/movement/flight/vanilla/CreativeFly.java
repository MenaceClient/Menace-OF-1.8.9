package dev.menace.module.modules.movement.flight.vanilla;

import dev.menace.module.modules.movement.flight.FlightBase;

public class CreativeFly extends FlightBase {

	boolean allowFlying;
	
	@Override
	public void onEnable() {
		allowFlying = MC.thePlayer.capabilities.allowFlying;
		MC.thePlayer.capabilities.allowFlying = true;
		MC.thePlayer.capabilities.isFlying = true;
	}
	
	@Override
	public void onUpdate() {
		MC.thePlayer.capabilities.allowFlying = true;
		MC.thePlayer.capabilities.isFlying = true;
	}
	
	@Override
	public void onDisable() {
		MC.thePlayer.capabilities.allowFlying = allowFlying;
		MC.thePlayer.capabilities.isFlying = false;
	}
	
}
