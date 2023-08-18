package dev.menace.ui.clickgui.dropdown.components.settings;

import dev.menace.module.settings.Setting;
import dev.menace.module.settings.ToggleSetting;
import dev.menace.utils.render.ColorUtils;
import dev.menace.utils.render.RenderUtils;

import java.awt.*;

public class DropdownToggleSetting extends DropdownSetting {

    public DropdownToggleSetting(ToggleSetting setting) {
        super(setting);
    }

    @Override
    public void render(int x, int y, int mouseX, int mouseY) {
        RenderUtils.drawRect(x, y, x + width, y + 20, ColorUtils.setAlpha(Color.BLACK, 150).getRGB());

        Color color = ((ToggleSetting)setting).getValue() ? Color.GREEN : Color.RED;
        RenderUtils.drawFilledCircle(x + 10, y + 10, 5, color);

        fontRenderer.drawString(setting.getName(), x + 20, y + 6, Color.WHITE.getRGB());

    }

    @Override
    public void mouseClicked(int x, int y, int mouseX, int mouseY, int mouseButton) {
        if (RenderUtils.hover(x, y, mouseX, mouseY, width, 20) && mouseButton == 0) {
            ((ToggleSetting)setting).setValue(!((ToggleSetting)setting).getValue());
        }
    }

    @Override
    public int getHeight() {
        return 20;
    }
}
