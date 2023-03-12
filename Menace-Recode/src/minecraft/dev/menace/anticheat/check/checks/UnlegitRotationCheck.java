package dev.menace.anticheat.check.checks;

import dev.menace.anticheat.PlayerVL;
import dev.menace.anticheat.check.BaseCheck;
import net.minecraft.entity.player.EntityPlayer;

public class UnlegitRotationCheck extends BaseCheck {
    public UnlegitRotationCheck() {
        super("UnlegitRotation");
    }

    @Override
    public void update(PlayerVL vl) {
        if (vl.getPlayer().rotationPitch > 90 || vl.getPlayer().rotationPitch < -90) {
            vl.addVL(1, this.getCheckName());
        }
    }
}
