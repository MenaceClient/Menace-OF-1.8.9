package dev.menace.module.modules.combat;

import java.util.ArrayList;

import dev.menace.event.EventTarget;
import dev.menace.event.events.*;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.settings.ListSetting;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.player.MovementUtils;
import dev.menace.utils.player.PacketUtils;
import dev.menace.utils.player.PlayerUtils;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;

public class CriticalsModule extends BaseModule {

    private boolean shouldCrit;
    private int editCrit;

	//Settings
    ListSetting mode;
	
    public CriticalsModule() {
        super("Criticals", "Makes all your attacks crits", Category.COMBAT, 0);
    }

    @Override
    public void setup() {
        mode = new ListSetting("Mode", true, "Packet", new String[] {"Packet", "MiniJump", "Jump", "Verus MiniJump", "OldNCP", "Gay"});
        this.rSetting(mode);
    }

    @Override
    public void onEnable() {
        shouldCrit = false;
        editCrit = 0;
        this.setDisplayName(mode.getValue());
        super.onEnable();
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        this.setDisplayName(mode.getValue());
        if (canCrit() && event.getPacket() instanceof C02PacketUseEntity) {
            C02PacketUseEntity packet = (C02PacketUseEntity)event.getPacket();
            if(packet.getAction() == C02PacketUseEntity.Action.ATTACK) {
                if (mode.getValue().equalsIgnoreCase("Packet")) {
                    PacketUtils.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0625, mc.thePlayer.posZ, true));
                    PacketUtils.sendPacket(new C03PacketPlayer(false));
                    PacketUtils.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.1E-5, mc.thePlayer.posZ, false));
                    PacketUtils.sendPacket(new C03PacketPlayer(false));
                } else if (mode.getValue().equalsIgnoreCase("MiniJump")) {
                    mc.thePlayer.jump();
                    mc.thePlayer.motionY -= .30000001192092879;
                }else if (mode.getValue().equalsIgnoreCase("Jump")) {
                    mc.thePlayer.jump();
                } else if (mode.getValue().equalsIgnoreCase("Verus MiniJump")) {
                    shouldCrit = true;
                } else if (mode.getValue().equalsIgnoreCase("OldNCP")) {
                     editCrit = 1;
                } else if (mode.getValue().equalsIgnoreCase("Gay")) {
                    PacketUtils.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, Integer.MAX_VALUE, mc.thePlayer.posZ, false));
                    PacketUtils.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
                }
                mc.thePlayer.onCriticalHit(mc.theWorld.getEntityByID(packet.entityId));
            }
        }
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        if (mc.thePlayer.onGround && editCrit >= 1) {
            if (editCrit == 1) {
                editCrit = 2;
                event.setPosY(event.getPosY() + 0.11);
            } else if (editCrit == 2) {
                editCrit = 3;
                event.setPosY(event.getPosY() + 0.1100013579);
            } else if (editCrit == 3) {
                editCrit = 0;
                event.setPosY(event.getPosY() + 0.0000013579);
            }
            event.setOnGround(false);
        }
    }

    @EventTarget
    public void onMove(EventMove event) {
        if (mode.getValue().equalsIgnoreCase("Verus MiniJump") && shouldCrit) {
            mc.gameSettings.keyBindJump.pressed = false;
            if (mc.thePlayer.onGround) {
                mc.thePlayer.jump();
                mc.thePlayer.motionY = 0.0;
                event.setY(0.41999998688698);
            }
            shouldCrit = false;
        }
    }

    private boolean canCrit() {
        return mc.thePlayer != null && !PlayerUtils.isInLiquid() && mc.thePlayer.onGround;
    }
}

