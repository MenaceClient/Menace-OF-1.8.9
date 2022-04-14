package dev.menace.module.modules.world;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventCollide;
import dev.menace.module.Category;
import dev.menace.module.Module;
import net.minecraft.block.BlockCactus;
import net.minecraft.util.AxisAlignedBB;

public class AntiCactus extends Module {

	public AntiCactus() {
		super("AntiCactus", 0, Category.WORLD);
	}

	@EventTarget
	public void onCollision(EventCollide event) {
		if (event.getBlock() instanceof BlockCactus)
            event.setBoundingBox(new AxisAlignedBB(event.getPosX(), event.getPosY(), event.getPosZ(),
                    event.getPosX() + 1.0, event.getPosY() + 1.0, event.getPosZ() + 1.0));
	}
	
}
