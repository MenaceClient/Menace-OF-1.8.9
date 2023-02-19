package dev.menace.anticheat.check.checks;

import dev.menace.anticheat.check.BaseCheck;
import net.minecraft.entity.player.EntityPlayer;

public class StepCheck extends BaseCheck {

    public StepCheck() {
        super("Step");
    }

    @Override
    public void update(EntityPlayer player) {
        if (player.stepHeight > 0.5f) {
            flag(player);
        }
    }
}
