package dev.menace.module.modules.movement;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventPreMotion;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.player.MovementUtils;
import dev.menace.utils.player.PlayerUtils;
import net.minecraft.potion.Potion;

public class LongJumpModule extends BaseModule {

	int count = 0;
	boolean disable;
	
	public LongJumpModule() {
		super("LongJump", Category.MOVEMENT, 0);
	}

	@Override
	public void onEnable() {

		if (!mc.thePlayer.onGround) {
			ChatUtils.message("You must be on the ground to use this!");
			this.toggle();
			return;
		}

		count = 0;
		disable = false;
		super.onEnable();
	}

	@EventTarget
	public void onPre(EventPreMotion event) {

		if (count == 0) {
			mc.thePlayer.jump();
			mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.1, mc.thePlayer.posZ);
			mc.thePlayer.motionY = 0.45;
			if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
				if (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() == 0) {
					MovementUtils.strafe(0.5893f);
				} else if (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() == 1) {
					MovementUtils.strafe(0.67f);
				}
			} else {
				MovementUtils.strafe(0.525f);
			}
			count++;
		}

		if (mc.thePlayer.motionY < 0.0 && count >= 1) {
			ChatUtils.message("MotionY: " + mc.thePlayer.motionY);

			//TODO: Make a better glide
			mc.thePlayer.motionY = (Math.round(mc.thePlayer.motionY * 100.0) / 100.0);

			ChatUtils.message("New MotionY: " + mc.thePlayer.motionY);
			count++;
			disable = true;
		}

		MovementUtils.strafe();

		if (mc.thePlayer.onGround && disable) {
			MovementUtils.stop();
			this.toggle();
		}
	}

}
