package dev.menace.module.modules.misc;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventReceivePacket;
import dev.menace.event.events.EventWorldChange;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.settings.SliderSetting;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.player.PacketUtils;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S45PacketTitle;

public class AutoPlayModule extends BaseModule {

    boolean newGame;

    SliderSetting delay;

    public AutoPlayModule() {
        super("AutoPlay", Category.MISC, 0);
    }

    @Override
    public void setup() {
        delay = new SliderSetting("Delay", true, 500, 500, 2000, 100, true);
        this.rSetting(delay);
        super.setup();
    }

    @Override
    public void onEnable() {
        newGame = false;
        super.onEnable();
    }

    public void onRecievePacket(EventReceivePacket event) {
        if (!this.isToggled()) return;
        if (event.getPacket() instanceof S02PacketChat) {
            if (event.getPacket() == null || ((S02PacketChat) event.getPacket()).getChatComponent() == null || ((S02PacketChat) event.getPacket()).getChatComponent().getFormattedText() == null) return;
            String message = ((S02PacketChat) event.getPacket()).getChatComponent().getUnformattedText();
            if ((message.contains(mc.thePlayer.getName() + " was killed by ") || message.contains(mc.thePlayer.getName() + " died!")) && !newGame) {
                ChatUtils.message("Sending you to a new game.");
                new Thread(() -> {
                    try {
                        Thread.sleep(delay.getValueL());
                        PacketUtils.sendPacketNoEvent(new C09PacketHeldItemChange(7));
                        PacketUtils.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getStackInSlot(7)));
                        PacketUtils.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getStackInSlot(7)));
                        mc.thePlayer.inventory.currentItem = 0;
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
                newGame = true;
            } else if (newGame && (message.contains("Only VIP players can join full servers!") || message.contains("You can't join this private game!"))) {
                ChatUtils.message("Full server, retrying.");
                new Thread(() -> {
                    try {
                        Thread.sleep(1500);
                        PacketUtils.sendPacketNoEvent(new C09PacketHeldItemChange(7));
                        PacketUtils.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getStackInSlot(7)));
                        PacketUtils.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getStackInSlot(7)));
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        } else if (event.getPacket() instanceof S45PacketTitle) {
            String title = ((S45PacketTitle)event.getPacket()).getMessage().getUnformattedText();
            if (title.contains(mc.thePlayer.getName()) && !newGame) {
                ChatUtils.message("Sending you to a new game.");
                new Thread(() -> {
                    try {
                        Thread.sleep(delay.getValueL());
                        PacketUtils.sendPacketNoEvent(new C09PacketHeldItemChange(7));
                        PacketUtils.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getStackInSlot(7)));
                        PacketUtils.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getStackInSlot(7)));
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
                newGame = true;
            }
        }
    }

    @EventTarget
    public void onWorldChange(EventWorldChange event) {
        if (newGame) {
            PacketUtils.sendPacketNoEvent(new C09PacketHeldItemChange(0));
            mc.thePlayer.inventory.currentItem = 0;
        }
        newGame = false;
    }

}
