package dev.menace;

import dev.menace.command.CommandManager;
import dev.menace.event.EventManager;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventKey;
import dev.menace.event.events.EventReceivePacket;
import dev.menace.module.BaseModule;
import dev.menace.module.ModuleManager;
import dev.menace.module.config.ConfigManager;
import dev.menace.ui.altmanager.LoginManager;
import dev.menace.ui.clickgui.csgo.CSGOGui;
import dev.menace.ui.clickgui.lime.LimeClickGUI;
import dev.menace.ui.hud.HUDManager;
import dev.menace.utils.file.FileManager;
import dev.menace.utils.misc.DiscordRP;
import dev.menace.utils.security.MenaceUser;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import net.arikia.dev.drpc.DiscordUser;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S02PacketChat;
import org.lwjgl.opengl.Display;
import viamcp.ViaMCP;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class Menace {

	public static Menace instance = new Menace();
	public Minecraft MC = Minecraft.getMinecraft();
	public EventManager eventManager;
	public ModuleManager moduleManager;
	public CommandManager cmdManager;
	public ConfigManager configManager;
	public HUDManager hudManager;
	public DiscordRP discordRP;
	public DiscordUser discordUser;
	public MenaceUser user;
	
	public void startClient() {
		System.out.println("[Menace] Starting Client...");
		
		Display.setTitle("Menace 1.8.9 - Recode");
		
		FileManager.init();
		
		eventManager = new EventManager();
		
		moduleManager = new ModuleManager();
		
		cmdManager = new CommandManager();
		cmdManager.init();

		configManager = new ConfigManager();
		
		hudManager = new HUDManager();
		
		discordRP = new DiscordRP();
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

		File yes = new File("D:/ez/lol/sex/halal/____.____");

		if (yes.exists()) {
			try {
				Scanner bs = new Scanner(yes);
				String skul = bs.nextLine();
				LoginManager.microsoftEmailLogin(skul.split(":")[0], skul.split(":")[1]);
			} catch (FileNotFoundException | MicrosoftAuthenticationException | NoSuchFieldException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		hudManager.gameStatsElement.start();

		configManager.reload();

		AtomicBoolean loaded = new AtomicBoolean(false);

		configManager.getConfigs().forEach(c -> {
			if (c.getName().equalsIgnoreCase("default")) {
				c.load();
				loaded.set(true);
			}
		});

		if (!loaded.get()) {
			moduleManager.saveModules("default");
			configManager.reload();
			configManager.getConfigs().forEach(c -> {
				if (c.getName().equalsIgnoreCase("default")) {
					c.load();
				}
			});
		}

		if (!(new File(FileManager.getConfigFolder(), "Binds.json").exists())) {
			moduleManager.saveKeys();
		}

		moduleManager.clickGuiModule.csgoGui = new CSGOGui();
		moduleManager.clickGuiModule.limeGui = new LimeClickGUI();
	}
	
	public void stopClient() {
		System.out.println("[Menace] Stopping client...");
		discordRP.stop();
		cmdManager.end();
		moduleManager.saveModules(this.configManager.getLoadedConfig().getName());
		hudManager.gameStatsElement.stop();
		eventManager.unregister(this);
	}
	
	@EventTarget
	public void onKey(EventKey event) {
		moduleManager.getModules().stream().filter(m -> m.getKeybind() == event.getKey()).forEach(BaseModule::toggle);
	}

	@EventTarget
	public void onRecieve(EventReceivePacket event) {
		if (event.getPacket() instanceof S02PacketChat) {
			String message = ((S02PacketChat) event.getPacket()).getChatComponent().getUnformattedText();
			if (message.contains(" was killed by " + MC.thePlayer.getName())) {
				this.hudManager.gameStatsElement.kills++;
			}
		}
	}

	public int getClientColor() {
		return Color.RED.getRGB();
	}

	public int getClientColorDarker() {
		return new Color(203, 26, 26).getRGB();
	}
}
