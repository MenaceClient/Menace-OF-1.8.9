package dev.menace.module.modules.render;

import dev.menace.event.EventTarget;
import dev.menace.event.events.Event3D;
import dev.menace.module.Category;
import dev.menace.module.Module;
import dev.menace.utils.entity.self.PlayerUtils;
import dev.menace.utils.render.RenderUtils;
import net.minecraft.util.BlockPos;
import java.awt.Color;

public class PrevFallPos extends Module {

	public PrevFallPos() {
		super("PrevFallPos", 0, Category.RENDER);
	}
	
	@EventTarget
	public void on3D(Event3D event) {
		if (MC.thePlayer.isAirBorne && PlayerUtils.calculateGround() != 0.0f) {
			RenderUtils.drawBlockBox(new BlockPos(MC.thePlayer.posX, PlayerUtils.calculateGround() - 1, MC.thePlayer.posZ), Color.WHITE, true, true, 3f);
		}
	}

}
