package dev.menace.module.modules.misc;

import dev.menace.event.EventTarget;
import dev.menace.event.events.*;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.misc.MathUtils;
import dev.menace.utils.player.PacketUtils;
import dev.menace.utils.timer.MSTimer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class DisablerModule extends BaseModule {

    public static final List<Packet<?>> packet = new ArrayList<>();
    private static Vec3 initialPosition;

    public DisablerModule() {
        super("Disabler", Category.MISC, 0);
    }

    @Override
    public void onEnable() {
        this.setDisplayName("BlocksMC");
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        if (packet.size() > 50) {
            while (!packet.isEmpty()) {
                PacketUtils.sendPacketNoEvent(packet.remove(0));
            }
        }
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        final Packet<?> p = event.getPacket();

        if (p instanceof C03PacketPlayer) {
            final C03PacketPlayer wrapper = (C03PacketPlayer) p;

            if (mc.thePlayer.ticksExisted == 1) {
                initialPosition = new Vec3(wrapper.getPositionX() + MathUtils.randInt(-1000000, 1000000), wrapper.getPositionY() + MathUtils.randInt(-1000000, 1000000), wrapper.getPositionZ() + MathUtils.randInt(-1000000, 1000000));
            } else if (mc.thePlayer.sendQueue.doneLoadingTerrain && initialPosition != null && mc.thePlayer.ticksExisted < 100) {
                wrapper.setX(initialPosition.xCoord);
                wrapper.setY(initialPosition.yCoord);
                wrapper.setZ(initialPosition.zCoord);
            }
        }

        if (p instanceof C0FPacketConfirmTransaction && !(((C0FPacketConfirmTransaction)p).getUid() > 0 && ((C0FPacketConfirmTransaction)p).getUid() < 100)) {
            packet.add(p);
            event.setCancelled(true);
        }

        if (p instanceof C0BPacketEntityAction) {
            event.setCancelled(true);
        }
    }

    @EventTarget
    public void onTeleport(EventTeleport event) {
        if (mc.thePlayer.sendQueue.doneLoadingTerrain) {
            if (mc.thePlayer.ticksExisted < 100) {
                for (int i = 0; i < 10; i++) {
                    PacketUtils.sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(event.getPosX(), event.getPosY(), event.getPosZ(), event.getYaw(), event.getPitch(), false));
                }

                PacketUtils.sendPacketNoEvent(event.getResponse());

                if (mc.thePlayer.getDistance(event.getPosX(), event.getPosY(), event.getPosZ()) < 3) {
                    event.setCancelled(true);
                }
            } else {
                event.setPosX(event.getPosX() - Double.MIN_VALUE);
                event.setPosZ(event.getPosZ() + Double.MIN_VALUE);
            }
        }
    }

    @EventTarget
    public void onWorld(EventWorldChange event) {
        while (!packet.isEmpty()) {
            PacketUtils.sendPacketNoEvent(packet.remove(0));
        }
    }

}
