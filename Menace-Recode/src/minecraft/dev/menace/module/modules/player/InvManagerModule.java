package dev.menace.module.modules.player;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventSendPacket;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.settings.SliderSetting;
import dev.menace.module.settings.ToggleSetting;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.misc.MathUtils;
import dev.menace.utils.player.InventoryUtils;
import dev.menace.utils.player.PacketUtils;
import dev.menace.utils.timer.MSTimer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.*;
import net.minecraft.network.play.client.C0DPacketCloseWindow;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InvManagerModule extends BaseModule {

    public SliderSetting minDelay;
    public SliderSetting maxDelay;
    List<Slot> slotList = new ArrayList<>();
    MSTimer delayTimer = new MSTimer();
    long nextDelay;
    float damage, pickValue, axeValue, shovelValue = 0;
    int blockValue = 0;
    int swordSlot = 0;
    int pickSlot = 1;
    int axeSlot = 2;
    int shovelSlot = 3;
    int gappleSlot = 4;
    int blockSlot = 8;
    ToggleSetting inInv;
    ToggleSetting closeScreen;
    ToggleSetting randomize;
    SliderSetting swordSet;
    SliderSetting pickSet;
    SliderSetting axeSet;
    SliderSetting shovelSet;
    SliderSetting gapSet;
    SliderSetting blockSet;
    private final int threshold = 10;

    public InvManagerModule() {
        super("InvManager", "Cleans your inventory", Category.PLAYER, 0);
    }

    @Override
    public void setup() {
        minDelay = new SliderSetting("Delay Min", true, 90, 0, 1000, 10, true) {
            @Override
            public void constantCheck() {
                if (Menace.instance.moduleManager.invManagerModule.maxDelay.getValue() < this.getValue()) {
                    this.setValue(Menace.instance.moduleManager.invManagerModule.maxDelay.getValue());
                }
            }
        };
        maxDelay = new SliderSetting("Delay Max", true, 100, 0, 1000, 10, true) {
            @Override
            public void constantCheck() {
                if (Menace.instance.moduleManager.invManagerModule.minDelay.getValue() > this.getValue()) {
                    this.setValue(Menace.instance.moduleManager.invManagerModule.minDelay.getValue());
                }
            }
        };
        randomize = new ToggleSetting("Randomize", true, true);
        inInv = new ToggleSetting("In Inventory", true, true);
        closeScreen = new ToggleSetting("CloseScreen", true, false) {
            @Override
            public void constantCheck() {
                this.setVisible(Menace.instance.moduleManager.invManagerModule.inInv.getValue());
            }
        };
        swordSet = new SliderSetting("Sword Slot", true, 1, 1, 9, true);
        pickSet = new SliderSetting("Pick Slot", true, 2, 1, 9, true);
        axeSet = new SliderSetting("Axe Slot", true, 3, 1, 9, true);
        shovelSet = new SliderSetting("Shovel Slot", true, 4, 1, 9, true);
        gapSet = new SliderSetting("Gapple Slot", true, 5, 1, 9, true);
        blockSet = new SliderSetting("Blocks Slot", true, 9, 1, 9, true);
        this.rSetting(minDelay);
        this.rSetting(maxDelay);
        this.rSetting(inInv);
        this.rSetting(closeScreen);
        this.rSetting(randomize);
        this.rSetting(swordSet);
        this.rSetting(pickSet);
        this.rSetting(axeSet);
        this.rSetting(shovelSet);
        this.rSetting(gapSet);
        this.rSetting(blockSet);
        super.setup();
    }

    @Override
    public void onEnable() {
        swordSlot = swordSet.getValueI() - 1;
        pickSlot = pickSet.getValueI() - 1;
        axeSlot = axeSet.getValueI() - 1;
        shovelSlot = shovelSet.getValueI() - 1;
        gappleSlot = gapSet.getValueI() - 1;
        blockSlot = blockSet.getValueI() - 1;
        delayTimer.reset();
        nextDelay = MathUtils.randLong(minDelay.getValueL(), maxDelay.getValueL());
        slotList.clear();
        super.onEnable();
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {

        damage = InventoryUtils.getDamage(mc.thePlayer.inventory.getStackInSlot(swordSlot));

        pickValue = InventoryUtils.getToolEffect(mc.thePlayer.inventory.getStackInSlot(pickSlot));

        axeValue = InventoryUtils.getToolEffect(mc.thePlayer.inventory.getStackInSlot(axeSlot));

        shovelValue = InventoryUtils.getToolEffect(mc.thePlayer.inventory.getStackInSlot(shovelSlot));

        blockValue = mc.thePlayer.inventory.getStackInSlot(blockSlot) != null ?
                mc.thePlayer.inventory.getStackInSlot(blockSlot).stackSize : 0;

        if (!(mc.currentScreen instanceof GuiInventory) && inInv.getValue()) {
            return;
        }

        if (!Menace.instance.moduleManager.killAuraModule.trget.isEmpty() || Menace.instance.moduleManager.scaffoldModule.isToggled() || mc.thePlayer.hurtTime > 0 || (mc.currentScreen != null && (!(mc.currentScreen instanceof GuiInventory) || !inInv.getValue()))) {
            return;
        }

        scanInv();

        if (slotList.isEmpty() || !delayTimer.hasTimePassed(nextDelay)) {
            if (slotList.isEmpty() && closeScreen.getValue()) {
                mc.thePlayer.closeScreen();
            }

            return;
        }

        //PacketUtils.sendPacketNoEvent(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));

        int o = randomize.getValue() ? MathUtils.randInt(0, slotList.size()) : 0;

        Slot slot = slotList.get(o);
        slotList.remove(slot);
        int i = slot.slotNumber;
        ItemStack is = slot.getStack();
        int durabilityThreshold = 50;

        if (is.getItem() instanceof ItemSword && InventoryUtils.getDamage(is) <= 0) {
            //DONT DROP MY FUCKING OP SWORD
            return;
        }

        //Sword
        if (is.getItem() instanceof ItemSword && InventoryUtils.getDamage(is) > damage && is.getMaxDamage() - is.getItemDamage() > durabilityThreshold) {
            InventoryUtils.swap(i, swordSlot);
            if (!inInv.getValue()) PacketUtils.sendPacketNoEvent(new C0DPacketCloseWindow());
            nextDelay = MathUtils.randLong(minDelay.getValueL(), maxDelay.getValueL());
            delayTimer.reset();
            return;
        }


        //Pickaxe
        if (InventoryUtils.getToolEffect(is) > pickValue && is.getItem() instanceof ItemPickaxe) {
            InventoryUtils.swap(i, pickSlot);
            if (!inInv.getValue()) PacketUtils.sendPacketNoEvent(new C0DPacketCloseWindow());
            nextDelay = MathUtils.randLong(minDelay.getValueL(), maxDelay.getValueL());
            delayTimer.reset();
            return;
        }

        //Axe
        if (InventoryUtils.getToolEffect(is) > axeValue && is.getItem() instanceof ItemAxe) {
            InventoryUtils.swap(i, axeSlot);
            if (!inInv.getValue()) PacketUtils.sendPacketNoEvent(new C0DPacketCloseWindow());
            nextDelay = MathUtils.randLong(minDelay.getValueL(), maxDelay.getValueL());
            delayTimer.reset();
            return;
        }

        //Shovel
        if (InventoryUtils.getToolEffect(is) > shovelValue && is.getItem() instanceof ItemSpade) {
            InventoryUtils.swap(i, shovelSlot);
            if (!inInv.getValue()) PacketUtils.sendPacketNoEvent(new C0DPacketCloseWindow());
            nextDelay = MathUtils.randLong(minDelay.getValueL(), maxDelay.getValueL());
            delayTimer.reset();
            return;
        }

        //Gapple
        if (is.getItem() instanceof ItemAppleGold && (mc.thePlayer.inventory.getStackInSlot(gappleSlot) == null || mc.thePlayer.inventory.getStackInSlot(gappleSlot).stackSize < 64)) {
            InventoryUtils.swap(i, gappleSlot);
            if (!inInv.getValue()) PacketUtils.sendPacketNoEvent(new C0DPacketCloseWindow());
            nextDelay = MathUtils.randLong(minDelay.getValueL(), maxDelay.getValueL());
            delayTimer.reset();
            return;
        }

        //Blocks
        if (is.stackSize > blockValue && is.getItem() instanceof ItemBlock && !InventoryUtils.BLOCK_BLACKLIST.contains(((ItemBlock) is.getItem()).getBlock())) {
            InventoryUtils.swap(i, blockSlot);
            if (!inInv.getValue()) PacketUtils.sendPacketNoEvent(new C0DPacketCloseWindow());
            nextDelay = MathUtils.randLong(minDelay.getValueL(), maxDelay.getValueL());
            delayTimer.reset();
            return;
        }

        //Armour
        if (is.getItem() instanceof ItemArmor) {
            ItemArmor armour = (ItemArmor) is.getItem();
            if (armour.armorType == 0 && InventoryUtils.getArmorStrength(is) > InventoryUtils.getArmorStrength(mc.thePlayer.inventory.armorInventory[3])) {
                InventoryUtils.drop(5);
                InventoryUtils.shiftClick(i, mc.thePlayer.inventoryContainer.windowId);
            } else if (armour.armorType == 1 && InventoryUtils.getArmorStrength(is) > InventoryUtils.getArmorStrength(mc.thePlayer.inventory.armorInventory[2])) {
                InventoryUtils.drop(6);
                InventoryUtils.shiftClick(i, mc.thePlayer.inventoryContainer.windowId);
            } else if (armour.armorType == 2 && InventoryUtils.getArmorStrength(is) > InventoryUtils.getArmorStrength(mc.thePlayer.inventory.armorInventory[1])) {
                InventoryUtils.drop(7);
                InventoryUtils.shiftClick(i, mc.thePlayer.inventoryContainer.windowId);
            } else if (armour.armorType == 3 && InventoryUtils.getArmorStrength(is) > InventoryUtils.getArmorStrength(mc.thePlayer.inventory.armorInventory[0])) {
                InventoryUtils.drop(8);
                InventoryUtils.shiftClick(i, mc.thePlayer.inventoryContainer.windowId);
            } else {
                InventoryUtils.drop(i);
            }
            if (!inInv.getValue()) PacketUtils.sendPacketNoEvent(new C0DPacketCloseWindow());
            nextDelay = MathUtils.randLong(minDelay.getValueL(), maxDelay.getValueL());
            delayTimer.reset();
            return;
        }

        //Clean
        if (InventoryUtils.TRASH.contains(is.getItem()) || is.getItem() instanceof ItemBlock && InventoryUtils.BLOCK_BLACKLIST.contains(((ItemBlock) is.getItem()).getBlock())) {
            InventoryUtils.drop(i);
            if (!inInv.getValue()) PacketUtils.sendPacketNoEvent(new C0DPacketCloseWindow());
            nextDelay = MathUtils.randLong(minDelay.getValueL(), maxDelay.getValueL());
            delayTimer.reset();
        } else if (i != 36 + swordSlot && is.getItem() instanceof ItemSword) {
            InventoryUtils.drop(i);
            if (!inInv.getValue()) PacketUtils.sendPacketNoEvent(new C0DPacketCloseWindow());
            nextDelay = MathUtils.randLong(minDelay.getValueL(), maxDelay.getValueL());
            delayTimer.reset();
        } else if (i != 36 + pickSlot && is.getItem() instanceof ItemPickaxe) {
            InventoryUtils.drop(i);
            if (!inInv.getValue()) PacketUtils.sendPacketNoEvent(new C0DPacketCloseWindow());
            nextDelay = MathUtils.randLong(minDelay.getValueL(), maxDelay.getValueL());
            delayTimer.reset();
        } else if (i != 36 + axeSlot && is.getItem() instanceof ItemAxe) {
            InventoryUtils.drop(i);
            if (!inInv.getValue()) PacketUtils.sendPacketNoEvent(new C0DPacketCloseWindow());
            nextDelay = MathUtils.randLong(minDelay.getValueL(), maxDelay.getValueL());
            delayTimer.reset();
        } else if (i != 36 + shovelSlot && is.getItem() instanceof ItemSpade) {
            InventoryUtils.drop(i);
            if (!inInv.getValue()) PacketUtils.sendPacketNoEvent(new C0DPacketCloseWindow());
            nextDelay = MathUtils.randLong(minDelay.getValueL(), maxDelay.getValueL());
            delayTimer.reset();
        }

    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if (event.getPacket() instanceof C0DPacketCloseWindow) {
            delayTimer.reset();
            nextDelay = MathUtils.randLong(minDelay.getValueL(), maxDelay.getValueL());
            slotList.clear();
        }
    }

    private void scanInv() {
        slotList.clear();
        for (int i = 9; i < 45; ++i) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                slotList.add(mc.thePlayer.inventoryContainer.getSlot(i));
            }
        }
    }

}