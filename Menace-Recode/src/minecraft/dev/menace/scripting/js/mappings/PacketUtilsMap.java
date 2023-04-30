package dev.menace.scripting.js.mappings;

import dev.menace.scripting.js.JSMapping;
import dev.menace.scripting.js.MappedName;
import dev.menace.utils.player.PacketUtils;
import net.minecraft.network.Packet;

@JSMapping
public class PacketUtilsMap {

    @MappedName(15)
    public static void sendPacket(Packet packetIn) {
        PacketUtils.sendPacket(packetIn);
    }

    @MappedName(16)
    public static void sendPacketNoEvent(Packet packetIn) {
        PacketUtils.sendPacketNoEvent(packetIn);
    }

    @MappedName(17)
    public static void sendPacketNoEventDelayed(Packet packetIn, long delay) {
        PacketUtils.sendPacketNoEventDelayed(packetIn, delay);
    }

    @MappedName(18)
    public static void addToSendQueue(Packet packetIn) {
        PacketUtils.addToSendQueue(packetIn);
    }

}
