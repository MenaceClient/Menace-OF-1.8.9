package dev.menace.ui.clickgui.lime;

import dev.menace.ui.clickgui.lime.components.FrameConfig;
import dev.menace.ui.clickgui.lime.components.FrameConfigs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import dev.menace.module.Category;
import dev.menace.ui.clickgui.lime.components.FrameCategory;

public class LimeClickGUI extends GuiScreen {
    private final List<FrameCategory> categories;
    public final FrameConfigs config;

    public LimeClickGUI()
    {
        this.categories = new ArrayList<>();

        int index = -1;
        for(Category category : Category.values())
        {
            categories.add(new FrameCategory(category, 10 + (++index * (Priority.defaultWidth + 10)), 10));
        }

        config = new FrameConfigs(10 + (++index * (Priority.defaultWidth + 10)), 10);
    }

    @Override
    public void initGui()
    {
        categories.forEach(FrameCategory::initGui);
        config.initGui();
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        categories.forEach(frameCategory -> frameCategory.drawScreen(mouseX, mouseY));
        config.drawScreen(mouseX, mouseY);
		ScaledResolution s = new ScaledResolution(mc);
  		GL11.glPushMatrix();
		GL11.glTranslated(s.getScaledWidth(), s.getScaledHeight(), 0);GL11.glScaled(0.5, 0.5, 0.5);
		Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("b"+"y"+ "W"+"y"+"k"+"t", -Minecraft.getMinecraft().fontRendererObj.getStringWidth("b"+"y"+ "W"+"y"+"k"+"t"), -Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT, 0xff11F86B);
		GL11.glPopMatrix();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        categories.forEach(frameCategory -> frameCategory.mouseClicked(mouseX, mouseY, mouseButton));
        config.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state)
    {
        categories.forEach(frameCategory -> frameCategory.mouseReleased(mouseX, mouseY, state));
        config.mouseReleased(mouseX, mouseY, state);
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
}
