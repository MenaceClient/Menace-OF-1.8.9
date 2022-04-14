package dev.menace.module.modules.render;

import dev.menace.Menace;
import dev.menace.gui.clickgui.herocode.clickgui.ClickGUI;
import dev.menace.gui.clickgui.herocode.clickgui.util.ColorUtil;
import dev.menace.gui.clickgui.menace.MenaceClickGui;
import dev.menace.module.Category;
import dev.menace.module.Module;
import dev.menace.module.settings.*;
import net.minecraft.client.gui.ScaledResolution;

import org.lwjgl.input.Keyboard;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class ClickGui extends Module {
	
	public ClickGUI heroClickGui;
	public MenaceClickGui menaceClickGui;
	public dev.menace.gui.clickgui.lemon.click.ClickGui lemonClickGui;
	
	//Settings
	public StringSetting design;
	public BoolSetting sound;
	public DoubleSetting red;
	public DoubleSetting green;
	public DoubleSetting blue;
	
    public ClickGui() {
        super("ClickGUI", Keyboard.KEY_RSHIFT, Category.RENDER);
    }

    @Override
    public void setup() {
        ArrayList<String> options = new ArrayList<>();
        options.add("Herocode");
        options.add("JellyLike");
        options.add("Lemon");
        options.add("Menace");
        design = new StringSetting("Design", this, "Herocode", options);
        sound = new BoolSetting("Sound", this, false);
        red = new DoubleSetting("GuiRed", this, 255, 0, 255);
        green = new DoubleSetting("GuiGreen", this, 0, 0, 255);
        blue = new DoubleSetting("GuiBlue", this, 0, 0, 255);
        this.rSetting(design);
        this.rSetting(sound);
        this.rSetting(red);
        this.rSetting(green);
        this.rSetting(blue);
    }

    @Override
    public void onEnable() {
        super.onEnable();

        if (design.getString().equalsIgnoreCase("Herocode") || design.getString().equalsIgnoreCase("JellyLike")) { 
            MC.displayGuiScreen(heroClickGui);
        } else if (design.getString().equalsIgnoreCase("Menace")) {
        	MC.displayGuiScreen(menaceClickGui);
        } else if (design.getString().equalsIgnoreCase("Lemon")) {
        	lemonClickGui.color = ColorUtil.getClickGUIColor().getRGB();
        	MC.displayGuiScreen(lemonClickGui);
        }
        

        toggle();
    }
}
