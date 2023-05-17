package dev.menace.module.modules.movement.flight.ncp;

import dev.menace.Menace;
import dev.menace.event.events.EventPreMotion;
import dev.menace.module.modules.movement.flight.FlightBase;
import dev.menace.utils.player.MovementUtils;

public class OldNCPFly extends FlightBase {
    private float moveSpeed;
    private boolean jumped;

    @Override
    public void onEnable() {
        if(mc.thePlayer.onGround) {
            mc.thePlayer.jump();
            moveSpeed = (float) Menace.instance.moduleManager.flightModule.speed.getValue() / 3;
        } else
            moveSpeed = 0.25F;
    }

    @Override
    public void onDisable() {
        moveSpeed = 0.25F;
        MovementUtils.strafe(moveSpeed);
        jumped = false;
    }

    @Override
    public void onPreMotion(EventPreMotion event) {
        if(!mc.thePlayer.onGround && !jumped && mc.thePlayer.fallDistance <= 0)
            jumped = true;

        if(mc.thePlayer.isCollidedHorizontally || !MovementUtils.isMoving() && jumped)
            moveSpeed = 0.25F;

        if(jumped) {
            event.setPosY(mc.thePlayer.posY -0.0000000010);

            mc.thePlayer.motionY = 0;

            if(moveSpeed > 0.25)
                moveSpeed = moveSpeed - moveSpeed / 119;

            MovementUtils.strafe(moveSpeed);
        }
    }

}
