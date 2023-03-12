package dev.menace.ui.clickgui.menace;

import dev.menace.Menace;
import dev.menace.module.Category;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.render.RenderUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;

public class MenaceClickGui extends GuiScreen {

    public int x, y, width, height;
    public boolean mainPackageDropdown = false;

    public MenaceClickGui() {
        this.x = 100;
        this.y = 100;
        this.width = GuiScreen.width - 100;
        this.height = GuiScreen.height - 100;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);

        //Draw intellij style gui

        //sidebar
        RenderUtils.drawRect(this.x, this.y, this.width, this.height, new Color(60,63,65,255).getRGB());

        //main panel
        RenderUtils.drawRect(this.x + 120, this.y, this.width, this.height, new Color(43,43,43,255).getRGB());

        //main category button
        //TODO: if selected then \u02C5 or \u2304

        //mainPackageDropdown = true;
        if (false) {
            Menace.instance.jetbrainsMono.drawString("\u02C5", this.x + 10, this.y + 10, new Color(135,147,154,255).getRGB());
        } else {
            Menace.instance.jetbrainsMono.drawString(">", this.x + 10, this.y + 10.5, new Color(135,147,154,255).getRGB());
        }
        Menace.instance.jetbrainsMono.drawString("modules", this.x + 27, this.y + 11, new Color(135,147,154,255).getRGB());
        RenderUtils.drawImage(this.x + 18, this.y + 10, 7, 7, new ResourceLocation("menace/menaceclickgui/MainPackage.png"), new Color(255, 255, 255, 0));

        if (!mainPackageDropdown) return;

        int yy = 0;
        for (Category c : Category.values()) {
            if (false) {
                Menace.instance.jetbrainsMono.drawString("\u02C5", this.x + 18, this.y + 20 + yy, new Color(135,147,154,255).getRGB());
            } else {
                Menace.instance.jetbrainsMono.drawString(">", this.x + 18, this.y + 20.5 + yy, new Color(135,147,154,255).getRGB());
            }
            Menace.instance.jetbrainsMono.drawString(c.name().toLowerCase(), this.x + 35, this.y + 21 + yy, new Color(135,147,154,255).getRGB());
            RenderUtils.drawImage(this.x + 25, this.y + 20 + yy, 8, 7, new ResourceLocation("menace/menaceclickgui/Package.png"), new Color(255, 255, 255, 0));
            yy+=10;
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

        if (RenderUtils.hover(this.x + 10, this.y + 10, mouseX, mouseY, Menace.instance.jetbrainsMono.getStringWidth("modules") + 17, Menace.instance.jetbrainsMono.getHeight())) {
            mainPackageDropdown = !mainPackageDropdown;
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
}
