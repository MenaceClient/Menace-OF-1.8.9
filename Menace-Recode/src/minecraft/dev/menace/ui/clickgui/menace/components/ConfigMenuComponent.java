package dev.menace.ui.clickgui.menace.components;

import dev.menace.Menace;
import dev.menace.module.BaseModule;
import dev.menace.module.config.Config;
import dev.menace.ui.clickgui.menace.MenaceClickGui;
import dev.menace.utils.render.RenderUtils;
import dev.menace.utils.render.font.MenaceFontRenderer;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class ConfigMenuComponent {

    int x, y, width, height;
    private final MenaceFontRenderer fontRenderer;
    MenaceClickGui parent;

    public ConfigMenuComponent(MenaceClickGui parent) {
        this.x = parent.x;
        this.y = parent.y;
        this.width = parent.width;
        this.height = parent.height;
        this.parent = parent;
        fontRenderer = Menace.instance.sfPro;
    }

    public void draw(int mouseX, int mouseY) {
        int yOffset = 40;
        int xOffset = 50;
        ResourceLocation icon1 = new ResourceLocation("menace/menaceui/config.png");

        for (Config config : Menace.instance.configManager.getConfigs()) {

            //Draw module background
            RenderUtils.drawRoundedRect(this.x + 5 + xOffset, this.y + 5 + yOffset, this.x + 5 + xOffset + 100, this.y + 5 + yOffset + 120, 5, new Color(39, 38, 42).getRGB());

            //Draw module name
            fontRenderer.drawCenteredString(config.getName(), this.x + 55 + xOffset, this.y + 12 + yOffset, Color.WHITE.getRGB());

            //Draw Icon
            RenderUtils.drawImage(this.x + 30 + xOffset, this.y + 25 + yOffset, 50, 50, icon1, new Color(255, 255, 255, 0));

            //Draw module state
            fontRenderer.drawCenteredString(Menace.instance.configManager.checkLoadedConfig(config) ? "Loaded" : "", this.x + 55 + xOffset, this.y + 112 + yOffset, Menace.instance.configManager.checkLoadedConfig(config) ? Color.GREEN.getRGB() : Color.RED.getRGB());

            xOffset += 110;

            if (xOffset >= this.width - 200) {
                xOffset = 50;
                yOffset += 130;
            }

        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        int yOffset = 40;
        int xOffset = 50;

            for (Config config : Menace.instance.configManager.getConfigs()) {
                if (mouseX >= this.x + 5 + xOffset && mouseX <= this.x + 5 + xOffset + 100 && mouseY >= this.y + 5 + yOffset && mouseY <= this.y + 5 + yOffset + 120) {
                    if (mouseButton == 0) {
                        config.load();
                    }
                }
                xOffset += 110;

                if (xOffset >= this.width - 200) {
                    xOffset = 50;
                    yOffset += 130;
                }
            }
    }

}
