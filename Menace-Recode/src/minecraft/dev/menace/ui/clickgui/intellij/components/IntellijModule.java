package dev.menace.ui.clickgui.intellij.components;

import dev.menace.Menace;
import dev.menace.module.BaseModule;
import dev.menace.module.settings.ListSetting;
import dev.menace.module.settings.SliderSetting;
import dev.menace.module.settings.ToggleSetting;
import dev.menace.ui.clickgui.intellij.components.impl.BoolComponent;
import dev.menace.ui.clickgui.intellij.components.impl.EnumComponent;
import dev.menace.ui.clickgui.intellij.components.impl.SlideComponent;
import dev.menace.utils.render.RenderUtils;
import dev.menace.utils.render.font.MenaceFontRenderer;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.ArrayList;

public class IntellijModule {

    private final BaseModule module;
    private final ArrayList<Component> components;

    private final IntellijCategory owner;
    private int x, y;
    private int offset;

    private boolean opened;

    MenaceFontRenderer font = Menace.instance.jetbrainsMono;

    public IntellijModule(BaseModule module, IntellijCategory owner, int x, int y)
    {
        this.module = module;
        this.components = new ArrayList<>();
        this.owner = owner;
        this.opened = false;

        this.x = x;
        this.y = y;

        if(module.hasSettings())
        {
            module.getSettings().forEach(setting -> {
                if(setting instanceof ToggleSetting)
                {
                    this.components.add(new BoolComponent(0, 0, this, setting));
                }
                if(setting instanceof ListSetting)
                {
                    this.components.add(new EnumComponent(0, 0, this, setting));
                }
                if(setting instanceof SliderSetting)
                {
                    this.components.add(new SlideComponent(0, 0, this, setting));
                }
            });
        }
    }


    public void drawScreen(int mouseX, int mouseY, int offset) {
        Menace.instance.jetbrainsMono.drawString(this.module.getName(), this.x + 35, this.y + 19.5 + offset, new Color(170, 177, 181, 255).getRGB());
        RenderUtils.drawImage(this.x + 25, this.y + 20 + offset, 7, 7, new ResourceLocation("menace/intellijclickgui/Class.png"), new Color(255, 255, 255, 0));
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
    }
}
