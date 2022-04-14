package dev.menace.module.modules.combat;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventLeftClick;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.Category;
import dev.menace.module.Module;
import dev.menace.module.settings.BoolSetting;
import dev.menace.module.settings.DoubleSetting;
import dev.menace.utils.misc.ChatUtils;

public class AutoClicker extends Module {

	//Settings
	DoubleSetting delay;
	BoolSetting rclick;
	
	public AutoClicker() {
		super("AutoClicker", 0, Category.COMBAT);
	}
	
	@Override
	public void setup() {
		delay = new DoubleSetting("Delay", this, 1, 1, 20);
		rclick = new BoolSetting("Right Click", this, false);
        this.rSetting(delay);
        this.rSetting(rclick);
	}
	
	@EventTarget
	private void onUpdate(EventUpdate event) {
		if (MC.gameSettings.keyBindPickBlock.isKeyDown() && MC.thePlayer.ticksExisted % delay.getValue() == 0.0) {
			MC.clickMouse();
		}
		else if (rclick.isChecked() && MC.gameSettings.keyBindDrop.isKeyDown() && MC.thePlayer.ticksExisted % delay.getValue() == 0.0) {	
			MC.rightClickMouse();
		}
	}
}
