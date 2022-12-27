package dev.menace.module.modules.misc;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventMouse;
import dev.menace.event.events.EventSendPacket;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.security.HWIDManager;
import org.lwjgl.input.Keyboard;

public class DevModule extends BaseModule {

	public DevModule() {
		super("DevModule", Category.MISC, Keyboard.KEY_P);
	}
	
	@Override
	public void onEnable() {
		//ChatUtils.message("System HWID: " + HWIDManager.getHWIDNew());
		super.onEnable();
	}

	@EventTarget
	public void onSend(EventSendPacket event) {

	}

	@EventTarget
	public void onUpdate(EventUpdate event) {

	}

	@EventTarget
	public void onClick(EventMouse event) {

	}

	@Override
	public void onDisable() {
		super.onDisable();
	}

}
