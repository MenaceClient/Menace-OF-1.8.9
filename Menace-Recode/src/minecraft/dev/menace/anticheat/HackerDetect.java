package dev.menace.anticheat;

import dev.menace.anticheat.check.BaseCheck;
import dev.menace.anticheat.check.checks.*;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;

public class HackerDetect {

    public static ArrayList<BaseCheck> checks = new ArrayList<>();

    public static Minecraft mc = Minecraft.getMinecraft();

    //Checks
    NoFallCheck noFallCheck = new NoFallCheck();
    StepCheck stepCheck = new StepCheck();
    UnlegitRotationCheck unlegitRotationCheck = new UnlegitRotationCheck();

    public static void updateChecks() {
        for (BaseCheck check : checks) {
            mc.theWorld.playerEntities.forEach(player -> {
                if (player == null || player == mc.thePlayer || player.isDead) return;

                check.update(player);
            });
        }
    }

}
