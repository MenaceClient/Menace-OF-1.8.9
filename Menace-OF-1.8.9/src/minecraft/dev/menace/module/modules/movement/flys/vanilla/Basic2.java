package dev.menace.module.modules.movement.flys.vanilla;

import dev.menace.event.events.EventCollide;
import dev.menace.event.events.EventMove;
import dev.menace.module.modules.movement.flys.FlightBase;
import dev.menace.utils.misc.MSTimer;
import net.minecraft.block.BlockAir;
import net.minecraft.util.AxisAlignedBB;

public class Basic2 extends FlightBase {
	@Override
	public void onCollision(EventCollide event) {	
		if (event.getBlock() instanceof BlockAir && event.getPosY() <= this.launchY) {
			event.setBoundingBox(AxisAlignedBB.fromBounds(event.getPosX(), event.getPosY(), event.getPosZ(), event.getPosX() + 1.0, this.launchY, event.getPosZ() + 1.0));
		}
	}
}
