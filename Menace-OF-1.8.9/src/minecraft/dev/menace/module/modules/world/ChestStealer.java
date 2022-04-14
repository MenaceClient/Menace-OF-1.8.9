package dev.menace.module.modules.world;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventReceivePacket;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.Category;
import dev.menace.module.Module;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Slot;
import net.minecraft.network.play.server.S30PacketWindowItems;

public class ChestStealer extends Module {

	private int contentRecieved = 0;
	
	
	public ChestStealer() {
		super("ChestStealer", 0, Category.WORLD);
	}

	@Override
	public void onEnable() {
		super.onEnable();
		
		contentRecieved = 0;
	}

	@EventTarget
	public void update(EventUpdate event) {
		if (MC.currentScreen == null || !(MC.currentScreen instanceof GuiChest)) return;
		GuiChest screen = (GuiChest) MC.currentScreen;
		
		if (!isEmpty(screen)) {

			for (int i = 0; i < (screen.inventoryRows * 9); ++i) {
				Slot slot = screen.inventorySlots.inventorySlots.get(i);

				if (slot.getStack() != null) {
					
					move(screen, slot);

				}

			}

		} else if (screen.inventorySlots.windowId == contentRecieved && isEmpty(screen)) {
			MC.thePlayer.closeScreen();
		}
	}

	@EventTarget
	public void onPacket(EventReceivePacket event) {

		if (event.getPacket() instanceof S30PacketWindowItems) {
			contentRecieved = ((S30PacketWindowItems) event.getPacket()).func_148911_c();
		}

	}
	
	public void move(GuiChest chest, Slot slot) {
		
		chest.handleMouseClick(slot, slot.slotNumber, 0, 1);
		
	}

	public boolean isEmpty(GuiChest chest) {	
		
		for (int i = 0; i < (chest.inventoryRows * 9); ++i) {
			Slot slot = chest.inventorySlots.inventorySlots.get(i);
					
			if (slot.getStack() != null) {
				return false;
			}

		} 
		
		
		return true;
	}

}
