package dev.menace.anticheat.check.checks;

import dev.menace.anticheat.PlayerVL;
import dev.menace.anticheat.check.BaseCheck;
import net.minecraft.entity.player.EntityPlayer;

public class StepCheck extends BaseCheck {

    public StepCheck() {
        super("Step");
    }

    @Override
    public void update(PlayerVL vl) {
        if (vl.getPlayer().stepHeight > 0.5f) {
            vl.addVL(1, this.getCheckName());
        }
    }
}
