package dev.menace.module.modules.movement.speed.vanilla;

import dev.menace.Menace;
import dev.menace.event.events.EventPreMotion;
import dev.menace.module.modules.movement.speed.SpeedBase;
import dev.menace.utils.player.MovementUtils;

public class BhopSpeed extends SpeedBase {

    @Override
    public void onPreMotion(EventPreMotion event) {
        if (!MovementUtils.shouldMove()) return;
        if (mc.thePlayer.onGround) {
            mc.thePlayer.jump();
            MovementUtils.strafe(Menace.instance.moduleManager.speedModule.speed.getValueF() / 2);
        }
        MovementUtils.strafe();
    }
}
