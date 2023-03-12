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

        mode = new ListSetting("Mode", true, "Vanilla", new String[] {"Vanilla", "Lunar", "CheatBreaker", "Null"});

        this.rSetting(mode);
        super.setup();
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if (event.getPacket() instanceof C17PacketCustomPayload) {
            C17PacketCustomPayload packet = (C17PacketCustomPayload) event.getPacket();
            if (packet.getChannelName().startsWith("MC|Brand")) {
                event.cancel();

                String name = "vanilla";

                switch (mode.getValue()) {
                    case "Vanilla":
                        name = "vanilla";
                        break;
                    case "Lunar":
                        String addition = "";
                        String whitelisted_letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ12345678901234567890__";
                        while (addition.length() < 7) {
                            addition = addition.concat(String.valueOf(whitelisted_letters.charAt(MathUtils.randInt(0, whitelisted_letters.length()))));
                        }
                        name = "lunarclient:" + addition;
                        break;
                    case "CheatBreaker":
                        name = "CB";
                        break;
                    case "Null":
                        name = "null";
                        break;
                }

                C17PacketCustomPayload newPacket = new C17PacketCustomPayload("MC|Brand", (new PacketBuffer(Unpooled.buffer())).writeString(name));
                PacketUtils.sendPacketNoEvent(newPacket);
            }
        }
    }

}
