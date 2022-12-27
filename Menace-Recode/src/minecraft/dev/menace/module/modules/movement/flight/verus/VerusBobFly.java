package dev.menace.module.modules.movement.flight.verus;

import dev.menace.event.events.EventCollide;
import dev.menace.event.events.EventMove;
import dev.menace.module.modules.movement.flight.FlightBase;
import dev.menace.utils.player.MovementUtils;
import net.minecraft.block.BlockAir;
import net.minecraft.util.AxisAlignedBB;

public class VerusBobFly extends FlightBase {

    @Override
    public void onMove(EventMove event) {
        if (mc.thePlayer.ticksExisted % 10 == 0 && mc.thePlayer.onGround) {
            MovementUtils.strafe(0.6f);
            event.setY(0.5);
            mc.thePlayer.motionY = 0;
        }
    }

    @Override
    public void onCollide(EventCollide event) {
        if (event.getBlock() instanceof BlockAir && event.getPosY() <= this.launchY) {
            event.setBoundingBox(AxisAlignedBB.fromBounds(event.getPosX(), event.getPosY(), event.getPosZ(), event.getPosX() + 1.0, this.launchY, event.getPosZ() + 1.0));
        }
    }

}
