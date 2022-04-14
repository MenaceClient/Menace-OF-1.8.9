package dev.menace.module.modules.world;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.Category;
import dev.menace.module.Module;
import dev.menace.module.settings.DoubleSetting;

public class Timer extends Module {

	//Settings
	DoubleSetting speed;
	
	public Timer() {
		super("Timer", 0, Category.WORLD);
	}
	
	@Override
	public void setup() {
		speed = new DoubleSetting("Speed", this, 1, 0.1, 5);
		this.rSetting(speed);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		MC.timer.timerSpeed = (float) speed.getValue();
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
		
		MC.timer.timerSpeed = 1;
	}

}
