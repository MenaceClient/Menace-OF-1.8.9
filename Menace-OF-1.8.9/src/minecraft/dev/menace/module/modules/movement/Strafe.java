package dev.menace.module.modules.movement;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventPostMotionUpdate;
import dev.menace.module.Category;
import dev.menace.module.Module;
import dev.menace.utils.entity.self.PlayerUtils;

public class Strafe extends Module {

	public Strafe() {
		super("Strafe", 0, Category.MOVEMENT);
	}
	
	@EventTarget
	public void onPostMotion(EventPostMotionUpdate event) {
		PlayerUtils.strafe();
	}

}
