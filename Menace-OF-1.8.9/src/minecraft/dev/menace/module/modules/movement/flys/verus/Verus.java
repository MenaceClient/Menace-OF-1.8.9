package dev.menace.module.modules.movement.flys.verus;

import dev.menace.event.events.EventCollide;
import dev.menace.event.events.EventJump;
import dev.menace.module.modules.movement.flys.FlightBase;
import dev.menace.utils.entity.self.PlayerUtils;
import net.minecraft.block.BlockAir;
import net.minecraft.util.AxisAlignedBB;

public class Verus extends FlightBase {

	@Override
	public void onUpdate() {
		MC.gameSettings.keyBindJump.pressed = false;
        if (MC.thePlayer.onGround) {
            MC.thePlayer.jump();
            PlayerUtils.strafe(0.48F);
        } else {
        	PlayerUtils.strafe();
        }
	}
	
	@Override
    public void onCollision(EventCollide event) {
        if (event.getBlock() instanceof BlockAir && event.getPosY() <= this.launchY) {
            event.setBoundingBox(AxisAlignedBB.fromBounds(event.getPosX(), event.getPosY(), event.getPosZ(), event.getPosX() + 1.0, this.launchY, event.getPosZ() + 1.0));
        }
	}
	
}
