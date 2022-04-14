package dev.menace.module.modules.misc.disablers;

import dev.menace.Menace;
import dev.menace.event.events.EventReceivePacket;
import dev.menace.event.events.EventSendPacket;
import net.minecraft.client.Minecraft;

public class DisablerBase {

	protected Minecraft MC = Menace.instance.MC;
	
	public void onEnable() {}
	public void onDisable() {}
	
    public void onUpdate() {}
    public void onSendPacket(EventSendPacket event) {}
    public void onReceivePacket(EventReceivePacket event) {}

	
}
