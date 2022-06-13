package dev.menace.module.modules.world;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.settings.SliderSetting;

public class TimerModule extends BaseModule {

	SliderSetting speed;
	
	public TimerModule() {
		super("Timer", "Changes the world speed.", Category.WORLD, 0);
	}
	
	@Override
	public void setup() {
		speed = new SliderSetting("Speed", true, 1, 0.1, 10, 0.5, false);
		this.rSetting(speed);
		super.setup();
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		MC.timer.timerSpeed = speed.getValueF();
	}
	
	@Override
	public void onDisable() {
		MC.timer.timerSpeed = 1;
		super.onDisable();
	}

}
