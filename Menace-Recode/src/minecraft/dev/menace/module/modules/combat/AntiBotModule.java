package dev.menace.module.modules.combat;

import com.mojang.authlib.GameProfile;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventSpawnEntity;
import dev.menace.event.events.EventUpdate;
import dev.menace.event.events.EventWorldChange;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.settings.SliderSetting;
import dev.menace.module.settings.ToggleSetting;
import dev.menace.utils.misc.ChatUtils;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;

public class AntiBotModule extends BaseModule {

    public final ArrayList<EntityPlayer> bots = new ArrayList<>();

    ToggleSetting gameProfile;
    ToggleSetting networkInfo;
    ToggleSetting matrix;
    SliderSetting spawnDistance;
    ToggleSetting hideBots;

    public AntiBotModule() {
        super("AntiBot", Category.COMBAT, 0);
    }

    @Override
    public void setup() {
        gameProfile = new ToggleSetting("CheckGameProfile", true, false);
        networkInfo = new ToggleSetting("CheckNetworkInfo", true, true);
        matrix = new ToggleSetting("Matrix", true, true);
        spawnDistance = new SliderSetting("SpawnDistance", true, 17, 1, 50, true) {
            @Override
            public void constantCheck() {
                this.setVisible(matrix.getValue());
                super.constantCheck();
            }
        };
        hideBots = new ToggleSetting("HideBots", true, false);
        this.rSetting(gameProfile);
        this.rSetting(networkInfo);
        this.rSetting(matrix);
        this.rSetting(spawnDistance);
        this.rSetting(hideBots);
        super.setup();
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        bots.clear();
        super.onDisable();
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        for (EntityPlayer player : mc.theWorld.playerEntities) {
            if (player != mc.thePlayer && !bots.contains(player)) {
                NetworkPlayerInfo info = this.mc.getNetHandler().getPlayerInfo(player.getUniqueID());
                if (info == null && networkInfo.getValue()) {
                    ChatUtils.message("Added " + player.getName() + " to bots list");
                    bots.add(player);
                }

                GameProfile profile = player.getGameProfile();
                if ((profile.getProperties() == null || profile.getProperties().isEmpty()) && gameProfile.getValue()) {
                    ChatUtils.message("Added " + player.getName() + " to bots list");
                    bots.add(player);
                }
            }
        }

        for (EntityPlayer player : bots) {
            if (mc.theWorld.playerEntities.contains(player)) {
                if (hideBots.getValue()) {
                    mc.theWorld.playerEntities.remove(player);
                }
            }
        }
    }

    @EventTarget
    public void onPlayerSpawn(EventSpawnEntity event) {
        Entity entity = event.getEntity();

        if (!(entity instanceof EntityPlayer) || !matrix.getValue()) {
            return;
        }

        EntityPlayer player = (EntityPlayer) entity;

        double diffX = mc.thePlayer.posX - player.posX;
        double diffY = mc.thePlayer.posY - player.posY;
        double diffZ = mc.thePlayer.posZ - player.posZ;

        double dist = Math.sqrt(diffX * diffX + diffY * diffY + diffZ * diffZ);

        if (dist <= spawnDistance.getValue() /*&& player.posX != mc.thePlayer.posX && player.posY != mc.thePlayer.posY && player.posZ != mc.thePlayer.posZ*/) {
            ChatUtils.message("Added " + player.getName() + " to bots list");
            bots.add(player);

            if (hideBots.getValue()) {
                event.cancel();
            }
        }

    }

    @EventTarget
    public void onWorldChange(EventWorldChange event) {
        bots.clear();
    }

}
