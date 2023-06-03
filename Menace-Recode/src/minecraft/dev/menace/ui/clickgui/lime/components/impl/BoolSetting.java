package dev.menace.ui.clickgui.lime.components.impl;

import dev.menace.Menace;
import dev.menace.module.settings.Setting;
import dev.menace.module.settings.ToggleSetting;
import dev.menace.ui.clickgui.lime.Priority;
import dev.menace.ui.clickgui.lime.components.Component;
import dev.menace.ui.clickgui.lime.components.FrameModule;
import dev.menace.utils.render.RenderUtils;
import dev.menace.utils.render.animtion.Animate;
import dev.menace.utils.render.animtion.Easing;
import dev.menace.utils.render.font.MenaceFontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class BoolSetting extends Component implements Priority {
    private final Animate animation;
    MenaceFontRenderer font = Menace.instance.productSans20;

    public BoolSetting(int x, int y, FrameModule owner, Setting setting)
    {
        super(x, y, owner, setting);
        this.animation = new Animate().setMin(0).setMax(5).setSpeed(15).setEase(Easing.LINEAR).setReversed(!((ToggleSetting) setting).getValue());
    }

    @Override
    public void initGui()
    {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY)
    {
        ToggleSetting set = (ToggleSetting) getSetting();
        if (animation.isReversed() == set.getValue()) {
            animation.setReversed(!set.getValue());
        }

        animation.update();
        font.drawStringWithShadow(getSetting().getName(), x + 5, y + (getOffset() / 2F - (font.getHeight() / 2F)), -1);
        RenderUtils.drawFilledCircle(x + defaultWidth - 10, (int) (y + (getOffset() / 2F - (font.getHeight() / 2F)) + 6.75f), 5, new Color(darkerMainColor));

        if(((ToggleSetting) getSetting()).getValue() || animation.getValue() != 0)
        {
            RenderUtils.drawFilledCircle(x + defaultWidth - 10, (int) (y + (getOffset() / 2F - (font.getHeight() / 2F)) + 6.75f), animation.getValue(), new Color(enabledColor));
            GlStateManager.resetColor();
            GL11.glColor4f(1, 1, 1, 1);
        }
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton)
    {
        if(RenderUtils.hover(x, y, mouseX, mouseY, defaultWidth, getOffset())) {
        	ToggleSetting set = (ToggleSetting) getSetting();
            set.setValue(!set.getValue());
            animation.setReversed(!set.getValue());
            return true;
        }
        return false;
    }

    @Override
    public void onGuiClosed(int mouseX, int mouseY, int mouseButton)
    {

    }

    @Override
    public void keyTyped(char typedChar, int keyCode)
    {

    }

    @Override
    public int getOffset()
    {
        return 15;
    }
}
