package dev.menace.module.modules.movement;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventPreMotion;
import dev.menace.event.events.EventStep;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.settings.ListSetting;
import dev.menace.module.settings.SliderSetting;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.player.PacketUtils;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockPos;
import org.jetbrains.annotations.NotNull;

public class StepModule extends BaseModule {

	ListSetting mode;
	SliderSetting height;

	//NCP safety switch
	int ncpTicks = 0;
	int stepAmt = 0;

	//MotionStep
	int motionStepState = 0;

	public StepModule() {
		super("Step", Category.MOVEMENT, 0);
	}

	@Override
	public void setup() {
		//matrix is just jumping then setting motionY to 0 after colliding <-- Thx hentajj
		mode = new ListSetting("Mode", true, "Vanilla", new String[] {"Vanilla", "Verus", "OldNCP", "NCP", "Motion", "Spider"});
		height = new SliderSetting("Height", true, 1, 1, 10, 0.5, false);
		this.rSetting(mode);
		this.rSetting(height);
		super.setup();
	}

	@Override
	public void onEnable() {
		ncpTicks = 0;
		stepAmt = 0;
		motionStepState = 0;
		super.onEnable();
	}

	@EventTarget
	public void onUpdate(EventUpdate event) {
		ncpTicks++;
		if (ncpTicks >= 20) {
			ncpTicks = 0;
			stepAmt = 0;
		}
		this.setDisplayName(mode.getValue());
	}

	@EventTarget
	public void onPreMotion(EventPreMotion event) {
		if (mode.getValue().equalsIgnoreCase("Spider") && mc.thePlayer.isCollidedHorizontally) {
			mc.thePlayer.motionY = 0.1;
		} else if (mode.getValue().equalsIgnoreCase("Motion")) {
			if (motionStepState == 1) {
				mc.thePlayer.jump();
				motionStepState = 2;
			} else if (motionStepState == 2 && !mc.thePlayer.isCollidedHorizontally) {
				mc.thePlayer.motionY = 0;
				motionStepState = 0;
			}
		}
	}

	@EventTarget
	public void onStep(EventStep event) {
		if (mode.getValue().equalsIgnoreCase("Verus")) {
			PacketUtils.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, new ItemStack(Items.water_bucket), 0, 0.5f, 0));
			PacketUtils.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.5, mc.thePlayer.posZ), 1, new net.minecraft.item.ItemStack(Blocks.stone.getItem(mc.theWorld, new BlockPos(-1, -1, -1))), 0f, 0.94f, 0f));
		} else if (mode.getValue().equalsIgnoreCase("OldNCP") || mode.getValue().equalsIgnoreCase("NCP")) {
			if (!canStep()) return;

			if (event.getState() == EventStep.StepState.PRE) {
				event.setStepHeight(mode.getValue().equalsIgnoreCase("NCP") ? 1f : Math.min(height.getValueF(), 2));
			} else {
				if (event.getStepHeight() < 1) return;

				double[] packets = new double[0];
				if (event.getStepHeight() == 1) {
					packets = new double[] {0.41999998688698, 0.7531999805212};
				} else if (event.getStepHeight() == 1.5) {
					packets = new double[] {0.4, 0.75, 0.5, 0.41, 0.83, 1.16, 1.41999998688698};
					//packets = new double[] {0.41999998688698, 0.7531999805212, 0.5, 0.39, 0.69, 0.83, 0.51, 1.16, 1.41999998688698};
				} else if (event.getStepHeight() == 2) {
					packets = new double[] {0.4, 0.75, 0.5, 0.41, 0.83, 1.16, 1.41999998688698, 1.57, 1.58, 1.42};
				}

				fakeJump();
				for (double offset : packets) {
					PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + offset, mc.thePlayer.posZ, true));
				}

				if (mode.getValue().equalsIgnoreCase("NCP")) {
					PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + event.getStepHeight(), mc.thePlayer.posZ, true));
					stepAmt++;
					if (stepAmt == 1) {
						ncpTicks = 0;
					}
				}
			}
			return;
		} else if (mode.getValue().equalsIgnoreCase("Spider")) {
			return;
		} else if (mode.getValue().equalsIgnoreCase("Motion")) {
			if (event.getState() == EventStep.StepState.PRE && canStep()) {
				motionStepState = 1;
			}
			return;
		}

		event.setStepHeight(height.getValueF());
	}

	private boolean canStep() {
		return this.mc.thePlayer.isCollidedVertically && this.mc.thePlayer.onGround && this.mc.thePlayer.motionY < 0.0 && !this.mc.thePlayer.movementInput.jump && stepAmt < 4;
	}

	private void fakeJump() {
		mc.thePlayer.isAirBorne = true;
		mc.thePlayer.triggerAchievement(StatList.jumpStat);
	}

}
