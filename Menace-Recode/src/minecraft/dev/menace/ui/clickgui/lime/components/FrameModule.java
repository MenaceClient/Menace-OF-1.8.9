package dev.menace.ui.clickgui.lime.components;


import dev.menace.Menace;
import dev.menace.module.BaseModule;
import dev.menace.module.settings.ListSetting;
import dev.menace.module.settings.SliderSetting;
import dev.menace.module.settings.ToggleSetting;
import dev.menace.ui.clickgui.lime.Priority;
import dev.menace.ui.clickgui.lime.components.impl.BoolSetting;
import dev.menace.ui.clickgui.lime.components.impl.EnumSetting;
import dev.menace.ui.clickgui.lime.components.impl.SlideSetting;
import dev.menace.utils.render.ColorUtils;
import dev.menace.utils.render.RenderUtils;
import dev.menace.utils.render.animtion.Animate;
import dev.menace.utils.render.animtion.Easing;
import dev.menace.utils.render.font.MenaceFontRenderer;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.util.ArrayList;

public class FrameModule implements Priority {
    private final BaseModule module;
    private final ArrayList<Component> components;

    private final FrameCategory owner;

    private final Animate moduleAnimation;

    private int x, y;
    private int offset;

    private boolean opened;

    MenaceFontRenderer font = Menace.instance.productSans20;

    public FrameModule(BaseModule module, FrameCategory owner, int x, int y)
    {
        this.module = module;
        this.components = new ArrayList<>();
        this.owner = owner;
        this.moduleAnimation = new Animate();
        moduleAnimation.setMin(0).setMax(255).setReversed(!module.isToggled()).setEase(Easing.LINEAR);
        this.opened = false;

        this.x = x;
        this.y = y;

        if(module.hasSettings())
        {
        	module.getSettings().forEach(setting -> {
        		if(setting instanceof ToggleSetting)
                {
                    this.components.add(new BoolSetting(0, 0, this, setting));
                }
                if(setting instanceof ListSetting)
                {
                    this.components.add(new EnumSetting(0, 0, this, setting));
                }
                if(setting instanceof SliderSetting)
                {
                    this.components.add(new SlideSetting(0, 0, this, setting));
                }
        	});
        }
    }

	@SuppressWarnings("unused")
	public void drawScreen(int mouseX, int mouseY)
    {
        moduleAnimation.setReversed(!module.isToggled());
        moduleAnimation.setSpeed(1000).update();

        if (RenderUtils.hover(x, y, mouseX, mouseY, defaultWidth, moduleHeight) && hoveredColor) {
            GuiScreen.drawRect(x,y, x + defaultWidth, y + moduleHeight, darkerMainColor);
        }

        if(module.isToggled() || (moduleAnimation.isReversed() && moduleAnimation.getValue() != 0)) {
            GuiScreen.drawRect(x,y, x + defaultWidth, y + moduleHeight, ColorUtils.setAlpha(new Color(enabledColor), (int) moduleAnimation.getValue()).getRGB());
        }
        
        font.drawStringWithShadow(module.getName(), x+3, y + (moduleHeight / 2F - (font.getHeight() / 2F)), stringColor);

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
                    module.toggle();
                    break;
                case 1:
                    opened = !opened;
                    break;
                case 2:
                    //TODO: Bind
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
