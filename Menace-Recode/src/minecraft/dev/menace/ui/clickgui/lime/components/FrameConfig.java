package dev.menace.ui.clickgui.lime.components;


import java.awt.Color;
import java.util.ArrayList;

import dev.menace.module.BaseModule;
import dev.menace.module.config.Config;
import dev.menace.module.settings.ListSetting;
import dev.menace.module.settings.SliderSetting;
import dev.menace.module.settings.ToggleSetting;
import dev.menace.ui.clickgui.lime.Priority;
import dev.menace.ui.clickgui.lime.components.impl.BoolSetting;
import dev.menace.ui.clickgui.lime.components.impl.EnumSetting;
import dev.menace.ui.clickgui.lime.components.impl.SlideSetting;
import dev.menace.ui.clickgui.lime.utils.render.ColorUtils;
import dev.menace.ui.clickgui.lime.utils.render.RenderUtils;
import dev.menace.ui.clickgui.lime.utils.render.animation.easings.Animate;
import dev.menace.ui.clickgui.lime.utils.render.animation.easings.Easing;
import dev.menace.utils.render.MenaceFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import org.jetbrains.annotations.NotNull;

public class FrameConfig implements Priority {
    private final Config config;
    private final ArrayList<Component> components;

    private final FrameConfigs owner;

    private final Animate moduleAnimation;

    private int x, y;
    private int offset;

    private boolean opened;

    MenaceFontRenderer productSans20 = MenaceFontRenderer.getFontOnPC("ProductSans20", 20);

    public FrameConfig(@NotNull Config config, FrameConfigs owner, int x, int y)
    {
        this.config = config;
        this.components = new ArrayList<>();
        this.owner = owner;
        this.moduleAnimation = new Animate();
        moduleAnimation.setMin(0).setMax(255).setReversed(!config.isLoaded()).setEase(Easing.LINEAR);
        this.opened = false;

        this.x = x;
        this.y = y;
    }

    public void drawScreen(int mouseX, int mouseY)
    {
        moduleAnimation.setReversed(!config.isLoaded());
        moduleAnimation.setSpeed(1000).update();

  //      if (RenderUtils.hover(x, y, mouseX, mouseY, defaultWidth, moduleHeight) && hoveredColor) {
  //          GuiScreen.drawRect(x,y, x + defaultWidth, y + moduleHeight, darkerMainColor);
   //     }

        if(config.isLoaded() || (moduleAnimation.isReversed() && moduleAnimation.getValue() != 0)) {
            GuiScreen.drawRect(x,y, x + defaultWidth, y + moduleHeight, ColorUtils.setAlpha(new Color(enabledColor), (int) moduleAnimation.getValue()).getRGB());
        }

        productSans20.drawStringWithShadow(config.getName(), x+3, y + (moduleHeight / 2F - (productSans20.FONT_HEIGHT / 2F)) - 2, stringColor);

        int offset = 0;

        if(opened) {
            for (Component component : this.components) { // using for loop because continue isn't supported on foreach
                component.getSetting().constantCheck();
                if(!component.getSetting().isVisible()) continue;

                component.setX(x);
                component.setY(y + moduleHeight + offset);

                component.drawScreen(mouseX, mouseY);

                offset += component.getOffset();
            }
        }

        this.setOffset(moduleHeight + offset);
    }

    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton)
    {
        if(RenderUtils.hover(x, y, mouseX, mouseY, defaultWidth, moduleHeight) && RenderUtils.hover(owner.getX(), owner.getY(), mouseX, mouseY, defaultWidth, owner.getHeight()))
        {
            switch(mouseButton)
            {
                case 0:
                    config.load();
                    break;
                case 1:
                    opened = !opened;
                    break;
            }
            return true;
        }

        if(RenderUtils.hover(owner.getX(), owner.getY(), mouseX, mouseY, defaultWidth, owner.getHeight()) && opened) {
            for (Component component : this.components) {
                if(component.getSetting().isVisible() && component.mouseClicked(mouseX, mouseY, mouseButton))
                    return true;
            }
        }

        return false;
    }

    public int getOffset() {
        offset = 0;
        if(opened) {
            for (Component component : this.components) { // using for loop because continue isn't supported on foreach
                component.getSetting().constantCheck();
                if(!component.getSetting().isVisible()) continue;

                offset += component.getOffset();
            }
        }

        this.setOffset(moduleHeight + offset);
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
