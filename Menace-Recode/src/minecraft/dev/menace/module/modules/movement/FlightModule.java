package dev.menace.module.modules.movement;

import org.lwjgl.input.Keyboard;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.Category;
import dev.menace.module.modules.movement.flight.FlightBase;
import dev.menace.module.modules.movement.flight.vanilla.CreativeFly;
import dev.menace.module.BaseModule;

public class FlightModule extends BaseModule {
	
	public FlightBase flightMode = new CreativeFly();
	
	public FlightModule() {
		super("Flight", "Fly like a bird!", Category.MOVEMENT, Keyboard.KEY_F);
	}
	
	@Override
	public void onEnable() {
		flightMode.onEnable();
		super.onEnable();
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		flightMode.onUpdate();
	}
	
	@Override
	public void onDisable() {
		flightMode.onDisable();
		super.onDisable();
	}

}
