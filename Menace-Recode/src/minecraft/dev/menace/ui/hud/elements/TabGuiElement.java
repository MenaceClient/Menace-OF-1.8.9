package dev.menace.ui.hud.elements;

import dev.menace.Menace;
import dev.menace.event.events.EventKey;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.ui.hud.BaseElement;
import dev.menace.ui.hud.options.BooleanOption;
import dev.menace.ui.hud.options.ColorSelectOption;
import dev.menace.utils.render.RenderUtils;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class TabGuiElement extends BaseElement {

    private boolean customFont;
    private Color color;

    public int currentTab = 0;
    public int moduleIndex = 0;
    public boolean expanded = false;

    @Override
    public void setup() {
        this.addOption(new BooleanOption("Custom Font", false) {
            @Override
            public void update() {
                TabGuiElement.this.customFont = this.getValue();
                super.update();
            }
        });
        this.addOption(new ColorSelectOption("Color", Color.red) {
            @Override
            public void update() {
                TabGuiElement.this.color = this.getColor();
                super.update();
            }
        });
    }

    @Override
    public void render() {
        RenderUtils.drawRect(this.getPosX(), this.getPosY(), this.getPosX() + 75, this.getPosY() + (Category.values().length * (this.getFontHeight(this.customFont) + 4)) + 4, new Color(0, 0, 0, 120).getRGB());
        RenderUtils.drawRect(this.getPosX() + 2, this.getPosY() + (currentTab * (this.getFontHeight(this.customFont) + 4)) + 2, this.getPosX() + 73, this.getPosY() + ((currentTab + 1) * (this.getFontHeight(this.customFont) + 4)) + 2, color.getRGB());

        int count = 0;
        for (Category c : Category.values()) {
            this.drawString(c.name(), this.getPosX() + 5, this.getPosY() + 5 + count, -1, this.customFont);

            count += this.getFontHeight(this.customFont) + 4;
        }

        if (expanded) {
            int modules = Menace.instance.moduleManager.getModulesByCategory(Category.values()[currentTab]).size();

            if (moduleIndex > modules - 1) {
                moduleIndex = modules - 1;
            }

            RenderUtils.drawRect(this.getPosX() + 75, this.getPosY(), this.getPosX() + 150, this.getPosY() + (modules * (this.getFontHeight(this.customFont) + 4)) + 4, new Color(0, 0, 0, 120).getRGB());
            RenderUtils.drawRect(this.getPosX() + 77, this.getPosY() + (moduleIndex * (this.getFontHeight(this.customFont) + 4)) + 2, this.getPosX() + 148, this.getPosY() + ((moduleIndex + 1) * (this.getFontHeight(this.customFont) + 4)) + 2, color.getRGB());

            int count2 = 0;
            for (BaseModule m : Menace.instance.moduleManager.getModulesByCategory(Category.values()[currentTab])) {
                this.drawString(m.getName(), this.getPosX() + 80, this.getPosY() + 5 + count2, -1, this.customFont);

                count2 += this.getFontHeight(this.customFont) + 4;
            }
        }

    }

    @Override
    public void renderDummy() {
        int count = 0;
        for (Category c : Category.values()) {
            this.drawString(c.name(), this.getPosX() + 5, this.getPosY() + 5 + count, -1, this.customFont);

            count += this.getFontHeight(customFont) + 4;
        }
    }

    public void onKey(EventKey event) {
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
    public int getWidth() {
        return 75;
    }

    @Override
    public int getHeight() {
        return 102;
    }
}
