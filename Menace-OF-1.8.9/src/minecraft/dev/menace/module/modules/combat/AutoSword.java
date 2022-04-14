package dev.menace.module.modules.combat;

import java.util.ArrayList;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventAttack;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.Category;
import dev.menace.module.Module;
import dev.menace.module.settings.StringSetting;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C09PacketHeldItemChange;

public class AutoSword extends Module {

	private String mode;
	
	//Settings
    StringSetting setMode;
	
	public AutoSword() {
		super("Auto Sword", 0, Category.COMBAT);
	}
	
	@Override
	public void setup() {
	    ArrayList<String> options = new ArrayList<>();
        options.add("Legit");
        options.add("Packet");
        setMode = new StringSetting("Mode", this, "Packet", options);
        this.rSetting(setMode);
	}
	
	@EventTarget
	public void onAttack(EventAttack event) {
		
		mode = setMode.getString();
		
		if (mode.equalsIgnoreCase("Packet")) {
			
			
			for (int i=0; i < (MC.thePlayer.inventory.getHotbarSize() - 1); i++) {
				
				if (MC.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemSword
						&& !(MC.thePlayer.getHeldItem().getItem() instanceof ItemSword)) {
					MC.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(i));
					//MC.playerController.sendSlotPacket(MC.thePlayer.inventory.getStackInSlot(i), i);
				}
			}
			
		}
		
		if (mode.equalsIgnoreCase("Legit")) {
			
			for (int i=0; i <= 8; i++) {
				
				if (MC.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemSword
						&& !(MC.thePlayer.getHeldItem().getItem() instanceof ItemSword)) {
					MC.thePlayer.inventory.currentItem = i;
				}
			}
			
		}
		
	}

}
