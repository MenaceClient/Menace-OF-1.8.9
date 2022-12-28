package dev.menace.module.modules.misc;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventMouse;
import dev.menace.event.events.EventSendPacket;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.settings.ListSetting;
import dev.menace.module.settings.ToggleSetting;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.player.MovementUtils;
import dev.menace.utils.player.PlayerUtils;
import dev.menace.utils.security.HWIDManager;
import dev.menace.utils.timer.MSTimer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.util.Timer;
import org.lwjgl.input.Keyboard;

import java.io.*;

public class DevModule extends BaseModule {

	ToggleSetting antiAFK;

	MSTimer timer = new MSTimer();
	public DevModule() {
		super("DevModule", Category.MISC, Keyboard.KEY_P);
	}

	@Override
	public void setup() {
		antiAFK = new ToggleSetting("antiAFK", true, false);
		this.rSetting(antiAFK);
		super.setup();
	}

	@Override
	public void onEnable() {
		ChatUtils.message("Enabled Staff Uploader");
		super.onEnable();
	}

	@EventTarget
	public void onSend(EventSendPacket event) {

	}

	@EventTarget
	public void onUpdate(EventUpdate event) {
		Iterable<NetworkPlayerInfo> players = mc.getNetHandler().getPlayerInfoMap();

		players.forEach(player -> {
			String a = mc.ingameGUI.getTabList().getPlayerName(player);
			if (a != null && a.contains("§c")){
				ChatUtils.message("Found user. Sending to staff list.");
			}
		});

		if (antiAFK.getValue() && timer.hasTimePassed(10000)) {
			mc.thePlayer.jump();
			timer.reset();
		}
	}

	@EventTarget
	public void onClick(EventMouse event) {

	}

	@Override
	public void onDisable() {
		ChatUtils.message("Disabled Staff Uploader");
		super.onDisable();
	}
}