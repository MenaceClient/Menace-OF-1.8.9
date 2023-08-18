package dev.menace.ui.clickgui.menace.components;

import dev.menace.Menace;
import dev.menace.module.BaseModule;
import dev.menace.ui.clickgui.menace.MenaceClickGui;
import dev.menace.utils.render.RenderUtils;
import dev.menace.utils.render.font.MenaceFontRenderer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class ModuleComponent {

    int x, y, width, height;
    private final MenaceFontRenderer fontRenderer;
    MenaceClickGui parent;

    public ModuleComponent(MenaceClickGui parent) {
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
        ResourceLocation icon1 = new ResourceLocation("menace/menaceui/module.png");

        for (BaseModule module : Menace.instance.moduleManager.getModulesByCategory(parent.selectedCategory)) {

            //Draw module background
            RenderUtils.drawRoundedRect(this.x + 5 + xOffset, this.y + 5 + yOffset, this.x + 5 + xOffset + 100, this.y + 5 + yOffset + 120, 5, new Color(39, 38, 42).getRGB());

            //Draw module name
            fontRenderer.drawCenteredString(module.getName(), this.x + 55 + xOffset, this.y + 12 + yOffset, Color.WHITE.getRGB());

            //Draw Icon
            RenderUtils.drawImage(this.x + 30 + xOffset, this.y + 25 + yOffset, 50, 50, icon1, new Color(255, 255, 255, 0));

            //Draw module keybind
            if (module == parent.settingKeybindModule) {
                fontRenderer.drawCenteredString("Setting Bind...", this.x + 55 + xOffset, this.y + 100 + yOffset, Color.WHITE.getRGB());
            } else {
                fontRenderer.drawCenteredString("Bind: " + Keyboard.getKeyName(module.getKeybind()), this.x + 55 + xOffset, this.y + 100 + yOffset, Color.WHITE.getRGB());
            }

            //Draw module state
            fontRenderer.drawCenteredString(module.isToggled() ? "Enabled" : "Disabled", this.x + 55 + xOffset, this.y + 112 + yOffset, module.isToggled() ? Color.GREEN.getRGB() : Color.RED.getRGB());

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

        for (BaseModule module : Menace.instance.moduleManager.getModulesByCategory(parent.selectedCategory)) {

            if (mouseX >= this.x + 5 + xOffset && mouseX <= this.x + 5 + xOffset + 100 && mouseY >= this.y + 5 + yOffset && mouseY <= this.y + 5 + yOffset + 120) {
                if (mouseButton == 0) {
                    module.toggle();
                } else if (mouseButton == 1) {
                    parent.selectedModule = parent.settingsMenuComponents.get(module);
                } else if (mouseButton == 2) {
                    parent.settingKeybindModule = module;
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
