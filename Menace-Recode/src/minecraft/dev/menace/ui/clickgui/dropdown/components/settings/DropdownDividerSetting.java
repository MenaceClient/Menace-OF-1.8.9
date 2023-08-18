package dev.menace.ui.clickgui.dropdown.components.settings;

import dev.menace.module.settings.*;
import dev.menace.utils.render.ColorUtils;
import dev.menace.utils.render.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;
import java.util.ArrayList;

public class DropdownDividerSetting extends DropdownSetting {

    ArrayList<DropdownSetting> settings = new ArrayList<>();
    int height = 22;
    int expandedHeight = 20;
    boolean expanded = false;

    public DropdownDividerSetting(DividerSetting setting) {
        super(setting);

        for (Setting s : setting.getSettings()) {
            if (s instanceof DividerSetting) {
                DropdownDividerSetting dropdownDividerSetting = new DropdownDividerSetting((DividerSetting) s);
                settings.add(dropdownDividerSetting);
                expandedHeight += dropdownDividerSetting.getHeight();
            } else if (s instanceof ToggleSetting) {
                DropdownToggleSetting dropdownToggleSetting = new DropdownToggleSetting((ToggleSetting) s);
                settings.add(dropdownToggleSetting);
                expandedHeight += dropdownToggleSetting.getHeight();
            } else if (s instanceof SliderSetting) {
                DropdownSliderSetting dropdownSliderSetting = new DropdownSliderSetting((SliderSetting) s);
                settings.add(dropdownSliderSetting);
                expandedHeight += dropdownSliderSetting.getHeight();
            } else if (s instanceof ListSetting) {
                DropdownListSetting dropdownListSetting = new DropdownListSetting((ListSetting) s);
                settings.add(dropdownListSetting);
                expandedHeight += dropdownListSetting.getHeight();
            }
        }
    }

    @Override
    public void render(int x, int y, int mouseX, int mouseY) {

        RenderUtils.drawRect(x, y, x + width, y + 20, ColorUtils.setAlpha(Color.BLACK, 150).getRGB());

        fontRenderer.drawString(setting.getName(), x + 2, y + 6, Color.WHITE.getRGB());

        if (expanded) {
            int yOffset = 20;
            for (DropdownSetting setting : settings) {
                setting.render(x, y + yOffset, mouseX, mouseY);
                yOffset += setting.getHeight();
            }

            RenderUtils.drawRect(x, y + 20, x + width, y + 22, Color.WHITE.getRGB());
            RenderUtils.drawRect(x, y + expandedHeight - 2, x + width, y + expandedHeight, Color.WHITE.getRGB());
        } else {
            RenderUtils.drawRect(x, y + height - 2, x + width, y + height, Color.WHITE.getRGB());
        }

    }

    @Override
    public void mouseClicked(int x, int y, int mouseX, int mouseY, int mouseButton) {

        if (RenderUtils.hover(x, y, mouseX, mouseY, width, 20)) {
            expanded = !expanded;
        }

        if (expanded) {
            int yOffset = 20;
            for (DropdownSetting setting : settings) {
                setting.mouseClicked(x, y + yOffset, mouseX, mouseY, mouseButton);
                yOffset += setting.getHeight();
            }
        }

    }

    @Override
    public int getHeight() {
        return expanded ? expandedHeight : height;
    }
}
