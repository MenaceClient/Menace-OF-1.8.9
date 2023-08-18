package dev.menace.ui.hud.elements;

import dev.menace.Menace;
import dev.menace.ui.hud.BaseElement;
import dev.menace.ui.hud.options.BooleanOption;
import dev.menace.utils.render.RenderUtils;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import java.awt.*;
import java.text.DecimalFormat;

public class TargetHudElement extends BaseElement {

    DecimalFormat decimalFormat = new DecimalFormat("0.0");
    private boolean customFont;

    @Override
    public void setup() {
        this.addOption(new BooleanOption("CustomFont", false) {
            @Override
            public void update() {
                TargetHudElement.this.customFont = this.getValue();
                super.update();
            }
        });
    }

    @Override
    public void render() {

        //Prioratize tpAuraModule
        if (Menace.instance.moduleManager.tpAuraModule.targets.size() > 0) {
            int xOff = 0;
            int yOff = 0;
            for (EntityLivingBase target : Menace.instance.moduleManager.tpAuraModule.targets) {
                if (!(target instanceof EntityPlayer)) continue;

                int q = this.getStringWidth(decimalFormat.format(target.getHealth()), this.customFont) + mc.fontRendererObj.getStringWidth("\u2764");
                int width = Math.max(this.getStringWidth(target.getDisplayName().getUnformattedText(), this.customFont), q);
                RenderUtils.drawRoundedRect((float) (this.getPosX() + xOff), (float) (this.getPosY() + yOff), (float) (this.getPosX() + width + 45 + xOff), (float) (this.getPosY() + 40 + yOff), 5, Color.black.getRGB());
                RenderUtils.drawHead(((AbstractClientPlayer)target).getLocationSkin(), (int) (this.getPosX() + 5 + xOff), (int) (this.getPosY() + 5 + yOff), 30, 30);
                this.drawString(target.getDisplayName().getUnformattedText(), this.getPosX() + 40 + xOff, this.getPosY() + 5 + yOff, -1, this.customFont);
                this.drawString("§r" + decimalFormat.format(target.getHealth()), this.getPosX() + 40 + xOff, this.getPosY() + 20 + yOff, -1, this.customFont);
                int w = this.getStringWidth(decimalFormat.format(target.getHealth()), this.customFont);
                mc.fontRendererObj.drawString("\u2764", this.getPosX() + w + 43 + xOff, this.getPosY() + 20 + yOff, Color.red.getRGB());

                xOff += width + 45;
                if (xOff + width + 45 > 0) {
                    xOff = 0;
                    yOff += 40;
                }
            }
        } else if (Menace.instance.moduleManager.killAuraModule.trget.size() > 0) {
            int xOff = 0;
            int yOff = 0;
            for (EntityLivingBase target : Menace.instance.moduleManager.killAuraModule.trget) {
                if (!(target instanceof EntityPlayer)) continue;

                int q = this.getStringWidth(decimalFormat.format(target.getHealth()), this.customFont) + mc.fontRendererObj.getStringWidth("\u2764");
                int width = Math.max(this.getStringWidth(target.getDisplayName().getUnformattedText(), this.customFont), q);
                RenderUtils.drawRoundedRect((float) (this.getPosX() + xOff), (float) (this.getPosY() + yOff), (float) (this.getPosX() + width + 45 + xOff), (float) (this.getPosY() + 40 + yOff), 5, Color.black.getRGB());
                RenderUtils.drawHead(((AbstractClientPlayer)target).getLocationSkin(), (int) (this.getPosX() + 5 + xOff), (int) (this.getPosY() + 5 + yOff), 30, 30);
                this.drawString(target.getDisplayName().getUnformattedText(), this.getPosX() + 40 + xOff, this.getPosY() + 5 + yOff, -1, this.customFont);
                this.drawString("§r" + decimalFormat.format(target.getHealth()), this.getPosX() + 40 + xOff, this.getPosY() + 20 + yOff, -1, this.customFont);
                int w = this.getStringWidth(decimalFormat.format(target.getHealth()), this.customFont);
                mc.fontRendererObj.drawString("\u2764", this.getPosX() + w + 43 + xOff, this.getPosY() + 20 + yOff, Color.red.getRGB());

                if (xOff > 0) {
                    xOff = 0;
                    yOff += 40;
                }
                xOff += width + 48;
            }
        }
    }

    @Override
    public void renderDummy() {
        int q = this.getStringWidth("20.0", this.customFont) + mc.fontRendererObj.getStringWidth("\u2764");

        //Security Features
        String targetName;
        if (Menace.instance.moduleManager.securityFeaturesModule.isToggled()) {
            targetName = Menace.instance.user.getUsername();
        } else {
            targetName = mc.thePlayer.getDisplayName().getUnformattedText();
        }
        String targetHealth = "20.0";

        int width = Math.max(this.getStringWidth(targetName, this.customFont), q);

        RenderUtils.drawRoundedRect((float) (this.getPosX()), (float) (this.getPosY()), (float) (this.getPosX() + width + 45), (float) (this.getPosY() + 40), 5, Color.black.getRGB());
        RenderUtils.drawHead(mc.thePlayer.getLocationSkin(), (int) (this.getPosX() + 5), (int) (this.getPosY() + 5), 30, 30);
        this.drawString(targetName, this.getPosX() + 40, this.getPosY() + 5, -1, this.customFont);
        this.drawString("§r" + targetHealth, this.getPosX() + 40, this.getPosY() + 20, -1, this.customFont);
        int w = this.getStringWidth(targetHealth, this.customFont);
        mc.fontRendererObj.drawString("\u2764", this.getPosX() + w + 43, this.getPosY() + 20, Color.red.getRGB());
    }

    @Override
    public int getWidth() {
        int q = this.getStringWidth("20.0", this.customFont) + mc.fontRendererObj.getStringWidth("\u2764");

        //Security Features
        String targetName;
        if (Menace.instance.moduleManager.securityFeaturesModule.isToggled()) {
            targetName = Menace.instance.user.getUsername();
        } else {
            targetName = mc.thePlayer.getDisplayName().getUnformattedText();
        }

        int width = Math.max(this.getStringWidth(targetName, this.customFont), q);

        return width + 45;
    }

    @Override
    public int getHeight() {
        return 40;
    }
}
