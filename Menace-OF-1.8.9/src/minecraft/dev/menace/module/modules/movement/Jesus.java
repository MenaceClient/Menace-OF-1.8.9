package dev.menace.module.modules.movement;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventCollide;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.Category;
import dev.menace.module.Module;
import dev.menace.utils.entity.self.PlayerUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.AxisAlignedBB;

public class Jesus extends Module {

    final float ascensionValue = 0.06000000238418583F; // Fastest legit water ascension value
	
	public Jesus() {
		super("Jesus", 0, Category.MOVEMENT);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
        if (PlayerUtils.isInLiquid() || (MC.thePlayer.movementInput.jump && PlayerUtils.isOnLiquid()))
            MC.thePlayer.motionY = ascensionValue;
	}
	
	@EventTarget
	public void onCollode(EventCollide event) {
        if (MC.thePlayer.movementInput == null || MC.thePlayer.movementInput.sneak || MC.thePlayer.movementInput.jump)
            return;

        final Block block = event.getBlock();
        
        if (block instanceof BlockLiquid && !PlayerUtils.isInLiquid())
            event.setBoundingBox(AxisAlignedBB.fromBounds(event.getPosX(), event.getPosY(), event.getPosZ(), event.getPosX() + 1, event.getPosY() + 1, event.getPosZ() + 1));
	}

}
