package dev.menace.module.modules.combat;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventReceivePacket;
import dev.menace.event.events.EventRender3D;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.settings.SliderSetting;
import dev.menace.module.settings.ToggleSetting;
import dev.menace.utils.render.RenderUtils;
import dev.menace.utils.timer.MSTimer;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S14PacketEntity;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class BackTrackerModule extends BaseModule {

    private final Queue<S14PacketEntity> packets = new ConcurrentLinkedDeque<>();
    public final ArrayList<EntityPlayer> fakePos = new ArrayList<>();
    MSTimer delayTimer = new MSTimer();

    SliderSetting delay;
    SliderSetting maxPackets;
    ToggleSetting onlyTarget;

    public BackTrackerModule() {
        super("BackTracker", Category.COMBAT, 0);
    }

    @Override
    public void setup() {
        delay = new SliderSetting("Delay", true, 1000, 0, 5000, 50, true);
        maxPackets = new SliderSetting("Max Packets", true, 100, 0, 1000, 10, true);
        onlyTarget = new ToggleSetting("Only Target", true, true);
        this.rSetting(delay);
        this.rSetting(maxPackets);
        this.rSetting(onlyTarget);
        super.setup();
    }

    @Override
    public void onEnable() {
        delayTimer.reset();
        fakePos.clear();
        packets.clear();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        for (EntityPlayer player : fakePos) {
            mc.theWorld.removeEntity(player);
        }
        while (!packets.isEmpty()) {
            packets.poll().processPacket(mc.getNetHandler());
        }
        fakePos.clear();
        super.onDisable();
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (delayTimer.hasTimePassed(delay.getValueL())) {
            if (!packets.isEmpty()) {
                packets.poll().processPacket(mc.getNetHandler());
            }
            delayTimer.reset();
        }
    }

    @EventTarget
    public void onRecievePacket(EventReceivePacket event) {
        if (event.getPacket() instanceof S14PacketEntity) {

            if (onlyTarget.getValue() && !Menace.instance.moduleManager.killAuraModule.trget.contains((EntityLivingBase) ((S14PacketEntity)event.getPacket()).getEntity(mc.theWorld))  && fakePos != null) {
                fakePos.forEach(p -> {
                    if (Objects.equals(p.getUniqueID(), ((S14PacketEntity)event.getPacket()).getEntity(mc.theWorld).getUniqueID())) {
                        mc.theWorld.removeEntity(p);
                        fakePos.remove(p);
                        while (!packets.isEmpty()) {
                            packets.poll().processPacket(mc.getNetHandler());
                        }
                    }
                });
                return;
            }

            if (((S14PacketEntity)event.getPacket()).getEntity(mc.theWorld) == mc.thePlayer
                    || !(((S14PacketEntity)event.getPacket()).getEntity(mc.theWorld) instanceof EntityPlayer)) return;

            EntityPlayer player = (EntityPlayer) ((S14PacketEntity)event.getPacket()).getEntity(mc.theWorld);
            if (fakePos.stream().noneMatch(p -> Objects.equals(p.getUniqueID(), player.getUniqueID()))) {
                EntityPlayer fakePlayer = new EntityOtherPlayerMP(mc.theWorld, player.getGameProfile());
                fakePlayer.clonePlayer(player, true);
                fakePlayer.copyLocationAndAnglesFrom(player);
                fakePlayer.serverPosX = player.serverPosX;
                fakePlayer.serverPosY = player.serverPosY;
                fakePlayer.serverPosZ = player.serverPosZ;
                mc.theWorld.addEntityToWorld(fakePlayer.entityId, fakePlayer);
                handleEntityMovement((S14PacketEntity) event.getPacket(), fakePlayer);
                fakePos.add(fakePlayer);
            } else {
                fakePos.forEach(p -> {
                    if (Objects.equals(p.getUniqueID(), player.getUniqueID())) {
                        handleEntityMovement((S14PacketEntity) event.getPacket(), p);
                    }
                });
            }

            packets.add((S14PacketEntity) event.getPacket());
            if (packets.size() > maxPackets.getValue()) {
                Objects.requireNonNull(packets.poll()).processPacket(mc.getNetHandler());
            }
            event.cancel();
        }
    }

    @EventTarget
    public void onRender2D(EventRender3D event) {
        if (!fakePos.isEmpty()) {
            for (EntityPlayer fp : fakePos) {
                RenderUtils.drawEntityBox(fp, Color.CYAN, true);
            }
        }
    }

    public void handleEntityMovement(S14PacketEntity packetIn, EntityPlayer player) {
        if (player != null) {
            player.serverPosX += packetIn.getPosX();
            player.serverPosY += packetIn.getPosY();
            player.serverPosZ += packetIn.getPosZ();
            double d0 = (double) player.serverPosX / 32.0D;
            double d1 = (double) player.serverPosY / 32.0D;
            double d2 = (double) player.serverPosZ / 32.0D;
            float f = packetIn.func_149060_h() ? (float)(packetIn.getYaw() * 360) / 256.0F : player.rotationYaw;
            float f1 = packetIn.func_149060_h() ? (float)(packetIn.getPitch() * 360) / 256.0F : player.rotationPitch;
            player.setPositionAndRotation2(d0, d1, d2, f, f1, 3, true);
            player.onGround = packetIn.getOnGround();
        }
    }

}