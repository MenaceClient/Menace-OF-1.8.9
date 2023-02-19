package dev.menace.anticheat.check.checks;

import dev.menace.anticheat.check.BaseCheck;
import net.minecraft.entity.player.EntityPlayer;

public class UnlegitRotationCheck extends BaseCheck {
    public UnlegitRotationCheck() {
        super("UnlegitRotation");
    }

    @Override
    public void update(EntityPlayer player) {
        if (player.rotationPitch > 90 || player.rotationPitch < -90) {
            flag(player);
        }
    }
}
