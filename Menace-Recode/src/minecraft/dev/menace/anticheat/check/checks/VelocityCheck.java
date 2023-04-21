package dev.menace.anticheat.check.checks;

import dev.menace.anticheat.PlayerVL;
import dev.menace.anticheat.check.BaseCheck;

public class VelocityCheck extends BaseCheck {

    public VelocityCheck() {
        super("Velocity");
    }

    @Override
    public void update(PlayerVL vl) {
        if (vl.getPlayer().hurtTime > 10 && vl.getPlayer().lastTickPosX == vl.getPlayer().posX && vl.getPlayer().posZ == vl.getPlayer().lastTickPosZ
                && !mc.theWorld.checkBlockCollision(vl.getPlayer().boundingBox.expand(0.05, 0, 0.05))) {
            vl.addVL(1, this.getCheckName());
        }
    }
}
