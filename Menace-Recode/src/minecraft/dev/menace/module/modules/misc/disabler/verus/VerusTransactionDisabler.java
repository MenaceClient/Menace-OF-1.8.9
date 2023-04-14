package dev.menace.module.modules.misc.disabler.verus;

import dev.menace.Menace;
import dev.menace.event.events.EventReceivePacket;
import dev.menace.event.events.EventSendPacket;
import dev.menace.event.events.EventWorldChange;
import dev.menace.module.modules.misc.disabler.DisablerBase;
import dev.menace.utils.notifications.Notification;
import dev.menace.utils.player.PacketUtils;
import dev.menace.utils.player.PathFindingUtils;
import dev.menace.utils.timer.MSTimer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C18PacketSpectate;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.Vec3;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.UUID;

public class VerusTransactionDisabler extends DisablerBase {

    private final LinkedList<Packet<INetHandlerPlayServer>> packetBuffer = new LinkedList<>();
    private final MSTimer fakeLagDelay = new MSTimer();
    private boolean verus2Stat = false;
    private boolean expectedTeleport = false;

    @Override
    public void onEnable() {
        packetBuffer.clear();
        fakeLagDelay.reset();
        verus2Stat = false;
        expectedTeleport = false;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        while (!packetBuffer.isEmpty()) {
            mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(packetBuffer.poll());
        }
        super.onDisable();
    }

    @Override
    public void onUpdate() {
        if (mc.isIntegratedServerRunning())
            return;

        PacketUtils.sendPacketNoEvent(new C0CPacketInput());
        PacketUtils.sendPacketNoEvent(new C18PacketSpectate(UUID.randomUUID()));

        if(fakeLagDelay.hasTimePassed(490L)) {
            fakeLagDelay.reset();
            if (!packetBuffer.isEmpty()) {
                mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(packetBuffer.poll());
            }
        }
    }

    @Override
    public void onSendPacket(EventSendPacket event) {
        if(event.getPacket() instanceof C0FPacketConfirmTransaction) {
            C0FPacketConfirmTransaction packet = (C0FPacketConfirmTransaction) event.getPacket();

            if (isInventory(packet.getUid())) {
                return;
            }

            packetBuffer.add(packet);
            event.setCancelled(true);
            if(packetBuffer.size() > 300) {
                if(!verus2Stat) {
                    verus2Stat = true;
                    Menace.instance.notificationManager.registerNotification(new Notification("Disabler", "Verus Disabled", 5000L, Color.green));
                }
                mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(packetBuffer.poll());
            }
        } else if(event.getPacket() instanceof C03PacketPlayer) {
            C03PacketPlayer packet = (C03PacketPlayer) event.getPacket();
            if(mc.thePlayer.ticksExisted % 40 == 0) {

                packet.setMoving(false);
                //packet.setY(-0.015625);
                packet.setY(packet.getPositionY() - 11.4514);
                packet.setOnGround(false);

                if (verus2Stat && Menace.instance.moduleManager.disablerModule.silent.getValue()) {
                    expectedTeleport = true;
                }
            }
        } /*else if (event.getPacket() instanceof C00PacketKeepAlive) {
            C00PacketKeepAlive packet = (C00PacketKeepAlive) event.getPacket();
            packet.setkey(-packet.getKey());
        } else if (event.getPacket() instanceof C0BPacketEntityAction) {
            final C0BPacketEntityAction c0B = (C0BPacketEntityAction) event.getPacket();

            if (c0B.getAction().equals(C0BPacketEntityAction.Action.START_SPRINTING)) {
                if (mc.thePlayer.isServerSprintState()) {
                    PacketUtils.sendPacketNoEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
                    mc.thePlayer.setServerSprintState(false);
                }
                event.setCancelled(true);
            }

            if (c0B.getAction().equals(C0BPacketEntityAction.Action.STOP_SPRINTING)) {
                event.setCancelled(true);
            }
        }*/

        if (mc.thePlayer != null && mc.thePlayer.ticksExisted <= 7) {
            fakeLagDelay.reset();
            packetBuffer.clear();
        }
    }

    @Override
    public void onReceivePacket(EventReceivePacket event) {
        if (event.getPacket() instanceof S08PacketPlayerPosLook && verus2Stat && Menace.instance.moduleManager.disablerModule.silent.getValue()) {
            S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) event.getPacket();

            if (expectedTeleport) {
                event.cancel();
                PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(packet.getX(), packet.getY(), packet.getZ(), packet.getYaw(), packet.getPitch(), true));

                Vec3 from = new Vec3(packet.getX(), packet.getY(), packet.getZ());
                Vec3 to = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);

                ArrayList<Vec3> path = PathFindingUtils.computePath(from, to);

                for (Vec3 vec3 : path) {
                    PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(vec3.xCoord, vec3.yCoord, vec3.zCoord, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, false));
                }

                PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, mc.thePlayer.onGround));

                expectedTeleport = false;
            }
        }
    }

    @Override
    public void onWorldChange(EventWorldChange event) {
        while (!packetBuffer.isEmpty()) {
            PacketUtils.sendPacketNoEvent(packetBuffer.poll());
        }
        fakeLagDelay.reset();
        verus2Stat = false;
        expectedTeleport = false;
    }

    boolean isInventory(short action) {
        return action > 0 && action < 100;
    }


}
