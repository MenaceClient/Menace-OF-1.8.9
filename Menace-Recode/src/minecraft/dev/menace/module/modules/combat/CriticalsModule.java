package dev.menace.module.modules.combat;

import java.util.ArrayList;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventMove;
import dev.menace.event.events.EventSendPacket;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.settings.ListSetting;
import dev.menace.utils.player.MovementUtils;
import dev.menace.utils.player.PlayerUtils;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;

public class CriticalsModule extends BaseModule {

    private boolean shouldCrit;

	//Settings
    ListSetting mode;
	
    public CriticalsModule() {
        super("Criticals", Category.COMBAT, 0);
    }

    @Override
    public void setup() {
        mode = new ListSetting("Mode", true, "Packet", new String[] {"Packet", "MiniJump", "Verus MiniJump"});
        this.rSetting(mode);
    }

    @Override
    public void onEnable() {
        shouldCrit = false;
        super.onEnable();
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        this.setDisplayName(mode.getValue());
        if(canCrit()) {
            if (event.getPacket() instanceof C02PacketUseEntity) {
                C02PacketUseEntity packet = (C02PacketUseEntity)event.getPacket();
                if(packet.getAction() == C02PacketUseEntity.Action.ATTACK) {
                    if(mode.getValue().equalsIgnoreCase("Packet")) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0625, mc.thePlayer.posZ, true));
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(false));
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.1E-5, mc.thePlayer.posZ, false));
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(false));
                    }
                }
                if(mode.getValue().equalsIgnoreCase("MiniJump")) {
                    mc.thePlayer.jump();
                    mc.thePlayer.motionY -= .30000001192092879;
                }
                if (mode.getValue().equalsIgnoreCase("Verus MiniJump")) {
                    shouldCrit = true;
                }
            }
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

