package dev.menace.ui.hud.elements;

import dev.menace.event.events.EventRender2D;
import dev.menace.ui.hud.BaseElement;
import dev.menace.ui.hud.options.BooleanOption;
import dev.menace.ui.hud.options.ColorSelectOption;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.misc.ServerUtils;
import dev.menace.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;

import java.awt.*;

public class WatermarkElement extends BaseElement {

    private Color color;
    private boolean customFont;

    @Override
    public void setup() {
        this.addOption(new BooleanOption("Custom Font", false) {
            @Override
            public void update() {
                WatermarkElement.this.customFont = this.getValue();
            }
        });
        this.addOption(new ColorSelectOption("Color", Color.red) {
            @Override
            public void update() {
                ChatUtils.message("Color updated");
                WatermarkElement.this.color = this.getColor();
            }
        });
    }

    @Override
    public void render() {
        int width = this.getStringWidth("Menace | " + Minecraft.debugFPS + " FPS | " + ServerUtils.getRemoteIp().split(":")[0], customFont) + 2;
        RenderUtils.drawRect(this.getPosX(), this.getPosY(), this.getPosX() + width + 1, this.getPosY() + 2, this.color.getRGB());
        RenderUtils.drawRect(this.getPosX(), this.getPosY() + 2, this.getPosX() + width + 1, this.getPosY() + this.getFontHeight(customFont) + 4, new Color(0, 0, 0, 255).getRGB());
        this.drawString("Menace | " + Minecraft.debugFPS + " FPS | " + ServerUtils.getRemoteIp().split(":")[0], this.getPosX() + 2, this.getPosY() + 3.5, Color.WHITE.getRGB(), customFont);
    }

    @Override
    public void renderDummy() {
        RenderUtils.drawRect(this.getPosX(), this.getPosY(), this.getPosX() + this.getWidth() + 1, this.getPosY() + 2, this.color.getRGB());
        RenderUtils.drawRect(this.getPosX(), this.getPosY() + 2, this.getPosX() + this.getWidth() + 1, this.getPosY() + this.getFontHeight(customFont) + 4, new Color(0, 0, 0, 255).getRGB());
        this.drawString("Menace | 60 FPS | MenaceClient.me", this.getPosX() + 2, this.getPosY() + 3.5, Color.WHITE.getRGB(), customFont);
    }

    @Override
    public int getWidth() {
        return this.getStringWidth("Menace | 60 FPS | MenaceClient.me", customFont) + 2;
    }

    @Override
    public int getHeight() {
        return this.getFontHeight(customFont) + 3;
    }
}
