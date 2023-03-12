package dev.menace.module.modules.combat;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventPreMotion;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.settings.SliderSetting;
import dev.menace.module.settings.ToggleSetting;
import dev.menace.utils.misc.ChatUtils;
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
    int potionSlot;

    SliderSetting delay;
    SliderSetting health;
    ToggleSetting onlyOnGround;

    public AutoPotModule() {
        super("AutoPot", Category.COMBAT, 0);
    }

    @Override
    public void setup() {
        delay = new SliderSetting("Delay", true, 100, 100, 1000, 50, true);
        health = new SliderSetting("Health", true, 10, 1, 20, 1, true);
        onlyOnGround = new ToggleSetting("OnlyOnGround", true, true);
        this.rSetting(delay);
        this.rSetting(health);
        this.rSetting(onlyOnGround);
        super.setup();
    }

    @Override
    public void onEnable() {
        delayTimer.reset();
        stage = 0;
        super.onEnable();
    }

    @EventTarget
    public void onPre(EventPreMotion event) {

        if (stage == 1) {
            event.setPitch(90);
            stage = 2;
            return;
        } else if (stage == 2) {
            throwPot(event, potionSlot);
            stage = 3;
            return;
        } else if (stage == 3) {
            event.setPitch(90);
            stage = 0;
            return;
        }

        handleHealth();

        handleSpeed();

    }

    private void handleHealth() {
        if (mc.thePlayer.getHealth() > health.getValueF() || mc.thePlayer.getActivePotionEffect(Potion.heal) != null || mc.thePlayer.getActivePotionEffect(Potion.regeneration) != null || mc.thePlayer.getActivePotionEffect(Potion.healthBoost) != null || !delayTimer.hasTimePassed(1000L + delay.getValueL()) || stage != 0) return;

        int potionSlot = -1;

        for (int i = 0; i < 9; ++i) {
            ItemStack s = mc.thePlayer.inventory.getStackInSlot(i);
            if (s != null && s.getItem() instanceof ItemPotion && ItemPotion.isSplash(s.getMetadata())) {
                List<Potion> effects = new ArrayList<>();
                ItemPotion potion = (ItemPotion) s.getItem();
                potion.getEffects(s.getMetadata()).forEach(p -> effects.add(this.getPotionByID(p.getPotionID())));

                if (!effects.contains(Potion.heal) && !effects.contains(Potion.regeneration) && !effects.contains(Potion.healthBoost)) continue;

                potionSlot = i;
            }
        }

        if (potionSlot == -1 || (!mc.thePlayer.onGround && onlyOnGround.getValue())) return;

        this.potionSlot = potionSlot;
        stage = 1;
    }

    private void handleSpeed() {
        if (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed) != null || !delayTimer.hasTimePassed(1000L + delay.getValueL()) || stage != 0) return;

        int potionSlot = -1;

        for (int i = 0; i < 9; ++i) {
            ItemStack s = mc.thePlayer.inventory.getStackInSlot(i);
            if (s != null && s.getItem() instanceof ItemPotion && ItemPotion.isSplash(s.getMetadata())) {
                List<Potion> effects = new ArrayList<>();
                ItemPotion potion = (ItemPotion) s.getItem();
                potion.getEffects(s.getMetadata()).forEach(p -> effects.add(this.getPotionByID(p.getPotionID())));

                if (!effects.contains(Potion.moveSpeed) || effects.contains(Potion.jump)) continue;

                potionSlot = i;
            }
        }

        if (potionSlot == -1 || (!mc.thePlayer.onGround && onlyOnGround.getValue())) return;

        this.potionSlot = potionSlot;
        stage = 1;
    }

    private void throwPot(EventPreMotion event, int potionSlot) {

        if (potionSlot == -1 || (!mc.thePlayer.onGround && onlyOnGround.getValue())) return;

        int oldSlot = mc.thePlayer.inventory.currentItem;

        event.setPitch(90);

        PacketUtils.addToSendQueue(new C09PacketHeldItemChange(potionSlot));
        PacketUtils.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getStackInSlot(potionSlot)));
        PacketUtils.addToSendQueue(new C09PacketHeldItemChange(oldSlot));

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
