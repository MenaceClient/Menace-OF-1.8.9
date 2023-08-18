package dev.menace.ui.clickgui.menace.components;

import dev.menace.Menace;
import dev.menace.module.BaseModule;
import dev.menace.module.settings.*;
import dev.menace.ui.clickgui.menace.MenaceClickGui;
import dev.menace.ui.clickgui.menace.components.settings.*;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.render.RenderUtils;
import dev.menace.utils.render.font.MenaceFontRenderer;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.ArrayList;

public class SettingsMenuComponent {

    int x, y, width, height;
    private final MenaceFontRenderer fontRenderer;
    private int scroll;
    private final BaseModule component;
    private ArrayList<SettingComponent> settingComponents = new ArrayList<>();

    public SettingsMenuComponent(MenaceClickGui parent, BaseModule component) {
        this.x = parent.x;
        this.y = parent.y;
        this.width = parent.width;
        this.height = parent.height;
        this.component = component;
        fontRenderer = Menace.instance.sfPro;

        for (Setting s : component.getSettings()) {
            if (s instanceof ToggleSetting) {
                settingComponents.add(new ToggleComponent((ToggleSetting) s));
            } else if (s instanceof SliderSetting) {
                settingComponents.add(new SliderComponent((SliderSetting) s));
            } else if (s instanceof ListSetting) {
                settingComponents.add(new ListComponent( (ListSetting) s));
            } else if (s instanceof DividerSetting) {
                settingComponents.add(new DividerComponent((DividerSetting) s));
            }
        }

    }

    public void draw(int mouseX, int mouseY) {

        RenderUtils.drawRoundedRect(this.x + 200, this.y + 5, this.width - 200, this.height - 5, 5, new Color(39, 38, 42).getRGB());

        //Module name
        fontRenderer.drawCenteredString(component.getName(), (float) (this.x + 200 + (this.width - 200)) / 2, this.y + 12, Color.WHITE.getRGB());

        //Module description
        fontRenderer.drawCenteredString(component.getDesc(), (float) (this.x + 200 + (this.width - 200)) / 2, this.y + 25, Color.WHITE.getRGB());

        //Module settings
        int yOffset = 27;

        int currScroll = 0;
        for (SettingComponent settingComponent : this.settingComponents) {
            settingComponent.setting.constantCheck();
            if (!settingComponent.setting.isVisible()) continue;

            if (currScroll < scroll) {
                currScroll++;
                continue;
            }

            if (this.y + 5 + yOffset + settingComponent.getHeight() > this.height - 20) {
                break;
            }

            settingComponent.draw(mouseX, mouseY, this.x, this.y + yOffset);
            yOffset += settingComponent.getHeight();
        }

        //Handle scroll
        if (Mouse.hasWheel()) {
            int wheel = Mouse.getDWheel();
            if (wheel < 0 && scroll < component.getSettings().size() - 10) {
                scroll += 1;
            } else if (wheel > 0 && scroll > 0) {
                scroll -= 1;
            }
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0) {
            int yOffset = 20;
            int currScroll = 0;
            for (SettingComponent settingComponent : this.settingComponents) {
                if (!settingComponent.setting.isVisible()) continue;

                if (currScroll < scroll) {
                    currScroll++;
                    continue;
                }

                if (this.y + 5 + yOffset + settingComponent.getHeight() > this.height - 20) {
                    break;
                }

                settingComponent.mouseClicked(mouseX, mouseY, this.x, this.y + yOffset, mouseButton);
                yOffset += settingComponent.getHeight();
            }
        }
    }
}
