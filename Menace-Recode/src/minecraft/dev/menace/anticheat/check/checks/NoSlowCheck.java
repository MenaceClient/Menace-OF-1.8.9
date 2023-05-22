package dev.menace.anticheat.check.checks;

import dev.menace.anticheat.PlayerVL;
import dev.menace.anticheat.check.BaseCheck;
import dev.menace.utils.player.MovementUtils;

public class NoSlowCheck extends BaseCheck {
    public NoSlowCheck() {
        super("NoSlow");
    }

    @Override
    public void update(PlayerVL vl) {

        if (vl.getPlayer().isBlocking()) {
            vl.blockingTicks++;
        } else {
            vl.blockingTicks = 0;
        }

        if (vl.getPlayer().isBlocking() && vl.getPlayer().isSprinting() && vl.blockingTicks > 5 && MovementUtils.getSpeed(vl.getPlayer()) > 0.05 && !vl.getPlayer().isRiding()) {
            vl.addVL(1, this.getCheckName());
        }
    }
}
