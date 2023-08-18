package dev.menace.module.modules.movement;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventPreMotion;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.settings.ListSetting;
import dev.menace.module.settings.SliderSetting;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.player.MovementUtils;
import dev.menace.utils.player.PacketUtils;
import dev.menace.utils.world.TimerHandler;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;

public class LongJumpModule extends BaseModule {
	public ListSetting mode;
	public SliderSetting speed;
	public SliderSetting height;
	private double moveSpeed;

	int count = 0;
	boolean disable;

	public LongJumpModule() {
		super("LongJump", "Jumps but long", Category.MOVEMENT, 0);
	}

	@Override
	public void setup() {
		mode = new ListSetting("Mode", true, "New NCP", new String[] {"NCP", "OldNCP", "Damage"});
		speed = new SliderSetting("Speed", true, 4, 1, 10, false) {
			@Override
			public void constantCheck() {
				setVisible(!mode.getValue().equals("New NCP"));
			}
		};
		height = new SliderSetting("Height", true, 1.4, 1, 4, false) {
			@Override
			public void constantCheck() {
				setVisible(mode.getValue().equals("Damage"));
			}
		};

		rSetting(mode);
		rSetting(speed);
		rSetting(height);
		super.setup();
	}

	@Override
	public void onEnable() {
		if (!mc.thePlayer.onGround) {
			ChatUtils.message("You must be on the ground to use this!");
			this.toggle();
			return;
		}

		// TODO: Damage Mode (Packet, Bow, Verus, etc).
		if(mode.getValue().equals("Damage")) {
			PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 3.001, mc.thePlayer.posZ, false));
			PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
		}

		count = 0;
		disable = false;
		moveSpeed = speed.getValue();
		super.onEnable();
	}

	@Override
	public void onDisable() {
		mc.thePlayer.speedInAir = 0.02f;
		TimerHandler.resetTimer();
		super.onDisable();
	}

	@EventTarget
	public void onPre(EventPreMotion event) {
		switch(mode.getValue()) {
			case "NCP":
				//TODO: Improve NCP Mode
				if (count == 0) {
					mc.thePlayer.jump();
					//mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.1, mc.thePlayer.posZ);
					//mc.thePlayer.motionY = 0.45;
					if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
						if (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() == 0) {
							MovementUtils.strafe(0.65f);
						} else if (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() == 1) {
							MovementUtils.strafe(0.75f);
						}
					} else {
						MovementUtils.strafe(0.525f);
					}
					count++;
				}

				if (count == 1) {
					mc.thePlayer.speedInAir = 0.025f;
				}

				if (mc.thePlayer.motionY < 0.0) {
					disable = true;
				}

				MovementUtils.strafe();
				break;
			case "OldNCP":
				if(mc.thePlayer.onGround)
					mc.thePlayer.jump();
				else
					disable = true;

				if(moveSpeed >= MovementUtils.getBaseMoveSpeed()) {
					moveSpeed -= moveSpeed / 70;
				}

				MovementUtils.strafe((float) moveSpeed);
				break;
			case "Damage":
				if(mc.thePlayer.hurtTime != 0) {
					if(mc.thePlayer.onGround) {
						mc.thePlayer.jump();
						mc.thePlayer.motionY *= height.getValue();
						MovementUtils.strafe((float) moveSpeed);
					} else {
						disable = true;
					}
					MovementUtils.strafe();
				}
				break;

		}
		if (mc.thePlayer.onGround && disable) {
			MovementUtils.stop();
			this.toggle();
		}
	}

}
