package dev.menace.module.modules.movement.flight.vanilla;

import dev.menace.event.events.EventCollide;
import dev.menace.module.modules.movement.flight.FlightBase;
import net.minecraft.block.BlockAir;
import net.minecraft.util.AxisAlignedBB;

public class FakeGroundFly  extends FlightBase {

	@Override
	public void onCollide(EventCollide event) {	
		if (event.getBlock() instanceof BlockAir && event.getPosY() <= this.launchY) {
			event.setBoundingBox(AxisAlignedBB.fromBounds(event.getPosX(), event.getPosY(), event.getPosZ(), event.getPosX() + 1.0, this.launchY, event.getPosZ() + 1.0));
		}
	}
	
}
