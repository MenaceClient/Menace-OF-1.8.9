package dev.menace.module.modules.player;

import java.util.HashMap;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.Category;
import dev.menace.module.Module;
import dev.menace.module.settings.BoolSetting;
import dev.menace.module.settings.DoubleSetting;
import dev.menace.utils.entity.self.InventoryUtil;
import dev.menace.utils.misc.MSTimer;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;

public class InvManager extends Module {

    public static boolean isWorking = false;
    public MSTimer openInvDelayTimer = new MSTimer();
    private final HashMap<ItemStack, Long> itemInvTime = new HashMap<>();
    public static final int BOW = 2;
    
    //Settings
    BoolSetting openInvSet;
    BoolSetting keepToolsSet;
    BoolSetting checkRealItemsSet;
    BoolSetting preferSwordsSet;
    DoubleSetting delay;
    DoubleSetting cdelay;
	
	public InvManager() {
		super("InvManager", 0, Category.PLAYER);
	}
	
	@Override
	public void setup() {
		openInvSet = new BoolSetting("OpenInv", this, false);
		keepToolsSet = new BoolSetting("KeepTools", this, false);
		checkRealItemsSet = new BoolSetting("CheckRealItem", this, false);
		preferSwordsSet = new BoolSetting("PreferSwords", this, false);
		delay = new DoubleSetting("OpenInvDelay", this, 75, 0, 175);
		cdelay = new DoubleSetting("ClickDelay", this, 115, 1, 500);
        this.rSetting(openInvSet);
        this.rSetting(keepToolsSet);
        this.rSetting(checkRealItemsSet);
        this.rSetting(preferSwordsSet);
        this.rSetting(delay);
        this.rSetting(cdelay);
	}
	
