package dev.menace.anticheat.check.checks;

import dev.menace.anticheat.PlayerVL;
import dev.menace.anticheat.check.BaseCheck;
import dev.menace.utils.player.PlayerUtils;
import net.minecraft.entity.player.EntityPlayer;

public class NoFallCheck extends BaseCheck {

    public NoFallCheck() {
        super("NoFall");
    }

    @Override
    public void update(PlayerVL vl) {
        EntityPlayer player = vl.getPlayer();

        //Update Real Fall Distance
        if (player.posY < player.lastTickPosY) {
            vl.realFallDistance += player.lastTickPosY - player.posY;
        }

        if (vl.realFallDistance > 4f && (player.onGround || PlayerUtils.isOnGround(player)) && player.hurtTime == 0) {
            vl.addVL(1, this.getCheckName() + " (A)");
        }

        if (PlayerUtils.isOnGround(player) || player.isInWeb || player.isInWater()) {
            vl.realFallDistance = 0;
        }
    }
}
