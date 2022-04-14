package dev.menace;

import java.awt.Color;

import org.apache.commons.lang3.SystemUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import com.mojang.authlib.exceptions.AuthenticationException;
import dev.menace.altmanager.directlogin.AltLoginThread;
import dev.menace.altmanager.directlogin.AltLoginThreadMsa;
import dev.menace.command.CommandManager;
import dev.menace.event.EventManager;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventKey;
import dev.menace.gui.clickgui.herocode.clickgui.ClickGUI;
import dev.menace.gui.clickgui.lemon.click.ClickGui;
import dev.menace.gui.clickgui.menace.MenaceClickGui;
import dev.menace.gui.hud.HUDManager;
import dev.menace.gui.hud.element.ElementManager;
import dev.menace.module.Category;
import dev.menace.module.ModuleManager;
import dev.menace.utils.entity.self.PlayerUtils;
import dev.menace.utils.entity.self.Rotations;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.misc.DiscordRP;
import dev.menace.utils.misc.file.FileManager;
import dev.menace.utils.misc.notif.Notification;
import dev.menace.utils.misc.notif.NotificationManager;
import dev.menace.utils.render.SplashProgress;
import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import fr.litarvan.openauth.microsoft.model.response.MinecraftProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Session;
import viamcp.ViaMCP;
import dev.menace.module.Module;

public class Menace {

	public static final Menace instance = new Menace();
	
	public ModuleManager moduleManager;
	public EventManager eventManager;
	public CommandManager commandManager;
	public HUDManager hudManager;
	public ElementManager elementManager;
	public DiscordRP discordRP = new DiscordRP();
	public NotificationManager notificationManager;
	public boolean initializing;
	public boolean isFirstLaunch;
	//TODO: change this when public release
	public boolean beta = true;

	public Minecraft MC = Minecraft.getMinecraft();

	public void startClient() {
		System.out.println("[Menace] Starting client...");
		initializing = true;
		SplashProgress.setProgress(2, "Initializing Events");
		eventManager = new EventManager();
		hudManager = new HUDManager();
		hudManager.init();
		elementManager = new ElementManager(hudManager);
		SplashProgress.setProgress(3, "Initializing Modules");
		moduleManager = new ModuleManager();
		SplashProgress.setProgress(4, "Initializing Commands");
		commandManager = new CommandManager();
		Display.setTitle("Menace 1.8.9");

		eventManager.register(this);

		MC.gameSettings.anaglyph = false;

		commandManager.init();

		PlayerUtils pl = new PlayerUtils();
		pl.init();

		Rotations r = new Rotations();
		r.init();
		
		ChatUtils cu = new ChatUtils();
		cu.init();
		
		try
		{
		  ViaMCP.getInstance().start();

		  ViaMCP.getInstance().initAsyncSlider();
		}
		catch (Exception e)
		{
		  e.printStackTrace();
		}
		
		notificationManager = new NotificationManager();
		
		login();

		SplashProgress.setProgress(5, "Initializing Discord RP");
		discordRP.start();
		
		this.moduleManager.clickGuiModule.heroClickGui = new ClickGUI();
		this.moduleManager.clickGuiModule.lemonClickGui = new ClickGui();
		this.moduleManager.clickGuiModule.menaceClickGui = new MenaceClickGui();
		
		initializing = false;
	}

	public void stopClient() {
		System.out.println("[Menace] Stopping client...");
		for (Module m : Menace.instance.moduleManager.modules) {
			m.settingsSave();
		}
		eventManager.unregister(this);
		discordRP.stop();
	}

	@EventTarget
	public void onKey(EventKey event) {
		moduleManager.getModules().stream().filter(module -> module.getKey() == event.getKey()).forEach(module -> { 
			module.toggle();
			
			String toggled = module.isToggled() ? "Enabled " : "Disabled ";
			Color toggledColor = module.isToggled() ? Color.BLUE : Color.RED;
			
			notificationManager.addNotification(new Notification(toggled + module.getName(), toggledColor, 2100L));
		});

		if (elementManager.tabGuiElement.isEnabled()) {
			if (event.getKey() == Keyboard.KEY_DOWN) {
				int i=0;
				for (Module m : moduleManager.modules) {
					if (m.getCategory() == elementManager.tabGuiElement.selectedCategory) {
						i++;
					}
				}

				if (!elementManager.tabGuiElement.dropDown && elementManager.tabGuiElement.selected != 6) {
					elementManager.tabGuiElement.selected++;
				} else if (elementManager.tabGuiElement.dropDown && elementManager.tabGuiElement.selectedM != i-1) {
					elementManager.tabGuiElement.selectedM++;
				}
			}
			if (event.getKey() == Keyboard.KEY_UP) {
				if (!elementManager.tabGuiElement.dropDown && elementManager.tabGuiElement.selected != 0) {
					elementManager.tabGuiElement.selected--;
				} else if (elementManager.tabGuiElement.dropDown && elementManager.tabGuiElement.selectedM != 0) {
					elementManager.tabGuiElement.selectedM--;
				}
			}
			if (event.getKey() == Keyboard.KEY_RIGHT) {
				if (!elementManager.tabGuiElement.dropDown) {
					elementManager.tabGuiElement.dropDown = true;
					elementManager.tabGuiElement.selectedM = 0;
				} else {
					elementManager.tabGuiElement.selectedModule.toggle();
				}
			}
			if (event.getKey() == Keyboard.KEY_LEFT && elementManager.tabGuiElement.dropDown) {
				elementManager.tabGuiElement.dropDown = false;
			}
		}
	}
	
	/*TODO: REMOVE THIS BEFORE SENDING*/
	public void login() {
    	MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
		try {
			MicrosoftAuthResult result = authenticator.loginWithRefreshToken("refreshToken");
			MinecraftProfile profile = result.getProfile();
			MC.session = new Session(profile.getName(), profile.getId(), result.getAccessToken(), "mojang");
		} catch (MicrosoftAuthenticationException e) {
			e.printStackTrace();
		}
		System.out.println("Login Successful");
	}
	
}
