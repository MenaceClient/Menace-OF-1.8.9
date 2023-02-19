package dev.menace.module.modules.misc;

import com.mojang.authlib.GameProfile;
import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.*;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.player.PacketUtils;
import dev.menace.utils.timer.MSTimer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;

import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;

public class DisablerModule extends BaseModule {

    MSTimer timer = new MSTimer();
    Queue<Packet<?>> packetQueue = new ConcurrentLinkedDeque<>();

    public DisablerModule() {
        super("Disabler", Category.MISC, 0);
    }

    @Override
    public void onEnable() {
        this.setDisplayName("Experimental");
        timer.reset();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        /*long delay = 10;
        while (!packetQueue.isEmpty()) {
            PacketUtils.sendPacketNoEventDelayed(packetQueue.poll(), delay);
            delay += 10;
        }*/
        packetQueue.clear();
        super.onDisable();
    }

    @EventTarget
    public void onTeleport(EventTeleport event) {

    }

    @EventTarget
    public void onUpdate(EventUpdate event) {

        if (timer.hasTimePassed(750L)) {
            timer.reset();
            ChatUtils.message("Sending packet " + packetQueue.size() + "/ 300");
            if (!packetQueue.isEmpty()) {
                PacketUtils.sendPacketNoEvent(packetQueue.poll());
            }
        }

    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if (event.getPacket() instanceof C0FPacketConfirmTransaction) {
            C0FPacketConfirmTransaction packet = (C0FPacketConfirmTransaction) event.getPacket();

            if (isInventory(packet.getUid())) {
                return;
            }

            event.cancel();

            packetQueue.add(packet);
            ChatUtils.message("Added packet " + packetQueue.size() + "/ 300");

            if (packetQueue.size() > 300) {
                ChatUtils.message("emptying queue");
                packetQueue.clear();
            }
        } else if (event.getPacket() instanceof C00PacketKeepAlive) {
            C00PacketKeepAlive packet = (C00PacketKeepAlive) event.getPacket();
            packet.setkey(1337);
            event.cancel();
            packetQueue.add(packet);
        } else if (event.getPacket() instanceof C0BPacketEntityAction) {
            event.cancel();
        }
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
    }

    @EventTarget
    public void onWorldChange(EventWorldChange event) {
        ChatUtils.message("emptying queue");
        while (!packetQueue.isEmpty()) {
            PacketUtils.sendPacketNoEvent(packetQueue.poll());
        }
        timer.reset();
    }

    boolean isInventory(short action) {
        return action > 0 && action < 100;
    }

}
