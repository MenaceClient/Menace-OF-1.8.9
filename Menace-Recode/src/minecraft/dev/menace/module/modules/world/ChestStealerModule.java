package dev.menace.module.modules.world;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.settings.SliderSetting;
import dev.menace.module.settings.ToggleSetting;
import dev.menace.utils.misc.MathUtils;
import dev.menace.utils.player.InventoryUtils;
import dev.menace.utils.timer.MSTimer;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ChestStealerModule extends BaseModule {

	MSTimer delayTimer = new MSTimer();
	long nextDelay;
	
	ToggleSetting closeScreen;
	public SliderSetting minDelay;
	public SliderSetting maxDelay;
	
	public ChestStealerModule() {
		super("ChestStealer", Category.WORLD, 0);
	}

	@Override
	public void setup() {
		minDelay = new SliderSetting("Delay Min", true, 90, 100, 1000, 100, true) {
			@Override
			public void constantCheck() {
				if (Menace.instance.moduleManager.chestStealerModule.maxDelay.getValue() < this.getValue()) {
					this.setValue(Menace.instance.moduleManager.chestStealerModule.maxDelay.getValue());
				}
			}
		};
		maxDelay = new SliderSetting("Delay Max", true, 100, 0, 1000, 100, true) {
			@Override
			public void constantCheck() {
				if (Menace.instance.moduleManager.chestStealerModule.minDelay.getValue() > this.getValue()) {
					this.setValue(Menace.instance.moduleManager.chestStealerModule.minDelay.getValue());
				}
			}
		};
		closeScreen = new ToggleSetting("CloseScreen", true, false);
		this.rSetting(minDelay);
		this.rSetting(maxDelay);
		this.rSetting(closeScreen);
		super.setup();
	}
	
	@Override
	public void onEnable() {
		delayTimer.reset();
		nextDelay = MathUtils.randLong(minDelay.getValueL(), maxDelay.getValueL());
		super.onEnable();
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		
		if (MC.currentScreen instanceof GuiChest && closeScreen.isToggled() && (isEmpty((GuiChest) MC.currentScreen) || isInvFull())) {
			MC.thePlayer.closeScreen();
		}
		
		if (!(MC.currentScreen instanceof GuiChest) 
				|| isEmpty((GuiChest) MC.currentScreen) 
				|| isInvFull()
				|| !delayTimer.hasTimePassed(nextDelay)) 
			{return;}
		
		GuiChest gui = (GuiChest) MC.currentScreen;
		
		for (int i = 0; i < gui.inventoryRows * 9; i++) {
			Slot slot = gui.inventorySlots.getSlot(i);
			if (slot.getHasStack()) {
				InventoryUtils.shiftClick(slot.slotNumber);
				delayTimer.reset();
				nextDelay = MathUtils.randLong(minDelay.getValueL(), maxDelay.getValueL());
				if (nextDelay != 0) {
					return;
				}
			}
		}
	}
	
	private boolean isEmpty(GuiChest gui) {
		for (int i = 0; i < gui.inventoryRows * 9; i++) {
			Slot slot = gui.inventorySlots.getSlot(i);
			if (slot.getHasStack()) {
				return false;
			}
		}
		return true;
	}
	
	private boolean isInvFull() {
        for(int index = 9; index <= 44; ++index) {
             ItemStack stack = MC.thePlayer.inventoryContainer.getSlot(index).getStack();
             if (stack == null) {
                return false;
             }
          }

          return true;
    }
	
}