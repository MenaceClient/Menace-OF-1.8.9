package dev.menace.module.modules.movement.flight.vanilla;

import dev.menace.Menace;
import dev.menace.event.events.EventCollide;
import dev.menace.event.events.EventJump;
import dev.menace.module.modules.movement.flight.FlightBase;
import dev.menace.utils.player.MovementUtils;
import net.minecraft.block.BlockAir;
import net.minecraft.util.AxisAlignedBB;

public class FakeGroundFly  extends FlightBase {

	@Override
	public void onUpdate() {
		if(MovementUtils.shouldMove()) {
			MovementUtils.strafe(Menace.instance.moduleManager.flightModule.speed.getValueF() / 5);
		}
	}

	@Override
	public void onCollide(EventCollide event) {	
		if (event.getBlock() instanceof BlockAir && event.getPosY() <= this.launchY) {
			event.setBoundingBox(AxisAlignedBB.fromBounds(event.getPosX(), event.getPosY(), event.getPosZ(), event.getPosX() + 1.0, this.launchY, event.getPosZ() + 1.0));
		}
	}

	/*@Override
	public void onJump(EventJump event) {
		event.cancel();
	}*/
	
}
