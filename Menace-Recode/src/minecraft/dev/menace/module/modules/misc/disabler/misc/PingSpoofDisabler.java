package dev.menace.module.modules.misc.disabler.misc;

import dev.menace.Menace;
import dev.menace.event.events.EventSendPacket;
import dev.menace.event.events.EventWorldChange;
import dev.menace.module.modules.misc.disabler.DisablerBase;
import dev.menace.utils.player.PacketUtils;
import dev.menace.utils.timer.MSTimer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;

import java.util.LinkedList;

public class PingSpoofDisabler extends DisablerBase {

    private final LinkedList<Packet<?>> packetBuffer = new LinkedList<>();
    private final MSTimer fakeLagDelay = new MSTimer();

    @Override
    public void onEnable() {
        fakeLagDelay.reset();
        packetBuffer.clear();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        for (Packet<?> packet : packetBuffer) {
            PacketUtils.sendPacketNoEvent(packet);
        }
        super.onDisable();
    }

    @Override
    public void onUpdate() {
        if (fakeLagDelay.hasTimePassed(Menace.instance.moduleManager.disablerModule.pingSpoofDelay.getValueL())
                || packetBuffer.size() > Menace.instance.moduleManager.disablerModule.pingSpoofMaxPackets.getValueL()) {
            PacketUtils.sendPacketNoEvent(packetBuffer.poll());
            fakeLagDelay.reset();
        }
        super.onUpdate();
    }

    @Override
    public void onSendPacket(EventSendPacket event) {
        if (event.getPacket() instanceof C00PacketKeepAlive) {
            packetBuffer.add(event.getPacket());
            event.setCancelled(true);
        } else if (event.getPacket() instanceof C0FPacketConfirmTransaction && Menace.instance.moduleManager.disablerModule.pingSpoofTransaction.getValue()) {
            packetBuffer.add(event.getPacket());
            event.setCancelled(true);
        }
        super.onSendPacket(event);
    }

    @Override
    public void onWorldChange(EventWorldChange event) {
        for (Packet<?> packet : packetBuffer) {
            PacketUtils.sendPacketNoEvent(packet);
        }
        super.onWorldChange(event);
    }
}
