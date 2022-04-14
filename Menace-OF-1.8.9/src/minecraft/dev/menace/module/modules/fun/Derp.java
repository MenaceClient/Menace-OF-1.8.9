package dev.menace.module.modules.fun;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventPreMotionUpdate;
import dev.menace.module.Category;
import dev.menace.module.Module;
import dev.menace.module.settings.BoolSetting;
import dev.menace.module.settings.DoubleSetting;

public class Derp extends Module {

	private double serverYaw;
	private int i;
	
	//Settings
	BoolSetting helicopter;
	BoolSetting headless;
	BoolSetting twerk;
	DoubleSetting speed;
	
	public Derp() {
		super("Derp", 0, Category.FUN);
	}
	
	@Override
	public void setup() {
		helicopter = new BoolSetting("Helicopter", this, true);
		headless = new BoolSetting("Headless", this, false);
		twerk = new BoolSetting("Twerk", this, false);
		speed = new DoubleSetting("Speed", this, 10, 1, 170);
		this.rSetting(helicopter);
		this.rSetting(headless);
		this.rSetting(twerk);
		this.rSetting(speed);
	}

	@EventTarget
	public void onUpdate(EventPreMotionUpdate event) {
		if (helicopter.isChecked()) {
			this.serverYaw += speed.getValue();
			event.setYaw((float)this.serverYaw);
			MC.thePlayer.rotationYawHead = (float)this.serverYaw;
			MC.thePlayer.renderYawOffset = (float)this.serverYaw;
		}
		if (twerk.isChecked()) {
			MC.gameSettings.keyBindSneak.pressed = true;
		}
		if (headless.isChecked()) {
			event.setPitch(180.0f);
		}
		else if (!helicopter.isChecked() && !headless.isChecked() && !twerk.isChecked()) {
			final float random = (float)(Math.random() * 360.0);
			event.setYaw(random);
			event.setPitch(random);
			MC.thePlayer.rotationYawHead = random;
			MC.thePlayer.renderYawOffset = random;
		}

	}

}
