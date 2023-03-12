package dev.menace.anticheat;

import dev.menace.Menace;
import dev.menace.anticheat.check.BaseCheck;
import dev.menace.anticheat.check.checks.*;
import dev.menace.utils.misc.ChatUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;

public class HackerDetect {

    public static ArrayList<BaseCheck> checks = new ArrayList<>();

    public static Minecraft mc = Minecraft.getMinecraft();

    //Checks
    NoFallCheck noFallCheck = new NoFallCheck();
    SpeedCheck speedCheck = new SpeedCheck();
    StepCheck stepCheck = new StepCheck();
    UnlegitRotationCheck unlegitRotationCheck = new UnlegitRotationCheck();
    VelocityCheck velocityCheck = new VelocityCheck();

    public static void updateChecks() {
        mc.theWorld.playerEntities.forEach(player -> {
            if (player != mc.thePlayer && !player.isDead && player.ticksExisted >= 40 && !Menace.instance.onlineMenaceUsers.containsValue(player.getName())) {
                checks.forEach(check -> check.update(player.playerVL));
            }
        });
    }

}
