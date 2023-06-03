package dev.menace.module.modules.movement.speed.ncp;

import dev.menace.event.events.EventPreMotion;
import dev.menace.module.modules.movement.speed.SpeedBase;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.player.MovementUtils;
import net.minecraft.potion.Potion;

public class NCPLowHopSpeed extends SpeedBase {

    @Override
    public void onPreMotion(EventPreMotion event) {
        //Method by Exterminate (Lowest lowhop ever, trust)
        if (!MovementUtils.shouldMove()) return;

        //Anti Retard
        mc.gameSettings.keyBindJump.pressed = false;

        if (mc.thePlayer.onGround) {
            mc.timer.timerSpeed = 0.95f;
            mc.thePlayer.jump();
            if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
                if (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() == 0) {
                    MovementUtils.strafe(0.58f);
                } else if (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() == 1) {
                    MovementUtils.strafe(0.67f);
                }
            } else {
                MovementUtils.strafe(0.485f);
            }
        } else if (mc.thePlayer.motionY < 0.16 && mc.thePlayer.motionY > 0.0) {
            mc.thePlayer.motionY = -0.1;
        } else if (mc.thePlayer.motionY < 0.0 && mc.thePlayer.motionY > -0.3) {
            mc.timer.timerSpeed = 1.2F;
        }
        MovementUtils.strafe();

        super.onPreMotion(event);
    }
}
