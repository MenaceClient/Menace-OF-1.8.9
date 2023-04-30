package dev.menace.event.events;

import dev.menace.event.Event;
import dev.menace.scripting.js.JSMapping;
import dev.menace.scripting.js.MappedName;
import net.minecraft.network.Packet;

@JSMapping(103)
public class EventSendPacket extends Event {

	private Packet packet;
	
	public EventSendPacket(Packet packet) {
		this.packet = packet;
	}

	@MappedName(68)
	public Packet getPacket() {
		return packet;
	}
	
}
