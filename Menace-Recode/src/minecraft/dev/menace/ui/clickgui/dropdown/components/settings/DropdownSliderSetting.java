package dev.menace.ui.clickgui.dropdown.components.settings;

import dev.menace.module.settings.Setting;
import dev.menace.module.settings.SliderSetting;
import dev.menace.utils.misc.MathUtils;
import dev.menace.utils.render.ColorUtils;
import dev.menace.utils.render.RenderUtils;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.text.DecimalFormat;

public class DropdownSliderSetting extends DropdownSetting {

    boolean dragging = false;

    public DropdownSliderSetting(SliderSetting setting) {
        super(setting);
    }

    @Override
    public void render(int x, int y, int mouseX, int mouseY) {
        if(!Mouse.isButtonDown(0)) dragging = false;

        RenderUtils.drawRect(x, y, x + width, y + 30, ColorUtils.setAlpha(Color.BLACK, 150).getRGB());

        fontRenderer.drawString(setting.getName() + ": " + (new DecimalFormat("#.##").format(((SliderSetting)setting).getValue())), x + 2, y + 6, Color.WHITE.getRGB());

        RenderUtils.drawRect(x + 2, y + 20, x + width - 2, y + 22, Color.GRAY.getRGB());

        float sliderPercent = (float) ((((SliderSetting)setting).getValue() - ((SliderSetting)setting).getMin()) / (((SliderSetting)setting).getMax() - ((SliderSetting)setting).getMin()));
        RenderUtils.drawRect(x + 2, y + 20, x + (width * sliderPercent), y + 22, Color.WHITE.getRGB());
        RenderUtils.drawRect(x + (width * sliderPercent), y + 18, x + (width * sliderPercent) + 2, y + 24, Color.WHITE.getRGB());

        double min = ((SliderSetting)setting).getMin();
        double max = ((SliderSetting)setting).getMax();
        double diff = Math.min(width - 4, Math.max(0, mouseX - (x + 2)));

        if (dragging) {
            if (diff == 0)
                ((SliderSetting)setting).setValue(((SliderSetting)setting).getMin());
            else {
                double newValue = MathUtils.roundToPlace((diff / (width - 4)) * (max - min) + min, 2);
                if (newValue <= ((SliderSetting)setting).getMax())
                    this.setValue(newValue);
            }
        }

    }

    @Override
    public void mouseClicked(int x, int y, int mouseX, int mouseY, int mouseButton) {

        if (RenderUtils.hover(x, y + 20, mouseX, mouseY, width, 10) && mouseButton == 0) {
            dragging = true;
        }

    }

    private void setValue(double value) {
        final SliderSetting set = (SliderSetting) setting;
        set.setValue(MathHelper.clamp_double(MathUtils.snapToStep(value, set.getIncrement()), set.getMin(), set.getMax()));
    }


    @Override
    public int getHeight() {
        return 30;
    }
}
