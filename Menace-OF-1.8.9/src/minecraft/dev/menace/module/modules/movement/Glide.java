package dev.menace.module.modules.movement;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.Category;
import dev.menace.module.Module;

public class Glide extends Module {

	public Glide() {
		super("Glide", 0, Category.MOVEMENT);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (!MC.thePlayer.isJumping && !Menace.instance.moduleManager.flightModule.isToggled()) {
			MC.thePlayer.motionY = -0.0784000015258789;
		}
	}

}
