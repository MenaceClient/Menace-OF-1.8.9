package dev.menace.module.modules.movement;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventPreMotion;
import dev.menace.event.events.EventStep;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.settings.ListSetting;
import dev.menace.module.settings.SliderSetting;
import dev.menace.utils.player.PacketUtils;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import org.jetbrains.annotations.NotNull;

public class StepModule extends BaseModule {

	ListSetting mode;
	SliderSetting height;
	
	public StepModule() {
		super("Step", Category.MOVEMENT, 0);
	}
	
	@Override
	public void setup() {
		mode = new ListSetting("Mode", true, "Vanilla", new String[] {"Vanilla", "Verus", "NCP", "Spider"});
		height = new SliderSetting("Height", true, 1, 1, 10, true);
		this.rSetting(mode);
		this.rSetting(height);
		super.setup();
	}

	@EventTarget
	public void onUpdate(EventUpdate event) {
		this.setDisplayName(mode.getValue());
	}

	@EventTarget
	public void onPreMotion(EventPreMotion event) {
		if (mode.getValue().equalsIgnoreCase("Spider") && mc.thePlayer.isCollidedHorizontally) {
			mc.thePlayer.motionY = 0.1;
		}
	}
	
	@EventTarget
	public void onStep(@NotNull EventStep event) {
		if (mode.getValue().equalsIgnoreCase("Verus")) {
			PacketUtils.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, new ItemStack(Items.water_bucket), 0, 0.5f, 0));
			PacketUtils.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.5, mc.thePlayer.posZ), 1, new net.minecraft.item.ItemStack(Blocks.stone.getItem(mc.theWorld, new BlockPos(-1, -1, -1))), 0f, 0.94f, 0f));
		} else if (mode.getValue().equalsIgnoreCase("NCP") && canStep()) {
			if (event.getState() == EventStep.StepState.PRE) {
				event.setStepHeight(1.0f);
			} else {
				PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.42, mc.thePlayer.posZ, true));
				PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.753, mc.thePlayer.posZ, true));
			}
			return;
		} else if (mode.getValue().equalsIgnoreCase("Spider")) {
			return;
		}

		event.setStepHeight(height.getValueF());
	}

	private boolean canStep() {
		return this.mc.thePlayer.isCollidedVertically && this.mc.thePlayer.onGround && this.mc.thePlayer.motionY < 0.0 && !this.mc.thePlayer.movementInput.jump;
	}

}
