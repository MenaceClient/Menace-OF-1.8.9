package dev.menace.ui.clickgui.menace.components.settings;

import dev.menace.module.settings.ListSetting;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.render.RenderUtils;

import java.awt.*;

public class ListComponent extends SettingComponent {
    public ListComponent(ListSetting s) {
        super(s);
    }

    @Override
    public void draw(int mouseX, int mouseY, int x, int y) {
        RenderUtils.drawRoundedRect(x + 205, y + 5, x + 205 + 100, y + 5 + 20 + (((ListSetting)setting).getOptions().length * 25), 5, new Color(32, 31, 35).getRGB());
        fontRenderer.drawCenteredString(setting.getName(), x + 255, y + 12, Color.WHITE.getRGB());
        String selectedOption = ((ListSetting)setting).getValue();
        int offset = 15;
        for (String value : ((ListSetting)setting).getOptions()) {
            if (value.equalsIgnoreCase(selectedOption)) {
                RenderUtils.drawRoundedRect(x + 205 + 5, y + 6 + offset + 5, x + 205 + 5 + 10, y + 6 + offset + 15, 5, Color.GREEN.getRGB());
            } else {
                RenderUtils.drawRoundedRect(x + 205 + 5, y + 6 + offset + 5, x + 205 + 5 + 10, y + 6 + offset + 15, 5, Color.GRAY.getRGB());
            }
            fontRenderer.drawString(value, x + 205 + 20, y + 12 + offset, Color.WHITE.getRGB());
            offset += 25;
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int x, int y, int mouseButton) {
        int offset = 15;
        for (String value : ((ListSetting)setting).getOptions()) {
            if (RenderUtils.hover2(x + 205 + 5, y + 6 + offset + 5 + 7, x + 205 + 5 + 10, y + 6 + offset + 22, mouseX, mouseY)) {
                ((ListSetting)setting).setValue(value);
            }
            offset += 25;
        }
    }

    @Override
    public int getHeight() {
        return 25 + (((ListSetting) setting).getOptions().length * 25);
    }
}
