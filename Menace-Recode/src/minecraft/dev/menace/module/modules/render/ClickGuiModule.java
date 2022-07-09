package dev.menace.module.modules.render;

import dev.menace.ui.clickgui.vape.VapeGui;
import org.lwjgl.input.Keyboard;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.settings.ListSetting;
import dev.menace.ui.clickgui.csgo.CSGOGui;
import dev.menace.ui.clickgui.lime.LimeClickGUI;

public class ClickGuiModule extends BaseModule {

	public CSGOGui csgoGui;
	public LimeClickGUI limeGui;
	
	ListSetting mode;
	
	public ClickGuiModule() {
		super("ClickGui", Category.RENDER, Keyboard.KEY_RSHIFT);
	}
	
	@Override
	public void setup() {
		mode = new ListSetting("Mode", true, "Dropdown", new String[] {"Dropdown", "Panel", "Dashboard"});
		this.rSetting(mode);
		super.setup();
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		if (mc.currentScreen instanceof CSGOGui || mc.currentScreen instanceof LimeClickGUI) {
			return;
		}
		
		switch (mode.getValue()) {
			case "Dropdown" :
				limeGui.config.reload();
				mc.displayGuiScreen(limeGui);
				break;
			case "Panel" :
				mc.displayGuiScreen(csgoGui);
				break;
			case "Dashboard" :
				mc.displayGuiScreen(new VapeGui());
				break;
		}
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		this.setToggled(false);
	}

}
