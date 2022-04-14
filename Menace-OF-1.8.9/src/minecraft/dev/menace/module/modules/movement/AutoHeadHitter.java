package dev.menace.module.modules.movement;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventPreMotionUpdate;
import dev.menace.module.Category;
import dev.menace.module.Module;
import dev.menace.utils.world.BlockUtils;
import net.minecraft.util.BlockPos;

public class AutoHeadHitter extends Module {

	public AutoHeadHitter() {
		super("AutoHeadHitter", 0, Category.MOVEMENT);
	}
	
	@EventTarget
	public void onPreMotion(EventPreMotionUpdate event) {
		if (MC.thePlayer.onGround && BlockUtils.getBlock(new BlockPos(MC.thePlayer.posX, MC.thePlayer.posY + 2, MC.thePlayer.posZ)).isOpaqueCube()
				&& BlockUtils.getBlock(new BlockPos(MC.thePlayer.posX, MC.thePlayer.posY + 2, MC.thePlayer.posZ)).isFullBlock()) {
			MC.thePlayer.jump();
		}
	}

}
