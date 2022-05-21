package dev.menace.ui.clickgui.lime.components.impl;

import dev.menace.module.settings.ListSetting;
import dev.menace.module.settings.Setting;
import dev.menace.ui.clickgui.lime.Priority;
import dev.menace.ui.clickgui.lime.components.Component;
import dev.menace.ui.clickgui.lime.components.FrameModule;
import dev.menace.ui.clickgui.lime.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class EnumSetting extends Component implements Priority {
    public EnumSetting(int x, int y, FrameModule owner, Setting setting) {
        super(x, y, owner, setting);
    }

    @Override
    public void initGui() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
    	FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
        fr.drawString(getSetting().getName(), x + 5, y + (getOffset() / 2F - (fr.FONT_HEIGHT / 2F)), -1, true);
        fr.drawString(((ListSetting) getSetting()).getValue().toUpperCase(), x + defaultWidth - fr.getStringWidth(((ListSetting) getSetting()).getValue().toUpperCase()) - 5, y + (getOffset() / 2F - (fr.FONT_HEIGHT / 2F)), -1, true);
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(RenderUtils.hover(x, y, mouseX, mouseY, defaultWidth, getOffset()))
        {
        	ListSetting enumValue = (ListSetting) getSetting();

            int enumIndex = 0;
            for(String _enum : enumValue.getOptions()) {
                if(_enum == enumValue.getName()) break;
                ++enumIndex;
            }

            if(mouseButton == 1) {
                if(enumIndex - 1 >= 0) {
                    enumValue.setValue(enumValue.getOptions()[enumIndex - 1]);
                } else {
                    enumValue.setValue(enumValue.getOptions()[enumValue.getOptions().length - 1]);
                }
            }

             if(mouseButton == 0) {
                if(enumIndex + 1 < enumValue.getOptions().length) {
                    enumValue.setValue(enumValue.getOptions()[enumIndex + 1]);
                } else {
                    enumValue.setValue(enumValue.getOptions()[0]);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void onGuiClosed(int mouseX, int mouseY, int mouseButton) {

    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {

    }

    @Override
    public int getOffset() {
        return 15;
    }
}
