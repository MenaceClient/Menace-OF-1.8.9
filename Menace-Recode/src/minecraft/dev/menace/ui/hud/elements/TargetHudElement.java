package dev.menace.ui.hud.elements;

import dev.menace.Menace;
import dev.menace.module.modules.combat.KillAuraModule;
import dev.menace.ui.hud.BaseElement;
import dev.menace.utils.render.RenderUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import java.awt.*;

public class TargetHudElement extends BaseElement {

    public TargetHudElement() {
        super(0.5, 0.5, true);
    }

    @Override
    public void render() {
        EntityLivingBase target = Menace.instance.moduleManager.killAuraModule.target;
        if (!(target instanceof EntityPlayer)) return;
        RenderUtils.drawRoundedRect(this.getAbsoluteX(), this.getAbsoluteY(), this.getAbsoluteX() + 80, this.getAbsoluteY() + 50, 5, Color.black.getRGB());
        fr.drawString(target.getName(), this.getAbsoluteX() + 5, this.getAbsoluteY() + 5, -1);
    }

    @Override
    public void renderDummy() {

    }

    @Override
    public int getWidth() {
        return 80;
    }

    @Override
    public int getHeight() {
        return 50;
    }
}
