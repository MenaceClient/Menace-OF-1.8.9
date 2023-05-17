package dev.menace;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.menace.anticheat.HackerDetect;
import dev.menace.command.CommandManager;
import dev.menace.event.EventManager;
import dev.menace.event.EventTarget;
import dev.menace.event.events.*;
import dev.menace.module.ModuleManager;
import dev.menace.module.config.ConfigManager;
import dev.menace.scripting.ScriptManager;
import dev.menace.ui.altmanager.LoginManager;
import dev.menace.ui.hud.HUDManager;
import dev.menace.utils.file.FileManager;
import dev.menace.utils.irc.IRCUtils;
import dev.menace.utils.misc.DiscordRP;
import dev.menace.utils.misc.ServerUtils;
import dev.menace.utils.notifications.Notification;
import dev.menace.utils.notifications.NotificationManager;
import dev.menace.utils.render.font.Fonts;
import dev.menace.utils.render.font.MenaceFontRenderer;
import dev.menace.utils.security.MenaceUser;
import dev.menace.utils.timer.MSTimer;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;
import org.jibble.pircbot.IrcException;
import org.lwjgl.opengl.Display;
import viamcp.ViaMCP;

import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class Menace {

	public static Menace instance = new Menace();
	public boolean starting;
	public Minecraft MC = Minecraft.getMinecraft();
	public String apiURL = "https://api.menaceclient.me/";
	public EventManager eventManager;
	public ModuleManager moduleManager;
	public CommandManager cmdManager;
	public ConfigManager configManager;
	public NotificationManager notificationManager;
	public HUDManager hudManager;
	public ScriptManager scriptManager;
	public HackerDetect hackerDetect;
	public IRCUtils ircBot;
	public DiscordRP discordRP;

	public MenaceUser user;
	public LinkedHashMap<String, String> onlineMenaceUsers = new LinkedHashMap<>();
	MSTimer updateTimer = new MSTimer();

	//Misc
	public boolean borderlessFullscreen = true;

	//Fonts
	public MenaceFontRenderer sfPro;
	public MenaceFontRenderer productSans20;
	public MenaceFontRenderer productSans24;
	public MenaceFontRenderer ascii24;
	public MenaceFontRenderer ascii18;
	public MenaceFontRenderer jetbrainsMono;

	public void initFonts() {
		sfPro = new MenaceFontRenderer(Fonts.fontFromTTF(new ResourceLocation("menace/fonts/SF-Pro.ttf"), 20, Font.PLAIN), true);
		productSans20 = new MenaceFontRenderer(Fonts.fontFromTTF(new ResourceLocation("menace/fonts/ProductSans.ttf"), 20, Font.PLAIN), true);
		productSans24 = new MenaceFontRenderer(Fonts.fontFromTTF(new ResourceLocation("menace/fonts/ProductSans.ttf"), 20, Font.PLAIN), true);
		ascii24 = new MenaceFontRenderer(Fonts.fontFromTTF(new ResourceLocation("menace/fonts/ascii.ttf"), 24, Font.PLAIN),  true);
		ascii18 = new MenaceFontRenderer(Fonts.fontFromTTF(new ResourceLocation("menace/fonts/ascii.ttf"), 18, Font.PLAIN),  true);
		jetbrainsMono = new MenaceFontRenderer(Fonts.fontFromTTF(new ResourceLocation("menace/fonts/JetBrainsMono-Regular.ttf"), 15, Font.PLAIN), true);
	}

	public void startClient() {
		starting = true;
		System.out.println("[Menace] Starting Client...");

		Display.setTitle("Menace 1.8.9 - Recode");

		FileManager.init();

		eventManager = new EventManager();

		moduleManager = new ModuleManager();

		cmdManager = new CommandManager();
		cmdManager.init();

		configManager = new ConfigManager();

		notificationManager = new NotificationManager();

		hudManager = new HUDManager();

		scriptManager = new ScriptManager();

		hackerDetect = new HackerDetect();

		discordRP = new DiscordRP();
		discordRP.start();

		eventManager.register(this);

		try {
			ViaMCP.getInstance().start();
			ViaMCP.getInstance().initAsyncSlider();
		} catch (Exception e) {
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
		hudManager.tabGuiElement.start();

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

		starting = false;
	}

	public void postinit() {
		new Thread() {
			@Override
			public void run() {
				ircBot = new IRCUtils();
				ircBot.setVerbose(false);
				ircBot.setAutoNickChange(true);
				try {
					ircBot.connect("chat.freenode.net");
				} catch (IOException | IrcException e) {
					System.out.println("[Menace] Failed to connect to IRC");
					throw new RuntimeException(e);
				}
				ircBot.joinChannel("#MenaceIRC5573");
				super.run();
			}
		}.start();
	}

	public void stopClient() {
		System.out.println("[Menace] Stopping client...");
		discordRP.stop();
		cmdManager.end();
		moduleManager.saveModules(this.configManager.getLoadedConfig().getName());
		hudManager.gameStatsElement.stop();
		hudManager.tabGuiElement.stop();
		ircBot.quitServer("Left the Game");
		eventManager.unregister(this);
	}

	@EventTarget
	public void onKey(EventKey event) {
		moduleManager.getModules().stream().filter(m -> m.getKeybind() == event.getKey()).forEach(module -> {
			module.toggle();
			notificationManager.registerNotification(new Notification("Toggle", module.getName() + " was toggled", 1000L, Color.red));
		});
	}

	@EventTarget
	public void onReceive(EventReceivePacket event) {
		this.moduleManager.autoPlayModule.onRecievePacket(event);
		if (event.getPacket() instanceof S02PacketChat) {
			String message = ((S02PacketChat) event.getPacket()).getChatComponent().getUnformattedText();

			if (message != null && message.contains(" killed by " + MC.thePlayer.getName())) {
				this.hudManager.gameStatsElement.kills++;
				this.moduleManager.killFXModule.onKill(message.split(" ")[0]);
			}

			final String[] formattedMessage = {((S02PacketChat) event.getPacket()).getChatComponent().getFormattedText()};
			onlineMenaceUsers.forEach((username, ign) -> {
				if (ign != null && formattedMessage[0].contains(ign)) {
					formattedMessage[0] = formattedMessage[0].replace(ign, ign + " §r(§b" + username + "§r)");
				}
			});

			((S02PacketChat) event.getPacket()).setChatComponent(new ChatComponentText(formattedMessage[0]));

		} else if (event.getPacket() instanceof S45PacketTitle) {
			S45PacketTitle packet = (S45PacketTitle) event.getPacket();
			if (packet == null || packet.getMessage() == null || packet.getMessage().getFormattedText() == null) return;
			final String[] formattedMessage = {packet.getMessage().getFormattedText()};

			onlineMenaceUsers.forEach((username, ign) -> {
				if (ign != null && formattedMessage[0].contains(ign)) {
					formattedMessage[0] = formattedMessage[0].replace(ign, ign + " §r(§b" + username + "§r)");
				}
			});

			packet.setMessage(new ChatComponentText(formattedMessage[0]));
		}
	}

	@EventTarget
	public void onConnection(EventConnection event) {
		if (event.getState() == EventConnection.State.CONNECTING) {
			Menace.instance.discordRP.update("Bypassing " + ServerUtils.getRemoteIp());
			Menace.instance.hudManager.gameStatsElement.reset();
			try {
				final URL url = new URL( apiURL + "/updateUser/" + ServerUtils.getRemoteIp().split(":")[0] + "/" + Menace.instance.user.getUsername() + "/" + MC.session.getUsername() + "/false");
				HttpURLConnection uc = (HttpURLConnection ) url.openConnection();
				uc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
				uc.setRequestMethod("GET");
				int responseCode = uc.getResponseCode();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			new Thread() {
				@Override
				public void run() {
					updateOnline();
					super.run();
				}
			}.start();
		} else {
			try {
				final URL url = new URL(apiURL + "/updateUser/" + ServerUtils.getLastServerIp().split(":")[0] + "/" + Menace.instance.user.getUsername() + "/" + MC.session.getUsername() + "/true");
				HttpURLConnection uc = (HttpURLConnection ) url.openConnection();
				uc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
				uc.setRequestMethod("GET");
				int responseCode = uc.getResponseCode();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@EventTarget
	public void onWorldChange(EventWorldChange event) {
		new Thread() {
			@Override
			public void run() {
				updateOnline();
				super.run();
			}
		}.start();

	}

	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (updateTimer.hasTimePassed(7000)) {
			new Thread() {
				@Override
				public void run() {
					updateOnline();
					super.run();
				}
			}.start();
		}
	}

	public void updateOnline() {

		if (!updateTimer.hasTimePassed(300)) {
			return;
		}

		try {
			String ip = ServerUtils.getRemoteIp().toLowerCase().contains("49.12.67.79") || ServerUtils.getRemoteIp().toLowerCase().contains("blocksmc.com") ? "blocksmc.com" : ServerUtils.getRemoteIp().toLowerCase().split(":")[0];
			final URL url = new URL(apiURL + "/getMenaceUsers/" + ip + "/");
			HttpURLConnection uc = (HttpURLConnection ) url.openConnection();
			uc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
			uc.setRequestMethod("GET");
			int responseCode = uc.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
				String inputLine;
				StringBuilder response = new StringBuilder();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();

				JsonObject server = new JsonParser().parse(response.toString()).getAsJsonObject();
				if (server.entrySet() != null && server.entrySet().size() > 0 && !server.entrySet().isEmpty()) {
					//WTF
					Map<String, String> map = server.entrySet().stream().filter(entry -> entry != null && entry.getKey() != null && entry.getValue() != null && !entry.getValue().isJsonNull() && !entry.getValue().getAsString().isEmpty()).collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue().getAsString()));
					onlineMenaceUsers = new LinkedHashMap<>(map);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		updateTimer.reset();

	}

	public int getClientColor() {
		return Color.RED.getRGB();
	}

	public int getClientColorDarker() {
		return new Color(203, 26, 26).getRGB();
	}
}
