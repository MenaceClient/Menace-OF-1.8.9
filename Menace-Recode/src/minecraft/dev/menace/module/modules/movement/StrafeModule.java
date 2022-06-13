package dev.menace.module.modules.movement;

import org.lwjgl.input.Keyboard;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.utils.player.MovementUtils;

public class StrafeModule extends BaseModule {

	public StrafeModule() {
		super("Strafe", Category.MOVEMENT, 0);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (MovementUtils.isMoving()) {
			MovementUtils.strafe();
		}
	}
	
}
