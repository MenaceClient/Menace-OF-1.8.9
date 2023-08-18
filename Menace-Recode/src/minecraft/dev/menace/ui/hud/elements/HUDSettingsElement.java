package dev.menace.ui.hud.elements;

import dev.menace.Menace;
import dev.menace.event.events.EventRender2D;
import dev.menace.ui.hud.BaseElement;
import dev.menace.ui.hud.options.BaseOption;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.render.RenderUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Optional;

public class HUDSettingsElement extends BaseElement {

    private Optional<BaseElement> selectedElement = Optional.empty();

    @Override
    public void setup() {
    }

    @Override
    public void render() {
        //ignore
    }

    @Override
    public void renderDummy() {
        RenderUtils.drawRect(this.getPosX(), this.getPosY(), this.getPosX() + getWidth(), this.getPosY() + getHeight(), Color.BLACK.getRGB());

        if (selectedElement.isPresent() && !(selectedElement.get() instanceof HUDSettingsElement)) {
            //Element Settings
            this.drawString(selectedElement.get().getClass().getSimpleName().split("Element")[0], this.getPosX() + 2, this.getPosY() + 2, -1, true);

            int yOff = this.getFontHeight(true) + 4;
            for (BaseOption option : selectedElement.get().getOptions()) {
                option.render(this.getPosX() + 2, this.getPosY() + yOff);

                yOff += option.getHeight() + 3;
            }
        } else {
            //Element Creator
            this.drawString("Create Element", this.getPosX() + 2, this.getPosY() + 2, -1, true);

            int yOff = this.getFontHeight(true) + 4;
            for (Class<? extends BaseElement> element : Menace.instance.hudManager.getElementList()) {
                this.drawString(element.getSimpleName().split("Element")[0], this.getPosX() + 2, this.getPosY() + yOff, -1, true);

                yOff += this.getFontHeight(true) + 2;
            }
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (selectedElement.isPresent() && !(selectedElement.get() instanceof HUDSettingsElement)) {
            int yOff = this.getFontHeight(true) + 4;
            for (BaseOption option : selectedElement.get().getOptions()) {
                option.mouseClicked(this.getPosX(), this.getPosY() + yOff, mouseX, mouseY, mouseButton);

                yOff += option.getHeight() + 3;
            }
        } else {
            //Element Creator

            int yOff = this.getFontHeight(true) + 4;
            for (Class<? extends BaseElement> element : Menace.instance.hudManager.getElementList()) {
                if (RenderUtils.hover((int) (this.getPosX() + 2), (int) (this.getPosY() + yOff), mouseX, mouseY, getWidth(), 10)) {
                    try {
                        BaseElement elementInstance = element.newInstance();
                        ChatUtils.message("Added " + element.getSimpleName() + " to the HUD");
                        Menace.instance.hudManager.registerElement(elementInstance);
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }

                yOff += this.getFontHeight(true) + 2;
            }
        }
    }

    public void setSelectedElement(Optional<BaseElement> element) {
        this.selectedElement = element;
    }

    @Override
    public int getWidth() {
        return 80;
    }

    @Override
    public int getHeight() {
        return 200;
    }
}
