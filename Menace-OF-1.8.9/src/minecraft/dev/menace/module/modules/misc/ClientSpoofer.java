package dev.menace.module.modules.misc;

import java.util.ArrayList;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventSendPacket;
import dev.menace.module.Category;
import dev.menace.module.Module;
import dev.menace.module.settings.StringSetting;
import dev.menace.utils.misc.ChatUtils;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;

public class ClientSpoofer extends Module {

	//Settings
	StringSetting client;
	
	public ClientSpoofer() {
		super("ClientSpoofer", 0, Category.MISC);
	}
	
	@Override
	public void setup() {
		ArrayList<String> op = new ArrayList<String>();
		op.add("Forge");
		op.add("Lunar");
		op.add("LabyMod");
		op.add("PvP Lounge");
		op.add("CheatBreaker");
		op.add("Geyser");
		client = new StringSetting("Client", this, "Forge", op);
		this.rSetting(client);
	}
	
	@Override
	public void onEnable() {
		ChatUtils.message("Rejoin for this to work.");
		super.onEnable();
	}
	
	@EventTarget
	public void onSendPacket(EventSendPacket event) {
		if (event.getPacket() instanceof C17PacketCustomPayload) {
            final C17PacketCustomPayload packet = (C17PacketCustomPayload) event.getPacket();
            switch (client.getString().toLowerCase()) {
                case "forge": {
                    packet.setData(createPacketBuffer("FML", true));
                    break;
                }

                case "lunar": {
                    packet.setChannel("REGISTER");
                    packet.setData(createPacketBuffer("Lunar-Client", false));
                    break;
                }

                case "labyMod": {
                    packet.setData(createPacketBuffer("LMC", true));
                    break;
                }

                case "pvp lounge": {
                    packet.setData(createPacketBuffer("PLC18", false));
                    break;
                }

                case "cheatbreaker": {
                    packet.setData(createPacketBuffer("CB", true));
                    break;
                }

                case "geyser": {
                    packet.setData(createPacketBuffer("Geyser", false));
                    break;
                }
            }
        }
	}
	
	   private PacketBuffer createPacketBuffer(final String data, final boolean string) {
	        if (string)
	            return new PacketBuffer(Unpooled.buffer()).writeString(data);
	        else
	            return new PacketBuffer(Unpooled.wrappedBuffer(data.getBytes()));
	    }

}
