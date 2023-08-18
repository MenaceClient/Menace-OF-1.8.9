package dev.menace.ui.hud.elements;

import dev.menace.ui.hud.BaseElement;
import dev.menace.ui.hud.options.BooleanOption;
import dev.menace.ui.hud.options.ColorSelectOption;
import dev.menace.ui.hud.options.ListOption;

import java.awt.*;

public class PositionElement extends BaseElement {

    private boolean customFont;
    private Color color;
    private String mode;

    @Override
    public void setup() {
        this.addOption(new BooleanOption("Custom Font",false) {
            @Override
            public void update() {
                PositionElement.this.customFont = this.getValue();
                super.update();
            }
        });
        this.addOption(new ColorSelectOption("Color", Color.RED) {
            @Override
            public void update() {
                PositionElement.this.color = this.getColor();
                super.update();
            }
        });
        this.addOption(new ListOption("Mode", new String[] {"Single", "Multi"}, "Single") {
            @Override
            public void update() {
                PositionElement.this.mode = this.getSelected();
                super.update();
            }
        });
    }

    @Override
    public void render() {
        if (mode.equalsIgnoreCase("Single")) {
            this.drawString(String.format("X: %.1f", mc.thePlayer.posX) + " |" + String.format(" Y: %.1f", mc.thePlayer.boundingBox.minY) + " |" + String.format(" Z: %.1f", mc.thePlayer.posZ), this.getPosX(), this.getPosY(), this.color.getRGB(), this.customFont);
        } else {
            this.drawString(String.format("X: %.1f", mc.thePlayer.posX), this.getPosX(), this.getPosY(), this.color.getRGB(), this.customFont);
            this.drawString(String.format("Y: %.1f", mc.thePlayer.boundingBox.minY), this.getPosX(), this.getPosY() + this.getFontHeight(this.customFont) + 2, this.color.getRGB(), this.customFont);
            this.drawString(String.format("Z: %.1f", mc.thePlayer.posZ), this.getPosX(), this.getPosY() + (this.getFontHeight(this.customFont) * 2) + 4, this.color.getRGB(), this.customFont);
        }
    }

    @Override
    public void renderDummy() {
        if (mode.equalsIgnoreCase("Single")) {
            this.drawString("X: 20.5 | Y: 64.0 | Z: -20.5", this.getPosX(), this.getPosY(), this.color.getRGB(), this.customFont);
        } else {
            this.drawString("X: 20.5", this.getPosX(), this.getPosY(), this.color.getRGB(), this.customFont);
            this.drawString("Y: 64.0", this.getPosX(), this.getPosY() + this.getFontHeight(this.customFont) + 2, this.color.getRGB(), this.customFont);
            this.drawString("Z: -20.5", this.getPosX(), this.getPosY() + (this.getFontHeight(this.customFont) * 2) + 4, this.color.getRGB(), this.customFont);
        }
    }

    @Override
    public int getWidth() {
        if (mode.equalsIgnoreCase("Single")) {
            return this.getStringWidth("X: 20.5 | Y: 64.0 | Z: -20.5", this.customFont);
        } else {
            return this.getStringWidth("X: 20.5", this.customFont);
        }
    }

    @Override
    public int getHeight() {
        if (mode.equalsIgnoreCase("Single")) {
            return this.getFontHeight(this.customFont);
        } else {
            return (this.getFontHeight(this.customFont) * 3) + 4;
        }
    }
}
