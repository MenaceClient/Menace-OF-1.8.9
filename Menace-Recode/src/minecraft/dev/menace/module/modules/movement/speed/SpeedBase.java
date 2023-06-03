package dev.menace.module.modules.movement.speed;

import dev.menace.event.events.EventMove;
import dev.menace.event.events.EventPreMotion;
import net.minecraft.client.Minecraft;

public class SpeedBase {

    public Minecraft mc = Minecraft.getMinecraft();

    public void onEnable() {}
    public void onDisable() {}
    public void onUpdate() {}
    public void onMove(EventMove event) {}
    public void onPreMotion(EventPreMotion event) {}

}
