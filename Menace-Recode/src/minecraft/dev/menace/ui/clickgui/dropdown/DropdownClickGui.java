package dev.menace.ui.clickgui.dropdown;

import dev.menace.module.Category;
import dev.menace.ui.clickgui.dropdown.components.DropdownCategory;
import dev.menace.utils.render.RenderUtils;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class DropdownClickGui extends GuiScreen {

    ArrayList<DropdownCategory> categories = new ArrayList<>();

    public DropdownClickGui() {
        int xOff = 10;
        for (Category category : Category.values()) {
            categories.add(new DropdownCategory(category, xOff, 20));
            xOff += 135;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtils.drawVerticalGradient(0, 0, GuiScreen.width, GuiScreen.height, new Color(255, 0, 0, 100).getRGB(), new Color(0, 0, 0, 30).getRGB());

        int wheel = 0;
        if (Mouse.hasWheel()) {
            wheel = Mouse.getDWheel();
        }

        for (DropdownCategory category : categories) {
            category.handleScroll(mouseX, mouseY, wheel);
            category.draw(mouseX, mouseY);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (DropdownCategory category : categories) {
            category.mouseClicked(mouseX, mouseY, mouseButton);
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        for (DropdownCategory category : categories) {
            category.keyTyped(typedChar, keyCode);
        }
        super.keyTyped(typedChar, keyCode);
    }

}
