package dev.menace.module.modules.movement.speed.other;

import dev.menace.event.events.EventPreMotion;
import dev.menace.module.modules.movement.speed.SpeedBase;
import dev.menace.utils.player.MovementUtils;
import net.minecraft.potion.Potion;

public class BlocksMCSpeed extends SpeedBase {

    @Override
    public void onPreMotion(EventPreMotion event) {

        if(!MovementUtils.shouldMove()) {
            mc.timer.timerSpeed = 1.0f;
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
