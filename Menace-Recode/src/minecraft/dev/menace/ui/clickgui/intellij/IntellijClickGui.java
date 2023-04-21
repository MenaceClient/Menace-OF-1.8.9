package dev.menace.ui.clickgui.intellij;

import dev.menace.Menace;
import dev.menace.module.Category;
import dev.menace.ui.clickgui.intellij.components.IntellijCategory;
import dev.menace.utils.render.RenderUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class IntellijClickGui extends GuiScreen {

    public int x, y, width, height;
    public boolean mainPackageDropdown = false;

    private final List<IntellijCategory> categories;

    public IntellijClickGui() {
        this.x = 100;
        this.y = 100;
        this.width = GuiScreen.width - 100;
        this.height = GuiScreen.height - 100;

        this.categories = new ArrayList<>();

        int index = -1;
        for(Category category : Category.values())
        {
            categories.add(new IntellijCategory(category, this.x + 10, this.y + (++index * 10)));
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        //Draw intellij style gui

        //sidebar
        RenderUtils.drawRect(this.x, this.y, this.width, this.height, new Color(60,63,65,255).getRGB());

        //main panel
        RenderUtils.drawRect(this.x + 120, this.y, this.width, this.height, new Color(43,43,43,255).getRGB());

        //main category button

        if (mainPackageDropdown) {
            //TODO: if selected then \u02C5 or \u2304
            Menace.instance.jetbrainsMono.drawString("\u2304", this.x + 10, this.y + 7.5, new Color(170, 177, 181, 255).getRGB());
            AtomicInteger offset = new AtomicInteger();
            categories.forEach(category -> {
                category.drawScreen(mouseX, mouseY, offset.get());
                if (category.dropdown) {
                    offset.addAndGet(category.modules.size() * 10);
                }
            });
        } else {
            Menace.instance.jetbrainsMono.drawString(">", this.x + 10, this.y + 9, new Color(170, 177, 181, 255).getRGB());
        }
        Menace.instance.jetbrainsMono.drawString("modules", this.x + 27, this.y + 9, new Color(170, 177, 181, 255).getRGB());
        RenderUtils.drawImage(this.x + 18, this.y + 10, 7, 7, new ResourceLocation("menace/intellijclickgui/MainPackage.png"), new Color(255, 255, 255, 0));

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

        if (RenderUtils.hover(this.x + 10, this.y + 10, mouseX, mouseY, Menace.instance.jetbrainsMono.getStringWidth("modules") + 17, Menace.instance.jetbrainsMono.getHeight())) {
            mainPackageDropdown = !mainPackageDropdown;
        }

        AtomicInteger offset = new AtomicInteger();
        categories.forEach(category -> {
            category.mouseClicked(mouseX, mouseY, mouseButton, offset.get());
            if (category.dropdown) {
                offset.addAndGet(category.modules.size() * 10);
            }
        });

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        AtomicInteger offset = new AtomicInteger();
        categories.forEach(category -> {
            category.mouseReleased(mouseX, mouseY, state, offset.get());
            if (category.dropdown) {
                offset.addAndGet(category.modules.size() * 10);
            }
        });
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
