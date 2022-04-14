package dev.menace.module.modules.movement;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.Category;
import dev.menace.module.Module;

public class InfJump extends Module {

	public InfJump() {
		super("InfJump", 0, Category.MOVEMENT);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		MC.thePlayer.jumpTicks = 0;
	}

}
