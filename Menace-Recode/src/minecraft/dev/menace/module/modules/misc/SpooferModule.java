package dev.menace.module.modules.misc;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventSendPacket;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.settings.ListSetting;
import dev.menace.utils.misc.MathUtils;
import dev.menace.utils.player.PacketUtils;
import io.netty.buffer.Unpooled;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;

public class SpooferModule extends BaseModule {

    ListSetting mode;

    public SpooferModule() {
        super("Spoofer", Category.MISC, 0);
    }

    @Override
    public void setup() {

        mode = new ListSetting("Mode", true, "Vanilla", new String[] {"Vanilla", "Forge", "Lunar", "LabyMod", "PVP Lounge", "CheatBreaker", "Geyser", "Null"});

        this.rSetting(mode);
        super.setup();
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        this.setDisplayName(mode.getValue());
        if (event.getPacket() instanceof C17PacketCustomPayload) {
            final C17PacketCustomPayload packet = (C17PacketCustomPayload) event.getPacket();
            switch (mode.getValue()) {
                case "Vanilla": {
                    packet.setData(createPacketBuffer("vanilla", true));
                    break;
                }
                case "Forge": {
                    packet.setData(createPacketBuffer("FML", true));
                    break;
                }
                case "Lunar": {
                    packet.setChannel("REGISTER");
                    packet.setData(createPacketBuffer("Lunar-Client", false));
                    break;
                }
                case "LabyMod": {
                    packet.setData(createPacketBuffer("LMC", true));
                    break;
                }
                case "PvP Lounge": {
                    packet.setData(createPacketBuffer("PLC18", false));
                    break;
                }
                case "CheatBreaker": {
                    packet.setData(createPacketBuffer("CB", true));
                    break;
                }
                case "Geyser": {
                    packet.setData(createPacketBuffer("Geyser", false));
                    break;
                }
                case "Null": {
                    packet.setData(null);
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
