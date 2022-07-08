package dev.menace.event.events;

import dev.menace.event.Event;
import net.minecraft.network.Packet;

public class EventSendPacket extends Event {

	public Packet packet;
	
	public EventSendPacket(Packet packet) {
		this.packet = packet;
	}
	
	public Packet getPacket() {
		return packet;
	}
	
}
