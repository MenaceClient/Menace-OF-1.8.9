package dev.menace.module.modules.combat;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventSendPacket;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.Category;
import dev.menace.module.Module;
import dev.menace.module.settings.StringSetting;
import dev.menace.utils.entity.self.PlayerUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;

import java.util.ArrayList;

public class Criticals extends Module {
	
	private String mode;
	
	//Settings
    StringSetting setMode;
	
    public Criticals() {
        super("Criticals", 0, Category.COMBAT);
    }

    @Override
    public void setup() {
        ArrayList<String> options = new ArrayList<>();
        options.add("Packet");
        options.add("NCP");
        options.add("MiniJump");
        setMode = new StringSetting("Mode", this, "Packet", options);
        this.rSetting(setMode);
    }

    @Override
    public void onEnable() {
    	if (MC.theWorld == null) {
    		this.toggle();
    		return;
    	}
    	super.onEnable();
    }
    
    @EventTarget
    public void onUpdate(EventUpdate event) {
        mode = setMode.getString();
        this.setDisplayName("Criticals §7[" + mode + "]");
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if(canCrit()) {
            if (event.getPacket() instanceof C02PacketUseEntity) {
                C02PacketUseEntity packet = (C02PacketUseEntity)event.getPacket();
                if(packet.getAction() == C02PacketUseEntity.Action.ATTACK) {
                    if(mode.equalsIgnoreCase("Packet")) {
                        MC.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(MC.thePlayer.posX, MC.thePlayer.posY + 0.0625, MC.thePlayer.posZ, true));
                        MC.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(false));
                        MC.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(MC.thePlayer.posX, MC.thePlayer.posY + 1.1E-5, MC.thePlayer.posZ, false));
                        MC.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(false));
                    }
                }
                if (mode.equalsIgnoreCase("NCP")) {
                	MC.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(MC.thePlayer.posX, MC.thePlayer.posY + 0.11, MC.thePlayer.posZ, false));
                	MC.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(MC.thePlayer.posX, MC.thePlayer.posY + 0.1100013579, MC.thePlayer.posZ, false));
                	MC.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(MC.thePlayer.posX, MC.thePlayer.posY + 0.0000013579, MC.thePlayer.posZ, false));
                }
                if(mode.equalsIgnoreCase("MiniJump")) {
                    MC.thePlayer.jump();
                    MC.thePlayer.motionY -= .30000001192092879;
                }
            }
        }
    }

    private boolean canCrit() {
        return !PlayerUtils.isInLiquid() && MC.thePlayer.onGround;
    }
}

