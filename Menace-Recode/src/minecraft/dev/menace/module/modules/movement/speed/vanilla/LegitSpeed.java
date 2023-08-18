package dev.menace.module.modules.movement.speed.vanilla;

import dev.menace.module.modules.movement.speed.SpeedBase;
import dev.menace.utils.player.MovementUtils;

public class LegitSpeed extends SpeedBase {

    @Override
    public void onUpdate() {
        if (mc.thePlayer.onGround && MovementUtils.shouldMove()) {
            mc.thePlayer.jump();
        }
        super.onUpdate();
    }
}
