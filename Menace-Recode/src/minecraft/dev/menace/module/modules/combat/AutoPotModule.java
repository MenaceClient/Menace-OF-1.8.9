package dev.menace.module.modules.combat;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventPreMotion;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.settings.SliderSetting;
import dev.menace.utils.player.PacketUtils;
import dev.menace.utils.timer.MSTimer;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AutoPotModule extends BaseModule {

    MSTimer delayTimer = new MSTimer();
    int stage;

    SliderSetting delay;

    public AutoPotModule() {
        super("AutoPot", Category.COMBAT, 0);
    }

    @Override
    public void setup() {
        delay = new SliderSetting("Delay", true, 100, 100, 1000, 50, true);
        this.rSetting(delay);
        super.setup();
    }

    @Override
    public void onEnable() {
        delayTimer.reset();
        super.onEnable();
    }

    @EventTarget
    public void onPre(EventPreMotion event) {
        if (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed) != null || !mc.thePlayer.onGround || !delayTimer.hasTimePassed(1000L + delay.getValueL())) return;

        int potionSlot = -1;

        for (int i = 0; i < 9; ++i) {
            ItemStack s = mc.thePlayer.inventory.getStackInSlot(i);
            if (s != null && s.getItem() instanceof ItemPotion && ItemPotion.isSplash(s.getMetadata())) {
                List<Potion> effects = new ArrayList<>();
                ItemPotion potion = (ItemPotion) s.getItem();
                potion.getEffects(s.getMetadata()).forEach(p -> effects.add(this.getPotionByID(p.getPotionID())));

                if (!effects.contains(Potion.moveSpeed)) continue;

                potionSlot = i;
            }
        }

        if (potionSlot == -1) return;

        int oldSlot = mc.thePlayer.inventory.currentItem;

        event.setPitch(90);
        mc.thePlayer.inventory.currentItem = potionSlot;
        mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getStackInSlot(mc.thePlayer.inventory.currentItem));
        mc.thePlayer.inventory.currentItem = oldSlot;
        //PacketUtils.sendPacket(new C09PacketHeldItemChange(potionSlot));
        //PacketUtils.sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getStackInSlot(potionSlot)));
        //PacketUtils.sendPacket(new C09PacketHeldItemChange(oldSlot));

        delayTimer.reset();
    }

    @Contract(pure = true)
    private @Nullable Potion getPotionByID(int id) {
        for (Potion p : Potion.field_180150_I.values()) {
            if (p.id == id) return p;
        }
        return null;
    }

}
