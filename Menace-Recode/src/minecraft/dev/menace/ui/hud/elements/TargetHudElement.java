package dev.menace.ui.hud.elements;

import dev.menace.Menace;
import dev.menace.ui.hud.BaseElement;
import dev.menace.utils.render.RenderUtils;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import java.awt.*;
import java.text.DecimalFormat;

public class TargetHudElement extends BaseElement {

    DecimalFormat decimalFormat = new DecimalFormat("0.0");

    public TargetHudElement() {
        super(0.5, 0.5, true);
    }

    @Override
    public void render() {
        EntityLivingBase target = Menace.instance.moduleManager.killAuraModule.target;
        if (!(target instanceof EntityPlayer)) return;
        int q = this.getStringWidth(decimalFormat.format(target.getHealth())) + mc.fontRendererObj.getStringWidth("\u2764");
        int width = Math.max(this.getStringWidth(target.getDisplayName().getUnformattedText()), q);
        RenderUtils.drawRoundedRect(this.getAbsoluteX(), this.getAbsoluteY(), this.getAbsoluteX() + width + 45, this.getAbsoluteY() + 40, 5, Color.black.getRGB());
        RenderUtils.drawHead(((AbstractClientPlayer)target).getLocationSkin(), this.getAbsoluteX() + 5, this.getAbsoluteY() + 5, 30, 30);
        this.drawString(target.getDisplayName().getUnformattedText(), this.getAbsoluteX() + 40, this.getAbsoluteY() + 5, -1);
        this.drawString(decimalFormat.format(target.getHealth()), this.getAbsoluteX() + 40, this.getAbsoluteY() + 20, -1);
        int w = this.getStringWidth(decimalFormat.format(target.getHealth()));
        mc.fontRendererObj.drawString("\u2764", this.getAbsoluteX() + w + 43, this.getAbsoluteY() + 19, Color.red.getRGB());
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
