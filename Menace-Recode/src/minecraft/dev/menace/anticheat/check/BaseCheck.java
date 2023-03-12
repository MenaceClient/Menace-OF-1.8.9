package dev.menace.anticheat.check;

import dev.menace.anticheat.HackerDetect;
import dev.menace.anticheat.PlayerVL;
import dev.menace.utils.misc.ChatUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class BaseCheck {

    {
        HackerDetect.checks.add(this);
    }

    String name;
    protected Minecraft mc = Minecraft.getMinecraft();

    public BaseCheck(String name) {
        this.name = name;
    }

    public void update(PlayerVL vl) {}

    public String getCheckName() {
        return name;
    }
}
