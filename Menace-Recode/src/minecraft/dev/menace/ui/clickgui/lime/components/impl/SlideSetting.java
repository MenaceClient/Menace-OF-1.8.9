package dev.menace.ui.clickgui.lime.components.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.lwjgl.input.Mouse;

import dev.menace.module.settings.Setting;
import dev.menace.module.settings.SliderSetting;
import dev.menace.ui.clickgui.lime.Priority;
import dev.menace.ui.clickgui.lime.components.Component;
import dev.menace.ui.clickgui.lime.components.FrameModule;
import dev.menace.ui.clickgui.lime.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.MathHelper;

public class SlideSetting extends Component implements Priority {
    public SlideSetting(int x, int y, FrameModule owner, Setting setting) {
        super(x, y, owner, setting);
    }

    private boolean drag;

    @Override
    public void initGui() {
        drag = false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {

        if(!Mouse.isButtonDown(0)) drag = false;

        SliderSetting slide = (SliderSetting) getSetting();
        double min = slide.getMin();
        double max = slide.getMax();
        double diff = Math.min(defaultWidth + 5, Math.max(0, mouseX - (this.x)));
        double renderWidth = defaultWidth * (slide.getValue() - min) / (max - min);
        Gui.drawRect(x, y, x + (int) renderWidth, y + getOffset(), darkerMainColor);

        if(drag)
        {
            if(diff == 0)
                slide.setValue(min);
            else
            {
                double newValue = roundToPlace((diff / defaultWidth) * (max - min) + min, 2);
                if(newValue <= max)
                    this.setValue(newValue);
            }
        }

        FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
        fr.drawString(getSetting().getName() + ": " + roundToPlace(((SliderSetting) getSetting()).getValue(), 2), x + 5, y + (getOffset() / 2F - (fr.FONT_HEIGHT / 2F)), stringColor, true);
    }

    private double roundToPlace(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    private double snapToStep(double value, double valueStep) {
        if (valueStep > 0.0F)
            value = valueStep * (double) Math.round(value / valueStep);

        return value;
    }

    private void setValue(double value) {
        final SliderSetting set = (SliderSetting) getSetting();
        set.setValue(MathHelper.clamp_double(snapToStep(value, set.getIncrement()), set.getMin(), set.getMax()));
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        return drag = RenderUtils.hover(x, y, mouseX, mouseY, defaultWidth, getOffset()) && mouseButton == 0;
    }

    @Override
    public void onGuiClosed(int mouseX, int mouseY, int mouseButton) {

    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {

    }

    @Override
    public int getOffset() {
        return 15;
    }
}
