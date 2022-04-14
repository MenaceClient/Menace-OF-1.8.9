package dev.menace.module.modules.movement.speeds;

import dev.menace.Menace;
import dev.menace.event.events.EventCollide;
import dev.menace.event.events.EventJump;
import dev.menace.event.events.EventMove;
import dev.menace.event.events.EventPostMotionUpdate;
import dev.menace.event.events.EventPreMotionUpdate;
import dev.menace.event.events.EventReceivePacket;
import dev.menace.event.events.EventUpdate;
import net.minecraft.client.Minecraft;

public abstract class SpeedBase {
	
	protected Minecraft MC = Menace.instance.MC;
	
	public SpeedBase() {
	}

	public void onEnable() {}
	public void onDisable() {}

    public void onMove(EventMove event) {}
    public void onUpdate() {}
    public void onJump(EventJump event) {}
    public void onCollision(EventCollide event) {}
    public void onRecievePacket(EventReceivePacket event) {}
    public void onPreMotion(EventPreMotionUpdate event) {}
	
}
