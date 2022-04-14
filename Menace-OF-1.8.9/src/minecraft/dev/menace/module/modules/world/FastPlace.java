package dev.menace.module.modules.world;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.Category;
import dev.menace.module.Module;

public class FastPlace extends Module {

	public FastPlace() {
		super("FastPlace", 0, Category.WORLD);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		MC.rightClickDelayTimer = 0;
	}

}
