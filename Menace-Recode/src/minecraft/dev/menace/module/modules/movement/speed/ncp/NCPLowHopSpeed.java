package dev.menace.module.modules.movement.speed.ncp;

import dev.menace.Menace;
import dev.menace.event.events.EventPreMotion;
import dev.menace.module.modules.movement.speed.SpeedBase;
import dev.menace.utils.player.MovementUtils;
import dev.menace.utils.player.PacketBalanceUtils;
import dev.menace.utils.world.TimerHandler;
import net.minecraft.potion.Potion;

public class NCPLowHopSpeed extends SpeedBase {

    @Override
    public void onPreMotion(EventPreMotion event) {
        //Method by Exterminate (Lowest lowhop ever, trust)
        if (!MovementUtils.shouldMove()) return;

        //Anti Retard
        mc.gameSettings.keyBindJump.pressed = false;

        //Timer balance
        if (Menace.instance.moduleManager.speedModule.timerMode.getValue().equalsIgnoreCase("PacketBalance")) {
            if (PacketBalanceUtils.instance.getBalance() > 150 || mc.timer.timerSpeed == 1f) {
                TimerHandler.setTimer(0.3f, 10);
            } else if (PacketBalanceUtils.instance.getBalance() < -400) {
                TimerHandler.setTimer(1.3f, 10);
            }
        }

        if (mc.thePlayer.onGround) {
            if (Menace.instance.moduleManager.speedModule.timerMode.getValue().equalsIgnoreCase("Standard")) {
                TimerHandler.setTimer(0.95f, 10);
            }
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
        } else if (mc.thePlayer.motionY < 0.0 && mc.thePlayer.motionY > -0.3 && Menace.instance.moduleManager.speedModule.timerMode.getValue().equalsIgnoreCase("Standard")) {
            TimerHandler.setTimer(1.2f, 10);
        }
        MovementUtils.strafe();

        super.onPreMotion(event);
    }
}
