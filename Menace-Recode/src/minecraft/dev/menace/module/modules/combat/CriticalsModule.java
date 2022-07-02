package dev.menace.module.modules.combat;

import java.util.ArrayList;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventSendPacket;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.settings.ListSetting;
import dev.menace.utils.player.PlayerUtils;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;

public class CriticalsModule extends BaseModule {
	
	//Settings
    ListSetting mode;
	
    public CriticalsModule() {
        super("Criticals", Category.COMBAT, 0);
    }

    @Override
    public void setup() {
        mode = new ListSetting("Mode", true, "Packet", new String[] {"Packet", "MiniJump"});
        this.rSetting(mode);
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        this.setDisplayName(mode.getValue());
        if(canCrit()) {
            if (event.getPacket() instanceof C02PacketUseEntity) {
                C02PacketUseEntity packet = (C02PacketUseEntity)event.getPacket();
                if(packet.getAction() == C02PacketUseEntity.Action.ATTACK) {
                    if(mode.getValue().equalsIgnoreCase("Packet")) {
                        MC.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(MC.thePlayer.posX, MC.thePlayer.posY + 0.0625, MC.thePlayer.posZ, true));
                        MC.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(false));
                        MC.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(MC.thePlayer.posX, MC.thePlayer.posY + 1.1E-5, MC.thePlayer.posZ, false));
                        MC.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(false));
                    }
                }
                if(mode.getValue().equalsIgnoreCase("MiniJump")) {
                    MC.thePlayer.jump();
                    MC.thePlayer.motionY -= .30000001192092879;
                }
            }
        }
    }

    private boolean canCrit() {
        return MC.thePlayer != null && !PlayerUtils.isInLiquid() && MC.thePlayer.onGround;
    }
}

