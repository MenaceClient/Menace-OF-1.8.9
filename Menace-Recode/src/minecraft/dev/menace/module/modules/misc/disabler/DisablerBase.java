package dev.menace.module.modules.misc.disabler;

import dev.menace.event.events.*;
import net.minecraft.client.Minecraft;

public class DisablerBase {

    protected Minecraft mc = Minecraft.getMinecraft();

    public void onEnable() {}
    public void onDisable() {}
    public void onUpdate() {}
    public void onCollide(EventCollide event) {}
    public void onMove(EventMove event) {}
    public void onPreMotion(EventPreMotion event) {}
    public void onPostMotion(EventPostMotion event) {}
    public void onSendPacket(EventSendPacket event) {}
    public void onReceivePacket(EventReceivePacket event) {}
    public void onJump(EventJump event) {}
    public void onWorldChange(EventWorldChange event) {}

}
