package dev.menace.module.modules.render;

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
		mode = new ListSetting("Mode", true, "CSGO", new String[] {"Lime", "CSGO"});
		this.rSetting(mode);
		super.setup();
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		if (MC.currentScreen instanceof CSGOGui || MC.currentScreen instanceof LimeClickGUI) {
			return;
		}
		
		switch (mode.getValue()) {
		case "Lime" : 
			MC.displayGuiScreen(limeGui);
			break;
		case "CSGO" :
			MC.displayGuiScreen(csgoGui);
			break;
		}
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		this.setToggled(false);
	}

}
