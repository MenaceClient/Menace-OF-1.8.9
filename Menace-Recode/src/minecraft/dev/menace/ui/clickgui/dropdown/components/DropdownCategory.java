package dev.menace.ui.clickgui.dropdown.components;

import dev.menace.Menace;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.render.RenderUtils;
import dev.menace.utils.render.font.MenaceFontRenderer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.ArrayList;

public class DropdownCategory {

    private Category category;
    private int x, y, width, height;
    private int scrollY;
    private boolean expanded = true;
    MenaceFontRenderer fr;
    ArrayList<DropdownModule> modules = new ArrayList<>();

    public DropdownCategory(Category category, int x, int y) {
        this.category = category;
        this.x = x;
        this.y = y;
        this.width = 125;
        this.height = 20;

        this.fr = Menace.instance.sfPro;

        for (BaseModule module : Menace.instance.moduleManager.getModulesByCategory(category)) {
            modules.add(new DropdownModule(module));
        }
    }

    public void draw(int mouseX, int mouseY) {

        RenderUtils.customRounded(x, y, x + width, y + height, 5, 5, expanded ? 0 : 5, expanded ? 0 : 5, Color.BLACK.getRGB());

        ResourceLocation icon = new ResourceLocation("menace/menaceui/" + category.getName().toLowerCase() + ".png");
        RenderUtils.drawImage(x + 2, y + 2, 16, 16, icon, new Color(255, 255, 255, 0));

        //fr.drawString(category.getName(), x + width - fr.getStringWidth(category.getName()) - 5, y + 4, Color.WHITE.getRGB());
        fr.drawCenteredString(category.getName(), x + width / 2, y + height / 2, Color.WHITE.getRGB());

        if (expanded) {
            int offsetY = y + 20;
            int i = scrollY;
            for (DropdownModule module : modules) {
                if (i > 0) {
                    i--;
                    continue;
                }
                module.draw(x, offsetY, mouseX, mouseY);
                offsetY += module.getHeight();
            }
        }

    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

        if (RenderUtils.hover(x, y, mouseX, mouseY, width, height) && mouseButton == 1) {
            expanded = !expanded;
        }

        if (expanded) {
            int offsetY = y + 20;
            int i = scrollY;
            for (DropdownModule module : modules) {
                if (i > 0) {
                    i--;
                    continue;
                }
                module.mouseClicked(x, offsetY, mouseX, mouseY, mouseButton);
                offsetY += module.getHeight();
            }
        }
    }

    public void keyTyped(char typedChar, int keyCode) {
        if (expanded) {
            int i = scrollY;
            for (DropdownModule module : modules) {
                if (i > 0) {
                    i--;
                    continue;
                }
                module.keyTyped(typedChar, keyCode);
            }
        }
    }

    public void handleScroll(int mouseX, int mouseY, int dWheel) {
        if (RenderUtils.hover(x, y, mouseX, mouseY, width, height) && dWheel != 0) {
            scrollY += dWheel / 120;
        }
    }

}
