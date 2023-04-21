package dev.menace.ui.clickgui.intellij.components;

import dev.menace.Menace;
import dev.menace.module.Category;
import dev.menace.utils.render.RenderUtils;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class IntellijCategory {

    private int x, y;

    private final Category category;

    public final ArrayList<IntellijModule> modules;

    public boolean dropdown = false;

    // Asking x and y so categories are not on themself
    public IntellijCategory(Category category, int x, int y)
    {
        this.category = category;
        this.modules = new ArrayList<>();
        this.x = x;
        this.y = y;

        AtomicInteger index = new AtomicInteger(0);
        Menace.instance.moduleManager.getModulesByCategory(category).forEach(module -> this.modules.add(new IntellijModule(module, this, this.x + 10, this.y + (index.incrementAndGet() * 10))));
    }

    public void drawScreen(int mouseX, int mouseY, int offset) {
        if (dropdown) {
            Menace.instance.jetbrainsMono.drawString("\u2304", this.x + 18, this.y + 17.5 + offset, new Color(170, 177, 181, 255).getRGB());
            modules.forEach(module -> module.drawScreen(mouseX, mouseY, offset));
        } else {
            Menace.instance.jetbrainsMono.drawString(">", this.x + 18, this.y + 19 + offset, new Color(170, 177, 181, 255).getRGB());
        }
        Menace.instance.jetbrainsMono.drawString(this.category.getName().toLowerCase(), this.x + 35, this.y + 20 + offset, new Color(170, 177, 181, 255).getRGB());
        RenderUtils.drawImage(this.x + 25, this.y + 20 + offset, 8, 7, new ResourceLocation("menace/intellijclickgui/Package.png"), new Color(255, 255, 255, 0));
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton, int offset) {
        if (RenderUtils.hover(this.x + 10, (int) (this.y + 20 + offset), mouseX, mouseY, Menace.instance.jetbrainsMono.getStringWidth(this.category.getName().toLowerCase()) + 17, Menace.instance.jetbrainsMono.getHeight())) {
            dropdown = !dropdown;
        }

        if (dropdown) {
            modules.forEach(module -> module.mouseClicked(mouseX, mouseY, mouseButton));
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state, int offset) {
        if (dropdown) {
            modules.forEach(module -> module.mouseReleased(mouseX, mouseY, state));
        }
    }
}
