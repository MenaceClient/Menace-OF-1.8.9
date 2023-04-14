package dev.menace.scripting.mappings;

import dev.menace.utils.player.PacketUtils;
import net.minecraft.network.Packet;

public class PacketUtilsMap {

    public static void sendPacket(Packet packetIn) {
        PacketUtils.sendPacket(packetIn);
    }

    public static void sendPacketNoEvent(Packet packetIn) {
        PacketUtils.sendPacketNoEvent(packetIn);
    }

    public static void sendPacketNoEventDelayed(Packet packetIn, long delay) {
        PacketUtils.sendPacketNoEventDelayed(packetIn, delay);
    }

    public static void addToSendQueue(Packet packetIn) {
        PacketUtils.addToSendQueue(packetIn);
    }

}
