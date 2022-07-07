package dev.menace.module.modules.misc;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventReceivePacket;
import dev.menace.event.events.EventSendPacket;
import dev.menace.event.events.EventUpdate;
import dev.menace.event.events.EventWorldChange;
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

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class DisablerModule extends BaseModule {


    public DisablerModule() {
        super("Disabler", Category.MISC, 0);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        PacketUtils.sendPacket(new C19PacketResourcePackStatus("", C19PacketResourcePackStatus.Action.ACCEPTED));
    }

    @EventTarget
    public void onSendPacket(@NotNull EventSendPacket event) {
    }

    @EventTarget
    public void onWorldChange(EventWorldChange event) {

    }

}
