package dev.menace.ui.clickgui.lime.components.impl;

import dev.menace.module.settings.ListSetting;
import dev.menace.module.settings.Setting;
import dev.menace.ui.clickgui.lime.Priority;
import dev.menace.ui.clickgui.lime.components.Component;
import dev.menace.ui.clickgui.lime.components.FrameModule;
import dev.menace.ui.clickgui.lime.utils.render.RenderUtils;
import dev.menace.utils.render.MenaceFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class EnumSetting extends Component implements Priority {
	MenaceFontRenderer productSans20 = MenaceFontRenderer.getFontOnPC("ProductSans20", 20);
	
    public EnumSetting(int x, int y, FrameModule owner, Setting setting) {
        super(x, y, owner, setting);
    }

    @Override
    public void initGui() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
    	productSans20.drawStringWithShadow(getSetting().getName(), x + 5, y + (getOffset() / 2F - (productSans20.FONT_HEIGHT / 2F)), -1);
    	productSans20.drawStringWithShadow(((ListSetting) getSetting()).getValue().toUpperCase(), x + defaultWidth - productSans20.getStringWidth(((ListSetting) getSetting()).getValue().toUpperCase()) - 5, y + (getOffset() / 2F - (productSans20.FONT_HEIGHT / 2F)), -1);
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(RenderUtils.hover(x, y, mouseX, mouseY, defaultWidth, getOffset()))
        {
        	ListSetting enumValue = (ListSetting) getSetting();

            int enumIndex = 0;
            for(String _enum : enumValue.getOptions()) {
                if(_enum == enumValue.getValue()) break;
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
