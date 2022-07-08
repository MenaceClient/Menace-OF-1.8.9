package dev.menace.module.modules.player;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.utils.player.InventoryUtils;
import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class InvManagerModule extends BaseModule {

	public InvManagerModule() {
		super("InvManager", Category.PLAYER, 0);
	}

	@EventTarget
	public void onUpdate(EventUpdate event) {
		
		int swordSlot = 0;
		int pickSlot = 1;
		int axeSlot = 2;
		int shovelSlot = 3;
		int gappleSlot = 4;
		int blockSlot = 8;
		
		if (!(mc.currentScreen instanceof GuiInventory)) return;

		float damage = InventoryUtils.getDamage(mc.thePlayer.inventoryContainer.getSlot(swordSlot).getStack());

		float pickValue =  InventoryUtils.getToolEffect(mc.thePlayer.inventoryContainer.getSlot(pickSlot).getStack());

		float axeValue =  InventoryUtils.getToolEffect(mc.thePlayer.inventoryContainer.getSlot(axeSlot).getStack());

		float shovelValue =  InventoryUtils.getToolEffect(mc.thePlayer.inventoryContainer.getSlot(shovelSlot).getStack());

		int blockValue = mc.thePlayer.inventoryContainer.getSlot(blockSlot).getStack() != null ?
				mc.thePlayer.inventoryContainer.getSlot(blockSlot).getStack().stackSize : 0;

		for(int i = 9; i < 45; ++i) {

			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
				
				//Sword
				if (InventoryUtils.getDamage(is) > damage && (is.getItem() instanceof ItemSword)) {
					InventoryUtils.swap(i, swordSlot);
					damage = InventoryUtils.getDamage(is);
				}
				
				//Pickaxe
				if (InventoryUtils.getToolEffect(is) > pickValue && is.getItem() instanceof ItemPickaxe) {
					InventoryUtils.swap(i, pickSlot);
					pickValue = InventoryUtils.getToolEffect(is);
				}
				
				//Axe
				if (InventoryUtils.getToolEffect(is) > axeValue && is.getItem() instanceof ItemAxe) {
					InventoryUtils.swap(i, axeSlot);
					axeValue = InventoryUtils.getToolEffect(is);
				}
				
				//Shovel
				if (InventoryUtils.getToolEffect(is) > shovelValue && is.getItem() instanceof ItemSpade) {
					InventoryUtils.swap(i, shovelSlot);
					shovelValue = InventoryUtils.getToolEffect(is);
				}
				
				//Gapple
				if (is.getItem() == Items.golden_apple && (mc.thePlayer.inventoryContainer.getSlot(gappleSlot).getStack() == null
						|| mc.thePlayer.inventoryContainer.getSlot(gappleSlot).getStack().stackSize < 64)) {

					if (mc.thePlayer.inventoryContainer.getSlot(gappleSlot).getStack() == null) {
						InventoryUtils.swap(i, gappleSlot);
					} else {
						InventoryUtils.shiftClick(i, mc.thePlayer.inventoryContainer.windowId);
					}
				}
				
				//Blocks
				if (is.stackSize > blockValue && is.getItem() instanceof ItemBlock && !InventoryUtils.BLOCK_BLACKLIST.contains(((ItemBlock)is.getItem()).getBlock())) {
					InventoryUtils.swap(i, blockSlot);
					blockValue = is.stackSize;
				}
				
			}
			
		}

	}

}