    @EventTarget
    public void onUpdate(EventUpdate e) {
    	
    	Boolean openInv = openInvSet.isChecked();
    	Boolean keepTools = keepToolsSet.isChecked();
    	Boolean checkRealItem = checkRealItemsSet.isChecked();
    	Boolean preferSwords = preferSwordsSet.isChecked();
    	double openInvDelay = delay.getValue();
    	double clickDelay = cdelay.getValue();
    	
    	this.setDisplayName("InvManager §7[" + "OpenInv: " + openInv + ", " + "ClickDelay: " + (int)clickDelay + "]");
        for (int i = 0; i < 45; ++i) {
            if (this.MC.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack stack = this.MC.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (!this.itemInvTime.containsKey(stack)) {
                    this.itemInvTime.put(stack, System.currentTimeMillis());
                }
            }
        }

        if (this.MC.theWorld != null) {
            if (!(this.MC.currentScreen instanceof GuiChest)) {
                if (!isWorking) {
                    if (openInv && !(this.MC.currentScreen instanceof GuiInventory)) {
                        this.openInvDelayTimer.reset();
                    } else if (!openInv || this.openInvDelayTimer.hasTimePassed((long) openInvDelay)) {
                        isWorking = true;
                        (new Thread(() -> {
                            try {
                                int bestWeaponSlot = InventoryUtil.getStrongestWeapon(true, preferSwords);
                                int bestBowSlot = InventoryUtil.getBestBow(true);
                                int bestFoodSlot = InventoryUtil.getBestFood();
                                if (bestWeaponSlot != 36 && bestWeaponSlot != -1 && this.itemInvTime.containsKey(this.MC.thePlayer.inventoryContainer.getSlot(bestWeaponSlot).getStack()) && System.currentTimeMillis() - this.itemInvTime.get(this.MC.thePlayer.inventoryContainer.getSlot(bestWeaponSlot).getStack()) >= 500L) {
                                    this.swapItem(bestWeaponSlot, 0);
                                    this.itemInvTime.remove(this.MC.thePlayer.inventoryContainer.getSlot(bestWeaponSlot).getStack());

                                    try {
                                        Thread.sleep((long) clickDelay);
                                    } catch (Exception ignored) {
                                    }
                                }

                                if (bestBowSlot != 38 && bestBowSlot != -1 && this.itemInvTime.containsKey(this.MC.thePlayer.inventoryContainer.getSlot(bestBowSlot).getStack()) && System.currentTimeMillis() - this.itemInvTime.get(this.MC.thePlayer.inventoryContainer.getSlot(bestBowSlot).getStack()) >= 500L) {
                                    this.swapItem(bestBowSlot, 2);
                                    this.itemInvTime.remove(this.MC.thePlayer.inventoryContainer.getSlot(bestBowSlot).getStack());

                                    try {
                                        Thread.sleep((long) clickDelay);
                                    } catch (Exception ignored) {
                                    }
                                }

                                if (bestFoodSlot != 39 && bestFoodSlot != -1 && this.itemInvTime.containsKey(this.MC.thePlayer.inventoryContainer.getSlot(bestFoodSlot).getStack()) && System.currentTimeMillis() - this.itemInvTime.get(this.MC.thePlayer.inventoryContainer.getSlot(bestFoodSlot).getStack()) >= 500L) {
                                    this.swapItem(bestFoodSlot, 3);
                                    this.itemInvTime.remove(this.MC.thePlayer.inventoryContainer.getSlot(bestFoodSlot).getStack());

                                    try {
                                        Thread.sleep((long) clickDelay);
                                    } catch (Exception ignored) {
                                    }
                                }

                                for (int i = 9; i < this.MC.thePlayer.inventoryContainer.getInventory().size(); ++i) {
                                    if (i != bestWeaponSlot && i != bestBowSlot && i != bestFoodSlot) {
                                        boolean isRodInSlot = this.MC.thePlayer.inventoryContainer.getInventory().get(37) != null && this.MC.thePlayer.inventoryContainer.getInventory().get(37).getItem() == Items.fishing_rod;
                                        boolean areBlocksInSlot = this.MC.thePlayer.inventoryContainer.getInventory().get(40) != null && InventoryUtil.isItemBlock((ItemStack) this.MC.thePlayer.inventoryContainer.getInventory().get(40));
                                        ItemStack item = this.MC.thePlayer.inventoryContainer.getInventory().get(i);
                                        if (this.itemInvTime.containsKey(item) && System.currentTimeMillis() - this.itemInvTime.get(item) >= 500L && item != null) {
                                            if (!isRodInSlot && item.getItem() == Items.fishing_rod) {
                                                this.swapItem(i, 1);
                                                this.itemInvTime.remove(item);
                                                try {
                                                    Thread.sleep((long) clickDelay);
                                                } catch (Exception ignored) {
                                                }
                                            } else if (!areBlocksInSlot && InventoryUtil.isItemBlock(item)) {
                                                this.swapItem(i, 4);
                                                this.itemInvTime.remove(item);

                                                try {
                                                    Thread.sleep((long) clickDelay);
                                                } catch (Exception ignored) {
                                                }
                                            } else if (InventoryUtil.isUselessItem(item)) {
                                                this.dropSlot(i);
                                            } else if (InventoryUtil.isUsefultem(item) && !(item.getItem() instanceof ItemArmor) && (i != 36 || !(item.getItem() instanceof ItemSword) && !(item.getItem() instanceof ItemTool)) && (i != 37 || item.getItem() != Items.fishing_rod) && (i != 38 || item.getItem() != Items.bow)) {
                                                int bestPickaxe = InventoryUtil.getBestPickaxe(true);
                                                int bestAxe = InventoryUtil.getBestAxe(true);
                                                int bestSpade = InventoryUtil.getBestSpade(true);
                                                if (keepTools && item.getItem() instanceof ItemTool) {
                                                    try {
                                                        Thread.sleep((long) clickDelay);
                                                    } catch (Exception ignored) {
                                                    }

                                                    if (openInv && !(this.MC.currentScreen instanceof GuiInventory)) {
                                                        isWorking = false;
                                                        return;
                                                    }

                                                    if ((bestPickaxe != -1 && item.getItem() instanceof ItemPickaxe && i != bestPickaxe || bestSpade != -1 && item.getItem() instanceof ItemSpade && i != bestSpade || bestAxe != -1 && item.getItem() instanceof ItemAxe && i != bestAxe) && !(MC.thePlayer.inventoryContainer.getSlot(i).getStack().getItem() instanceof ItemArmor)) {
                                                        this.dropSlot(i);
                                                    }
                                                } else {
                                                    try {
                                                        Thread.sleep((long) clickDelay);
                                                    } catch (Exception ignored) {
                                                    }

                                                    if (openInv && !(MC.currentScreen instanceof GuiInventory)) {
                                                        isWorking = false;
                                                        return;
                                                    }

                                                    if (!(MC.thePlayer.inventoryContainer.getSlot(i).getStack().getItem() instanceof ItemArmor) && (!checkRealItem || !MC.thePlayer.inventoryContainer.getSlot(i).getStack().hasDisplayName())) {
                                                        this.dropSlot(i);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            } catch (Exception ignored) {
                            }

                            isWorking = false;
                        })).start();
                    }
                }
            }
        }
    }
	
    public void dropSlot(int slot) {
        MC.thePlayer.inventoryContainer.slotClick(slot, 0, 4, MC.thePlayer);
        MC.playerController.windowClick(MC.thePlayer.inventoryContainer.windowId, slot, 1, 4, MC.thePlayer);
    }

    public void swapItem(int from, int to) {
        MC.playerController.windowClick(MC.thePlayer.inventoryContainer.windowId, from, to, 2, MC.thePlayer);
    }

}
