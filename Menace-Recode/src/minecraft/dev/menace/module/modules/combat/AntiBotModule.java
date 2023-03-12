package dev.menace.module.modules.combat;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventReceivePacket;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.utils.misc.ChatUtils;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;

import java.util.ArrayList;

public class AntiBotModule extends BaseModule {

    public final ArrayList<EntityPlayer> bots = new ArrayList<>();

    public AntiBotModule() {
        super("AntiBot", Category.COMBAT, 0);
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @EventTarget
    public void onUpdate(EventUpdate event) {
        for (EntityPlayer player : mc.theWorld.playerEntities) {
            if (player != mc.thePlayer) {
                NetworkPlayerInfo info = this.mc.getNetHandler().getPlayerInfo(player.getUniqueID());
                if (info == null || Float.isNaN(info.getResponseTime())) {
                    bots.add(player);
                }
            }
        }
    }

    @EventTarget
    public void onSendPacket(EventReceivePacket event) {
        if (event.getPacket() instanceof S0CPacketSpawnPlayer) {
            S0CPacketSpawnPlayer packet = (S0CPacketSpawnPlayer) event.getPacket();
            double posX = packet.getX() / 32D;
            double posY = packet.getY() / 32D;
            double posZ = packet.getZ() / 32D;

            double diffX = mc.thePlayer.posX - posX;
            double diffY = mc.thePlayer.posY - posY;
            double diffZ = mc.thePlayer.posZ - posZ;

            double dist = Math.sqrt(diffX * diffX + diffY * diffY + diffZ * diffZ);

            if (dist <= 17D && posX != mc.thePlayer.posX && posY != mc.thePlayer.posY && posZ != mc.thePlayer.posZ) {
                Entity entity = mc.theWorld.getEntityByID(packet.getEntityID());
                if (entity instanceof EntityPlayer) {
                    bots.add((EntityPlayer) entity);
                }
            }
        }
    }

}
