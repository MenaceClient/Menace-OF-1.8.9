package dev.menace.ui.hud.elements;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventKey;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.modules.render.HUDModule;
import dev.menace.ui.hud.BaseElement;
import dev.menace.utils.render.ColorUtils;
import dev.menace.utils.render.RenderUtils;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class TabGuiElement extends BaseElement {

    public int currentTab = 0;
    public int moduleIndex = 0;
    public boolean expanded = false;

    public TabGuiElement() {
        super(0.5, 0.5, true);
    }

    public void start() {
        Menace.instance.eventManager.register(this);
    }

    public void stop() {
        Menace.instance.eventManager.unregister(this);
    }

    @Override
    public void render() {

        HUDModule hudModule = Menace.instance.moduleManager.hudModule;
        int color = hudModule.color.getValue().equalsIgnoreCase("Custom") ? new Color(hudModule.red.getValueI(), hudModule.green.getValueI(), hudModule.blue.getValueI(), hudModule.alpha.getValueI()).getRGB() : ColorUtils.fade(hudModule.rainbowSpeed.getValueF(), -this.getAbsoluteY()).getRGB();
        RenderUtils.drawRect(this.getAbsoluteX(), this.getAbsoluteY(), this.getAbsoluteX() + 75, this.getAbsoluteY() + (Category.values().length * (this.getFontHeight() + 4)) + 4, new Color(0, 0, 0, 120).getRGB());
        RenderUtils.drawRect(this.getAbsoluteX() + 2, this.getAbsoluteY() + (currentTab * (this.getFontHeight() + 4)) + 2, this.getAbsoluteX() + 73, this.getAbsoluteY() + ((currentTab + 1) * (this.getFontHeight() + 4)) + 2, color);

        int count = 0;
        for (Category c : Category.values()) {
            this.drawString(c.name(), this.getAbsoluteX() + 5, this.getAbsoluteY() + 5 + count, -1);

            count += this.getFontHeight() + 4;
        }

        if (expanded) {
            int modules = Menace.instance.moduleManager.getModulesByCategory(Category.values()[currentTab]).size();
            //int size = (int) (this.getFontHeight() * modules * 1.5 + 3);

            if (moduleIndex > modules - 1) {
                moduleIndex = modules - 1;
            }

            RenderUtils.drawRect(this.getAbsoluteX() + 75, this.getAbsoluteY(), this.getAbsoluteX() + 150, this.getAbsoluteY() + (modules * (this.getFontHeight() + 4)) + 4, new Color(0, 0, 0, 120).getRGB());
            RenderUtils.drawRect(this.getAbsoluteX() + 77, this.getAbsoluteY() + (moduleIndex * (this.getFontHeight() + 4)) + 2, this.getAbsoluteX() + 148, this.getAbsoluteY() + ((moduleIndex + 1) * (this.getFontHeight() + 4)) + 2, color);

            int count2 = 0;
            for (BaseModule m : Menace.instance.moduleManager.getModulesByCategory(Category.values()[currentTab])) {
                this.drawString(m.getName(), this.getAbsoluteX() + 80, this.getAbsoluteY() + 5 + count2, -1);

                count2 += this.getFontHeight() + 4;
            }
        }

    }

    @EventTarget
    public void onKey(EventKey event) {

        HUDModule hudModule = Menace.instance.moduleManager.hudModule;
        if (!hudModule.tabGui.getValue()) return;

        if (event.getKey() == Keyboard.KEY_UP) {
            if (expanded) {
                if (moduleIndex > 0) {
                    moduleIndex--;
                } else {
                    moduleIndex =  Menace.instance.moduleManager.getModulesByCategory(Category.values()[currentTab]).size() - 1;
                }
            } else {
                if (currentTab > 0) {
                    currentTab--;
                } else {
                    currentTab = Category.values().length - 1;
                }
            }
        } else if (event.getKey() == Keyboard.KEY_DOWN) {
            if (expanded) {
                if (moduleIndex < Menace.instance.moduleManager.getModulesByCategory(Category.values()[currentTab]).size() - 1) {
                    moduleIndex++;
                } else {
                    moduleIndex = 0;
                }
            } else {
                if (currentTab < Category.values().length - 1) {
                    currentTab++;
                } else {
                    currentTab = 0;
                }
            }
        } else if (event.getKey() == Keyboard.KEY_RIGHT) {
            if (expanded) {
                Menace.instance.moduleManager.getModulesByCategory(Category.values()[currentTab]).get(moduleIndex).toggle();
            } else {
                expanded = true;
            }
        } else if (event.getKey() == Keyboard.KEY_LEFT) {
            expanded = false;
        }

    }

    @Override
    public void renderDummy() {
        int count = 0;
        for (Category c : Category.values()) {
            this.drawString(c.name(), this.getAbsoluteX() + 5, this.getAbsoluteY() + 5 + count, -1);

            count += this.getFontHeight() + 4;
        }
    }

    @Override
    public int getWidth() {
        return 75;
    }

    @Override
    public int getHeight() {
        return 102;
    }
}
