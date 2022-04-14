package dev.menace.module.modules.combat;

import java.util.ArrayList;
import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventAttack;
import dev.menace.event.events.EventSendPacket;
import dev.menace.module.Category;
import dev.menace.module.Module;
import dev.menace.module.settings.StringSetting;
import dev.menace.utils.misc.ChatUtils;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C0BPacketEntityAction;

public class WTap extends Module {

	//Settings
	StringSetting mode;
	
	public WTap() {
		super("WTap", 0, Category.COMBAT);
	}
	
	@Override
	public void setup() {
		ArrayList<String> op = new ArrayList<String>();
		op.add("WTap");
		op.add("Packet");
		op.add("DoublePacket");
		mode = new StringSetting("Mode", this, "WTap", op);
		this.rSetting(mode);
	}
	
	@EventTarget
	public void onAttack(EventAttack event) {
		if (mode.getString().equalsIgnoreCase("WTap")) {
            if (MC.thePlayer.isSprinting()) {
                MC.thePlayer.setSprinting(false);
            }
            MC.getNetHandler().addToSendQueue(new C0BPacketEntityAction(MC.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
            MC.thePlayer.setServerSprintState(true);
		}
		
		if (mode.getString().equalsIgnoreCase("Packet")) {
            if (MC.thePlayer.isSprinting()) {
                MC.thePlayer.setSprinting(false);
            }
            MC.getNetHandler().addToSendQueue(new C0BPacketEntityAction(MC.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
            MC.getNetHandler().addToSendQueue(new C0BPacketEntityAction(MC.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
            MC.thePlayer.setServerSprintState(true);
		}
		
		if (mode.getString().equalsIgnoreCase("DoublePacket")) {
			if (MC.thePlayer.isSprinting()) {
                MC.thePlayer.setSprinting(false);
            }
			MC.getNetHandler().addToSendQueue(new C0BPacketEntityAction(MC.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
            MC.getNetHandler().addToSendQueue(new C0BPacketEntityAction(MC.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
            MC.getNetHandler().addToSendQueue(new C0BPacketEntityAction(MC.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
            MC.getNetHandler().addToSendQueue(new C0BPacketEntityAction(MC.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
            MC.thePlayer.setServerSprintState(true);
		}
	}
}
