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

import java.util.ArrayList;
import java.util.List;

public class ChestStealerModule extends BaseModule {

	List<Slot> slotList = new ArrayList<>();
	MSTimer delayTimer = new MSTimer();
	long nextDelay;
	
	ToggleSetting closeScreen;
	public SliderSetting minDelay;
	public SliderSetting maxDelay;
	ToggleSetting randomize;
	
	public ChestStealerModule() {
		super("ChestStealer", Category.WORLD, 0);
	}

	@Override
	public void setup() {
		minDelay = new SliderSetting("Delay Min", true, 90, 0, 1000, 10, true) {
			@Override
			public void constantCheck() {
				if (Menace.instance.moduleManager.chestStealerModule.maxDelay.getValue() < this.getValue()) {
					this.setValue(Menace.instance.moduleManager.chestStealerModule.maxDelay.getValue());
				}
			}
		};
		maxDelay = new SliderSetting("Delay Max", true, 100, 0, 1000, 10, true) {
			@Override
			public void constantCheck() {
				if (Menace.instance.moduleManager.chestStealerModule.minDelay.getValue() > this.getValue()) {
					this.setValue(Menace.instance.moduleManager.chestStealerModule.minDelay.getValue());
				}
			}
		};
		closeScreen = new ToggleSetting("CloseScreen", true, false);
		randomize = new ToggleSetting("Randomize", true, true);
		this.rSetting(minDelay);
		this.rSetting(maxDelay);
		this.rSetting(closeScreen);
		this.rSetting(randomize);
		super.setup();
	}
	
	@Override
	public void onEnable() {
		delayTimer.reset();
		nextDelay = MathUtils.randLong(minDelay.getValueL(), maxDelay.getValueL());
		slotList.clear();
		super.onEnable();
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		
		if (MC.currentScreen instanceof GuiChest && closeScreen.getValue() && (isEmpty((GuiChest) MC.currentScreen) || isInvFull())) {
			MC.thePlayer.closeScreen();
		}
		
		if (!(MC.currentScreen instanceof GuiChest) 
				|| isEmpty((GuiChest) MC.currentScreen) 
				|| isInvFull()
				|| !delayTimer.hasTimePassed(nextDelay)) 
			{return;}
		
		GuiChest gui = (GuiChest) MC.currentScreen;

		if (slotList.isEmpty()) {
			for (int i = 0; i < gui.inventoryRows * 9; i++) {
				Slot slot = gui.inventorySlots.getSlot(i);
				if (slot.getHasStack()) {
					slotList.add(slot);
				}
			}
		}

		int i = randomize.getValue() ? MathUtils.randInt(0, slotList.size()) : 0;

		Slot slot = slotList.get(i);
		slotList.remove(slot);
		InventoryUtils.shiftClick(slot.slotNumber, gui.inventorySlots.windowId);
		delayTimer.reset();
		nextDelay = MathUtils.randLong(minDelay.getValueL(), maxDelay.getValueL());
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