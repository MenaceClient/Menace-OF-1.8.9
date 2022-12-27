package dev.menace.module.modules.player;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.settings.ListSetting;
import dev.menace.utils.player.MovementUtils;
import dev.menace.utils.player.PacketUtils;
import dev.menace.utils.player.PlayerUtils;
import dev.menace.utils.world.BlockUtils;
import net.minecraft.network.play.client.C03PacketPlayer;

import java.sql.Blob;

public class AntiVoidModule extends BaseModule {

    private double lastOnGroundX;
    private double lastOnGroundY;
    private double lastOnGroundZ;

    ListSetting mode;

    public AntiVoidModule() {
        super("AntiVoid", Category.PLAYER, 0);
    }

    @Override
    public void setup() {
        mode = new ListSetting("Mode", true, "Teleport", new String[] {"Teleport", "Flag", "Scaffold"});
        this.rSetting(mode);
        super.setup();
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mc.thePlayer.onGround && BlockUtils.isBlockUnder(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ)) {
            this.lastOnGroundX = mc.thePlayer.posX;
            this.lastOnGroundY = mc.thePlayer.posY;
            this.lastOnGroundZ = mc.thePlayer.posZ;
        }

        if (mc.thePlayer.fallDistance > 1.0F && !BlockUtils.isBlockUnder(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ)) {
            switch (mode.getValue()) {
                case "Teleport":
                    mc.thePlayer.setPositionAndUpdate(lastOnGroundX, lastOnGroundY, lastOnGroundZ);
                    MovementUtils.stop();
                    break;
                case "Flag" :
                    MovementUtils.stop();
                    PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 100, mc.thePlayer.posZ, true));
                    break;
                case "Scaffold" :
                    if (!Menace.instance.moduleManager.scaffoldModule.isToggled())
                        Menace.instance.moduleManager.scaffoldModule.toggle();
                    break;
            }
        }

    }

}
