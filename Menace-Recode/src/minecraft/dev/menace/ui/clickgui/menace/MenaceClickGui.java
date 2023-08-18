package dev.menace.ui.clickgui.menace;

import dev.menace.Menace;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.ui.clickgui.menace.components.CategorySelectorComponent;
import dev.menace.ui.clickgui.menace.components.ConfigMenuComponent;
import dev.menace.ui.clickgui.menace.components.ModuleComponent;
import dev.menace.ui.clickgui.menace.components.SettingsMenuComponent;
import dev.menace.utils.render.RenderUtils;
import dev.menace.utils.render.font.MenaceFontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MenaceClickGui extends GuiScreen {

    public final int x, y, width, height;
    public Category selectedCategory;
    public BaseModule settingKeybindModule;
    private final ConfigMenuComponent configMenuComponent;
    private boolean renderConfigMenu = false;
    public SettingsMenuComponent selectedModule;
    private final CategorySelectorComponent categorySelectorComponent;
    private final ModuleComponent moduleComponent;
    public final Map<BaseModule, SettingsMenuComponent> settingsMenuComponents = new HashMap<>();

    public MenaceClickGui() {
        this.x = 100;
        this.y = 100;
        this.width = GuiScreen.width - 100;
        this.height = GuiScreen.height - 100;

        configMenuComponent = new ConfigMenuComponent(this);
        categorySelectorComponent = new CategorySelectorComponent(this);
        moduleComponent = new ModuleComponent(this);

        for (BaseModule m : Menace.instance.moduleManager.getModules()) {
            settingsMenuComponents.put(m, new SettingsMenuComponent(this, m));
        }

        selectedCategory = Category.COMBAT;
        settingKeybindModule = null;
        selectedModule = null;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        //Background blur
        RenderUtils.drawVerticalGradient(0, 0, GuiScreen.width, GuiScreen.height, new Color(255, 0, 0, 100).getRGB(), new Color(0, 0, 0, 30).getRGB());

        //Main Panel
        RenderUtils.drawRoundedRect(this.x, this.y, this.width, this.height, 5, Color.BLACK.getRGB());

        //Logo
        RenderUtils.drawImage(this.x, this.y - 5, 50, 50, new ResourceLocation("menace/newicon.png"), new Color(255, 255, 255, 0));

        //Config Icon
        RenderUtils.drawImage(this.x + this.width - 100 - 25, this.y + 5, 20, 20, new ResourceLocation("menace/menaceui/config.png"), new Color(255, 255, 255, 0));

        //Config Menu
        if (renderConfigMenu) {
            configMenuComponent.draw(mouseX, mouseY);
            super.drawScreen(mouseX, mouseY, partialTicks);
            return;
        }

        //Settings Panel
        if (selectedModule != null) {
            selectedModule.draw(mouseX, mouseY);
            return;
        }

        //Modules
        moduleComponent.draw(mouseX, mouseY);

        //Draw category bar
        categorySelectorComponent.draw(mouseX, mouseY);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

        //Category bar
        if (categorySelectorComponent.mouseClicked(mouseX, mouseY, mouseButton)) {
            super.mouseClicked(mouseX, mouseY, mouseButton);
            return;
        }

        if (selectedModule != null) {

            //SettingsMenu
            selectedModule.mouseClicked(mouseX, mouseY, mouseButton);

            super.mouseClicked(mouseX, mouseY, mouseButton);
            return;
        }

        //modules
        moduleComponent.mouseClicked(mouseX, mouseY, mouseButton);

        //Config Icon
        if (RenderUtils.hover(this.x + this.width - 100 - 25, this.y + 5, mouseX, mouseY, 20, 20)) {
            renderConfigMenu = !renderConfigMenu;
        }

        //Config Menu
        if (renderConfigMenu) {
            configMenuComponent.mouseClicked(mouseX, mouseY, mouseButton);
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {

        //Settings Menu
        if (selectedModule != null && keyCode == Keyboard.KEY_ESCAPE) {
            selectedModule = null;
            return;
        }

        //Keybind
        if (settingKeybindModule != null) {
            if (keyCode == Keyboard.KEY_ESCAPE) {
                settingKeybindModule.setKeybind(0);
                settingKeybindModule = null;
                return;
            } else {
                settingKeybindModule.setKeybind(keyCode);
                settingKeybindModule = null;
            }
        }

        super.keyTyped(typedChar, keyCode);
    }
}
