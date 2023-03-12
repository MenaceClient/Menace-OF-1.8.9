package dev.menace.anticheat.check.checks;

import dev.menace.anticheat.PlayerVL;
import dev.menace.anticheat.check.BaseCheck;
import net.minecraft.entity.player.EntityPlayer;

public class NoFallCheck extends BaseCheck {

    public NoFallCheck() {
        super("NoFall");
    }

    @Override
    public void update(PlayerVL vl) {
        EntityPlayer player = vl.getPlayer();
        if (player.fallDistance > 3.5f && player.onGround && player.hurtTime == 0) {
            vl.addVL(1, this.getCheckName());
        }
    }
}
