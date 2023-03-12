package dev.menace.anticheat.check.checks;

import dev.menace.anticheat.PlayerVL;
import dev.menace.anticheat.check.BaseCheck;

public class SpeedCheck extends BaseCheck {
    public SpeedCheck() {
        super("Speed");
    }

    @Override
    public void update(PlayerVL vl) {
        if (Math.abs(vl.getPlayer().posX - vl.getPlayer().lastTickPosX) > 1 || Math.abs(vl.getPlayer().posZ - vl.getPlayer().lastTickPosZ) > 1) {
            vl.addVL(1, this.getCheckName());
        }
    }
}
