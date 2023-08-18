package dev.menace.ui.hud;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventRender2D;
import dev.menace.ui.hud.options.BaseOption;
import dev.menace.ui.hud.options.ColorSelectOption;
import dev.menace.utils.render.font.MenaceFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.util.ArrayList;

public abstract class BaseElement {

    private ArrayList<BaseOption> options = new ArrayList<>();

    protected double posX;
    protected double posY;

    MenaceFontRenderer fr;
    protected Minecraft mc;

    public BaseElement() {
        fr = Menace.instance.sfPro;
        mc = Minecraft.getMinecraft();

        this.setPos(50, 50);

        this.setup();
    }

    public abstract void setup();

    public abstract void render();

    public abstract void renderDummy();

    public void setPos(double posX, double posY) {
        ScaledResolution sr = new ScaledResolution(mc);

        this.posX = posX / sr.getScaledWidth();
        this.posY = posY / sr.getScaledHeight();
    }

    public void setPosX(double posX) {
        ScaledResolution sr = new ScaledResolution(mc);

        this.posX = posX / sr.getScaledWidth();
    }

    public void setPosY(double posY) {
        ScaledResolution sr = new ScaledResolution(mc);

        this.posY = posY / sr.getScaledHeight();
    }

    public double getPosX() {
        ScaledResolution sr = new ScaledResolution(mc);

        return posX * sr.getScaledWidth();
    }

    public double getPosY() {
        ScaledResolution sr = new ScaledResolution(mc);

        return posY * sr.getScaledHeight();
    }

    public ArrayList<BaseOption> getOptions() {
        return options;
    }

    public void addOption(BaseOption option) {
        options.add(option);
    }

    public abstract int getWidth();

    public abstract int getHeight();

    public void drawString(String string, double x, double y, int color, boolean customFont) {
        if (customFont) {
            fr.drawString(string, x, y, color);
        } else {
            mc.fontRendererObj.drawString(string, x, y, color);
        }
    }

    public void drawCenteredString(String string, float x, float y, int color, boolean customFont) {
        if (customFont) {
            fr.drawCenteredString(string, x, y, color);
        } else {
            mc.fontRendererObj.drawCenteredString(string, x, y, color);
        }
    }

    protected String wrapString(String string, int width, boolean customFont) {
        if (getStringWidth(string, customFont) <= width) {
            return string;
        } else {
            return wrapString(string.substring(0, string.length() - 1), width, customFont);
        }
    }

    protected String reverseWrapString(String string, int width, boolean customFont) {
        if (getStringWidth(string, customFont) <= width) {
            return string;
        } else {
            return reverseWrapString(string.substring(1), width, customFont);
        }
    }

    protected int getStringWidth(String string, boolean customFont) {
        if (customFont) {
            return fr.getStringWidth(string);
        } else {
            return mc.fontRendererObj.getStringWidth(string);
        }
    }

    protected int getFontHeight(boolean customFont) {
        if (customFont) {
            return fr.getHeight();
        } else {
            return mc.fontRendererObj.FONT_HEIGHT;
        }
    }

}
