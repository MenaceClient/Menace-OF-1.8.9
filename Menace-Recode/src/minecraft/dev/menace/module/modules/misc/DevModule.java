package dev.menace.module.modules.misc;

import dev.menace.anticheat.HackerDetect;
import dev.menace.anticheat.check.BaseCheck;
import dev.menace.event.Event;
import dev.menace.event.EventTarget;
import dev.menace.event.events.*;
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
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import net.minecraft.util.Timer;
import org.lwjgl.input.Keyboard;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class DevModule extends BaseModule {

	public DevModule() {
		super("DevModule", Category.MISC, Keyboard.KEY_P);
	}

	@Override
	public void setup() {
		super.setup();
	}

	@EventTarget
	public void onEvent(EventAll event) {
		ChatUtils.message("Event: " + event.getEvent().getClass().getSimpleName().replaceFirst("E", "e"));
	}

}