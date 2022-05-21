package dev.menace;

import java.awt.Color;
import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Stack;

import org.lwjgl.opengl.Display;

import com.mojang.authlib.exceptions.AuthenticationException;
import com.thealtening.utilities.SSLVerification;

import dev.menace.event.EventManager;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventKey;
import dev.menace.module.ModuleManager;
import dev.menace.ui.altmanager.LoginManager;
import dev.menace.ui.clickgui.csgo.CSGOGui;
import dev.menace.ui.clickgui.lime.LimeClickGUI;
import dev.menace.utils.file.FileManager;
import dev.menace.utils.misc.DiscordRP;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import net.minecraft.client.Minecraft;
import viamcp.ViaMCP;

public class Menace {

	public static Menace instance = new Menace();
	
	public Minecraft MC = Minecraft.getMinecraft();
	public EventManager eventManager = new EventManager();
	public ModuleManager moduleManager = new ModuleManager();
	public DiscordRP discordRP = new DiscordRP();
	
	public boolean isFirstLaunch;
	
	public void startClient() {
		System.out.println("[Menace] Starting Client...");
		
		Display.setTitle("Menace 1.8.9 - Recode");
		
		FileManager.init();
		
		discordRP.start();
		
		eventManager.register(this);
		
		try
		{
		  ViaMCP.getInstance().start();
		  ViaMCP.getInstance().initAsyncSlider();
		}
		catch (Exception e)
		{
		  e.printStackTrace();
		}
		
		SSLVerification ssl = new SSLVerification();
		ssl.verify();
		
		moduleManager.clickGuiModule.csgoGui = new CSGOGui();
		moduleManager.clickGuiModule.limeGui = new LimeClickGUI();
		
		if (isFirstLaunch) {
			moduleManager.saveModules("default");
		} else {
			moduleManager.loadModules("default");
		}
		if (!(new File(FileManager.getConfigFolder(), "Binds.json").exists())) {
			moduleManager.saveKeys();
		}
		
	}
	
	public void stopClient() {
		System.out.println("[Menace] Stopping client...");
		discordRP.stop();
		Menace.instance.moduleManager.saveModules(this.moduleManager.selectedConfig);
		eventManager.unregister(this);
	}
	
	@EventTarget
	public void onKey(EventKey event) {
		moduleManager.getModules().stream().filter(m -> m.getKeybind() == event.getKey()).forEach(module -> {
			module.toggle();
		});
	}

	public int getClientColor() {
		return Color.RED.getRGB();
	}

	public int getClientColorDarker() {
		return new Color(203, 26, 26).getRGB();
	}
}
