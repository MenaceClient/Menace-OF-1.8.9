package dev.menace.module.modules.movement.speed.ncp;

import dev.menace.event.events.EventPreMotion;
import dev.menace.module.modules.movement.speed.SpeedBase;
import dev.menace.utils.player.MovementUtils;
import dev.menace.utils.world.TimerHandler;
import net.minecraft.potion.Potion;

public class NCPStrafeSpeed extends SpeedBase {

    @Override
    public void onPreMotion(EventPreMotion event) {

        if(!MovementUtils.shouldMove()) {
            return;
        }

        mc.gameSettings.keyBindJump.pressed = false;
        if(mc.thePlayer.onGround) {
            mc.thePlayer.jump();
            if(mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
                if (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() == 0) {
                    MovementUtils.strafe(0.5893f);
                } else if (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() == 1) {
                    MovementUtils.strafe(0.6893f);
                }
            } else {
                MovementUtils.strafe(0.485f);
            }
        }
        MovementUtils.strafe();

        super.onPreMotion(event);
    }
}
