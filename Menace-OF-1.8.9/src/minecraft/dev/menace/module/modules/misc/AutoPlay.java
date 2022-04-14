package dev.menace.module.modules.misc;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventReceivePacket;
import dev.menace.module.Category;
import dev.menace.module.Module;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.misc.MSTimer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S2FPacketSetSlot;

public class AutoPlay extends Module {

	private int clickState = 0;
	
	public AutoPlay() {
		super("AutoPlay", 0, Category.MISC);
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		clickState = 0;
	}
	
	@EventTarget
	public void onRecievePacket(EventReceivePacket event) {
		if (event.getPacket() instanceof S2FPacketSetSlot) {
			S2FPacketSetSlot packet = (S2FPacketSetSlot) event.getPacket();
			ItemStack item = packet.func_149174_e();
			if (item == null) return;
			int windowId = packet.func_149175_c();
			int slot = packet.func_149173_d();
			String itemName = item.getUnlocalizedName();
			String displayName = item.getDisplayName();
			
			if (clickState == 0 && windowId == 0 && slot == 43 && itemName.equalsIgnoreCase("paper")) {
				MC.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(7));
				
				for(int i = 0; i == 2; i++) {
					MC.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(MC.thePlayer.inventory.getCurrentItem()));
				}
			}
		} else if (event.getPacket() instanceof S02PacketChat) {
			ChatUtils.message("Join failed! trying again...");
			// connect failed so try to join again

			MC.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(7));
			
			for(int i = 0; i == 2; i++) {
				MC.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(MC.thePlayer.inventory.getCurrentItem()));
			}
        }
	}
}
