package dev.menace.module.modules.player;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventReceivePacket;
import dev.menace.event.events.EventSendPacket;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.settings.ToggleSetting;
import dev.menace.ui.clickgui.csgo.CSGOGui;
import dev.menace.ui.clickgui.lime.LimeClickGUI;
import dev.menace.ui.clickgui.vape.VapeGui;
import dev.menace.utils.player.PacketUtils;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class InvMoveModule extends BaseModule {

    private final Queue<Packet<?>> packets = new ConcurrentLinkedDeque<>();

    ToggleSetting rotate;
    ToggleSetting blink;

    public InvMoveModule() {
        super("InvMove", Category.PLAYER, 0);
    }

    @Override
    public void setup() {
        rotate = new ToggleSetting("Rotate", true, true);
        blink = new ToggleSetting("Blink", true, false);
        this.rSetting(rotate);
        this.rSetting(blink);
        super.setup();
    }

    @Override
    public void onDisable() {
        if (!GameSettings.isKeyDown(mc.gameSettings.keyBindForward) || mc.currentScreen != null)
            mc.gameSettings.keyBindForward.pressed = false;
        if (!GameSettings.isKeyDown(mc.gameSettings.keyBindBack) || mc.currentScreen != null)
            mc.gameSettings.keyBindBack.pressed = false;
        if (!GameSettings.isKeyDown(mc.gameSettings.keyBindRight) || mc.currentScreen != null)
            mc.gameSettings.keyBindRight.pressed = false;
        if (!GameSettings.isKeyDown(mc.gameSettings.keyBindLeft) || mc.currentScreen != null)
            mc.gameSettings.keyBindLeft.pressed = false;
        if (!GameSettings.isKeyDown(mc.gameSettings.keyBindJump) || mc.currentScreen != null)
            mc.gameSettings.keyBindJump.pressed = false;
        if (!GameSettings.isKeyDown(mc.gameSettings.keyBindSprint) || mc.currentScreen != null)
            mc.gameSettings.keyBindSprint.pressed = false;

        blink();
        super.onDisable();
    }

    @EventTarget
    public void onUpdate(@NotNull EventUpdate event) {
        if (mc.currentScreen instanceof GuiContainer || mc.currentScreen instanceof VapeGui || mc.currentScreen instanceof LimeClickGUI || mc.currentScreen instanceof CSGOGui) {
            mc.gameSettings.keyBindForward.pressed = GameSettings.isKeyDown(mc.gameSettings.keyBindForward);
            mc.gameSettings.keyBindBack.pressed = GameSettings.isKeyDown(mc.gameSettings.keyBindBack);
            mc.gameSettings.keyBindRight.pressed = GameSettings.isKeyDown(mc.gameSettings.keyBindRight);
            mc.gameSettings.keyBindLeft.pressed = GameSettings.isKeyDown(mc.gameSettings.keyBindLeft);
            mc.gameSettings.keyBindJump.pressed = GameSettings.isKeyDown(mc.gameSettings.keyBindJump);
            mc.gameSettings.keyBindSprint.pressed = GameSettings.isKeyDown(mc.gameSettings.keyBindSprint);
            if (rotate.getValue()) {
                if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
                    mc.thePlayer.rotationYaw += 1;
                } else if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
                    mc.thePlayer.rotationYaw -= 1;
                } else if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
                    mc.thePlayer.rotationPitch -= 1;
                } else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
                    mc.thePlayer.rotationPitch += 1;
                }
            }
        } else {
            blink();
        }
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if (!(mc.currentScreen instanceof GuiContainer) || !blink.getValue()) return;
        if (event.getPacket() instanceof C03PacketPlayer.C04PacketPlayerPosition || event.getPacket() instanceof C03PacketPlayer.C06PacketPlayerPosLook ||
                event.getPacket() instanceof C08PacketPlayerBlockPlacement ||
                event.getPacket() instanceof C0APacketAnimation ||
                event.getPacket() instanceof C0BPacketEntityAction || event.getPacket() instanceof C02PacketUseEntity ||
                event.getPacket() instanceof C03PacketPlayer) {
            event.setCancelled(true);
            packets.add(event.getPacket());
        }
    }

    @EventTarget
    public void onRecievePacket(EventReceivePacket event) {
        if (event.getPacket() instanceof S2EPacketCloseWindow) {
            event.cancel();
            PacketUtils.sendPacketNoEvent(new C0DPacketCloseWindow());
            PacketUtils.sendPacketNoEvent(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
        }
    }

    public void blink() {
        while (!packets.isEmpty()) {
            PacketUtils.sendPacketNoEvent(packets.poll());
        }
    }

}
