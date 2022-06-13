package dev.menace.module.modules.movement;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventStep;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.settings.SliderSetting;

public class StepModule extends BaseModule {

	SliderSetting height;
	
	public StepModule() {
		super("Step", Category.MOVEMENT, 0);
	}
	
	@Override
	public void setup() {
		height = new SliderSetting("Height", true, 1, 1, 10, true);
		this.rSetting(height);
		super.setup();
	}
	
	@EventTarget
	public void onStep(EventStep event) {
		event.setStepHeight(height.getValueF());
	}

}
