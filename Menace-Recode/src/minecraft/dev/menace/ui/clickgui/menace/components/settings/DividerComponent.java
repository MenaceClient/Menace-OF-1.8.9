package dev.menace.ui.clickgui.menace.components.settings;

import dev.menace.module.settings.*;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.render.RenderUtils;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.ArrayList;

public class DividerComponent extends SettingComponent {

    private int scroll;
    private ArrayList<SettingComponent> settingComponents = new ArrayList<>();

    public DividerComponent(DividerSetting s) {
        super(s);
        for (Setting ss : s.getSettings()) {
            if (ss instanceof ToggleSetting) {
                settingComponents.add(new ToggleComponent((ToggleSetting) ss));
            } else if (ss instanceof SliderSetting) {
                settingComponents.add(new SliderComponent((SliderSetting) ss));
            } else if (ss instanceof ListSetting) {
                settingComponents.add(new ListComponent( (ListSetting) ss));
            } else if (ss instanceof DividerSetting) {
                settingComponents.add(new DividerComponent((DividerSetting) ss));
            }
        }
    }

    @Override
    public void draw(int mouseX, int mouseY, int x, int y) {
        DividerSetting divider = (DividerSetting) setting;
        if (!divider.isOpen()) {
            RenderUtils.drawRoundedRect(x + 205, y + 5, x + 205 + 100, y + 5 + 20, 5, new Color(32, 31, 35).getRGB());
            fontRenderer.drawCenteredString(setting.getName(), x + 255, y + 15, Color.WHITE.getRGB());
        } else {
            int h = 0;
            for (SettingComponent settingComponent : this.settingComponents) {
                h += settingComponent.getHeight();
            }
            RenderUtils.drawRoundedRect(x + 205, y + 5, x + 205 + 100, y + 5 + h, 5, new Color(32, 31, 35).getRGB());
            fontRenderer.drawCenteredString(setting.getName(), x + 255, y + 15, Color.WHITE.getRGB());
            int yOffset = 20;

            int currScroll = 0;
            for (SettingComponent settingComponent : this.settingComponents) {
                if (!settingComponent.setting.isVisible()) continue;

                if (currScroll < scroll) {
                    currScroll++;
                    continue;
                }

                if (y + 5 + yOffset + settingComponent.getHeight() > GuiScreen.height - 100 - 40) {
                    break;
                }

                settingComponent.draw(mouseX, mouseY, x, y + yOffset);
                yOffset += settingComponent.getHeight();
            }

            //Handle scroll
            if (Mouse.hasWheel()) {
                int wheel = Mouse.getDWheel();
                if (wheel < 0 && scroll < divider.getSettings().length - 10) {
                    scroll += 1;
                } else if (wheel > 0 && scroll > 0) {
                    scroll -= 1;
                }
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int x, int y, int mouseButton) {
        DividerSetting divider = (DividerSetting) setting;
        if (RenderUtils.hover2(x + 205, y + 5, x + 205 + 100, y + 5 + 20, mouseX, mouseY)) {
            divider.setOpen(!divider.isOpen());
        } else {
            int yOffset = 20;

            int currScroll = 0;
            for (SettingComponent settingComponent : this.settingComponents) {
                if (!settingComponent.setting.isVisible()) continue;

                if (currScroll < scroll) {
                    currScroll++;
                    continue;
                }

                if (y + 5 + yOffset + settingComponent.getHeight() > GuiScreen.height - 100 - 20) {
                    break;
                }

                settingComponent.mouseClicked(mouseX, mouseY, x, y + yOffset, mouseButton);
                yOffset += settingComponent.getHeight();
            }
        }

    }

    @Override
    public int getHeight() {
        DividerSetting divider = (DividerSetting) setting;
        if (!divider.isOpen()) {
            return 25;
        } else {
            int height = 25;
            for (SettingComponent settingComponent : this.settingComponents) {
                height += settingComponent.getHeight();
            }
            return height;
        }
    }
}
