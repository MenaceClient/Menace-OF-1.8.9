package dev.menace.ui.hud.elements;

import dev.menace.ui.hud.BaseElement;
import dev.menace.ui.hud.options.BooleanOption;
import dev.menace.ui.hud.options.ColorSelectOption;
import net.minecraft.client.network.NetworkPlayerInfo;

import java.awt.*;

public class PingElement extends BaseElement {

    private boolean customFont;
    private Color color;

    @Override
    public void setup() {
        this.addOption(new BooleanOption("Custom Font", false) {
            @Override
            public void update() {
                PingElement.this.customFont = this.getValue();
                super.update();
            }
        });
        this.addOption(new ColorSelectOption("Color", Color.RED) {
            @Override
            public void update() {
                PingElement.this.color = this.getColor();
                super.update();
            }
        });
    }

    @Override
    public void render() {
        if (mc.theWorld == null || mc.thePlayer == null) {
            return;
        }

        final NetworkPlayerInfo you = this.mc.getNetHandler().getPlayerInfo(this.mc.thePlayer.getUniqueID());

        if (you == null) {
            return;
        }

        //TODO: Ping Average and Ping Graph

        this.drawString("Ping: " + you.getResponseTime() + "ms", this.getPosX(), this.getPosY(), this.color.getRGB(), this.customFont);
    }

    @Override
    public void renderDummy() {
        this.drawString("Ping: 255ms", this.getPosX(), this.getPosY(), this.color.getRGB(), this.customFont);
    }

    @Override
    public int getWidth() {
        return this.getStringWidth("Ping: 255ms", this.customFont);
    }

    @Override
    public int getHeight() {
        return this.getFontHeight(this.customFont);
    }
}
