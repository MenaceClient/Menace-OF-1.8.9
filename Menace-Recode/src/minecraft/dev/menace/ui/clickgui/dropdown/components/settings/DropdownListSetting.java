package dev.menace.ui.clickgui.dropdown.components.settings;

import dev.menace.module.settings.ListSetting;
import dev.menace.module.settings.Setting;
import dev.menace.utils.render.ColorUtils;
import dev.menace.utils.render.RenderUtils;

import java.awt.*;

public class DropdownListSetting extends DropdownSetting {

    int height = 20;

    public DropdownListSetting(ListSetting setting) {
        super(setting);

        height = 20 + (setting.getOptions().length * 20);
    }

    @Override
    public void render(int x, int y, int mouseX, int mouseY) {
        RenderUtils.drawRect(x, y, x + width, y + height, ColorUtils.setAlpha(Color.BLACK, 150).getRGB());

        fontRenderer.drawString(setting.getName(), x + 2, y + 6, -1);

        y += 20;
        for (String option : ((ListSetting) setting).getOptions()) {
            RenderUtils.drawRect(x, y, x + 2, y + 20, Color.WHITE.getRGB());
            if (option.equals(((ListSetting) setting).getValue())) {
                fontRenderer.drawString("§a" + option + "§r", x + 3, y + 6, -1);
            } else {
                fontRenderer.drawString(option, x + 3, y + 6, -1);
            }
            y += 20;
        }

    }

    @Override
    public void mouseClicked(int x, int y, int mouseX, int mouseY, int mouseButton) {
        if (RenderUtils.hover(x, y, mouseX, mouseY, width, height)) {
            int offsetY = y + 20;
            for (String option : ((ListSetting) setting).getOptions()) {
                if (RenderUtils.hover(x, offsetY, mouseX, mouseY, width, 20)) {
                    ((ListSetting) setting).setValue(option);
                }
                offsetY += 20;
            }
        }
    }

    @Override
    public int getHeight() {
        return height;
    }
}
