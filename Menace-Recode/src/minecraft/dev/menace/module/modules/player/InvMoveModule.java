package dev.menace.module.modules.player;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventSendPacket;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.settings.ToggleSetting;
import dev.menace.utils.player.PacketUtils;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
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
        if (!GameSettings.isKeyDown(MC.gameSettings.keyBindForward) || MC.currentScreen != null)
            MC.gameSettings.keyBindForward.pressed = false;
        if (!GameSettings.isKeyDown(MC.gameSettings.keyBindBack) || MC.currentScreen != null)
            MC.gameSettings.keyBindBack.pressed = false;
        if (!GameSettings.isKeyDown(MC.gameSettings.keyBindRight) || MC.currentScreen != null)
            MC.gameSettings.keyBindRight.pressed = false;
        if (!GameSettings.isKeyDown(MC.gameSettings.keyBindLeft) || MC.currentScreen != null)
            MC.gameSettings.keyBindLeft.pressed = false;
        if (!GameSettings.isKeyDown(MC.gameSettings.keyBindJump) || MC.currentScreen != null)
            MC.gameSettings.keyBindJump.pressed = false;
        if (!GameSettings.isKeyDown(MC.gameSettings.keyBindSprint) || MC.currentScreen != null)
            MC.gameSettings.keyBindSprint.pressed = false;

        blink();
        super.onDisable();
    }

    @EventTarget
    public void onUpdate(@NotNull EventUpdate event) {
        if (MC.currentScreen instanceof GuiContainer) {
            MC.gameSettings.keyBindForward.pressed = GameSettings.isKeyDown(MC.gameSettings.keyBindForward);
            MC.gameSettings.keyBindBack.pressed = GameSettings.isKeyDown(MC.gameSettings.keyBindBack);
            MC.gameSettings.keyBindRight.pressed = GameSettings.isKeyDown(MC.gameSettings.keyBindRight);
            MC.gameSettings.keyBindLeft.pressed = GameSettings.isKeyDown(MC.gameSettings.keyBindLeft);
            MC.gameSettings.keyBindJump.pressed = GameSettings.isKeyDown(MC.gameSettings.keyBindJump);
            MC.gameSettings.keyBindSprint.pressed = GameSettings.isKeyDown(MC.gameSettings.keyBindSprint);
            if (rotate.getValue()) {
                if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
                    MC.thePlayer.rotationYaw += 1;
                } else if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
                    MC.thePlayer.rotationYaw -= 1;
                } else if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
                    MC.thePlayer.rotationPitch -= 1;
                } else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
                    MC.thePlayer.rotationPitch += 1;
                }
            }
        } else {
            blink();
        }
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if (!(MC.currentScreen instanceof GuiContainer) || !blink.getValue()) return;
        if (event.getPacket() instanceof C03PacketPlayer.C04PacketPlayerPosition || event.getPacket() instanceof C03PacketPlayer.C06PacketPlayerPosLook ||
                event.getPacket() instanceof C08PacketPlayerBlockPlacement ||
                event.getPacket() instanceof C0APacketAnimation ||
                event.getPacket() instanceof C0BPacketEntityAction || event.getPacket() instanceof C02PacketUseEntity ||
                event.getPacket() instanceof C03PacketPlayer) {
            event.setCancelled(true);
            packets.add(event.getPacket());
        }
    }

    public void blink() {
        while (!packets.isEmpty()) {
            PacketUtils.sendPacketNoEvent(packets.poll());
        }
    }

}
