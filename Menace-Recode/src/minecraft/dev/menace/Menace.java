package dev.menace;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.menace.command.CommandManager;
import dev.menace.event.EventManager;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventKey;
import dev.menace.event.events.EventReceivePacket;
import dev.menace.event.events.EventWorldChange;
import dev.menace.module.ModuleManager;
import dev.menace.module.config.ConfigManager;
import dev.menace.ui.altmanager.LoginManager;
import dev.menace.ui.clickgui.csgo.CSGOGui;
import dev.menace.ui.clickgui.lime.LimeClickGUI;
import dev.menace.ui.clickgui.menace.MenaceClickGui;
import dev.menace.ui.hud.HUDManager;
import dev.menace.utils.file.FileManager;
import dev.menace.utils.irc.IRCClient;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.misc.DiscordRP;
import dev.menace.utils.misc.ServerUtils;
import dev.menace.utils.notifications.Notification;
import dev.menace.utils.notifications.NotificationManager;
import dev.menace.utils.render.font.Fonts;
import dev.menace.utils.render.font.MenaceFontRenderer;
import dev.menace.utils.security.MenaceUser;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import jdk.nashorn.internal.parser.JSONParser;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.Display;
import viamcp.ViaMCP;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Menace {

	public static Menace instance = new Menace();
	public boolean starting;
	public Minecraft MC = Minecraft.getMinecraft();
	public EventManager eventManager;
	public ModuleManager moduleManager;
	public CommandManager cmdManager;
	public ConfigManager configManager;
	public NotificationManager notificationManager;
	public HUDManager hudManager;
	public IRCClient irc;
	public DiscordRP discordRP;

	public MenaceUser user;
	public LinkedHashMap<String, String> onlineMenaceUsers = new LinkedHashMap<>();

	//Fonts
	public MenaceFontRenderer sfPro;
	public MenaceFontRenderer productSans20;
	public MenaceFontRenderer productSans24;
	public MenaceFontRenderer ascii24;
	public MenaceFontRenderer ascii18;
	public MenaceFontRenderer jetbrainsMono;

	public void initFonts() {
		sfPro = new MenaceFontRenderer(Fonts.fontFromTTF(new ResourceLocation("menace/fonts/SF-Pro.ttf"), 20, Font.PLAIN), true, true);
		productSans20 = new MenaceFontRenderer(Fonts.fontFromTTF(new ResourceLocation("menace/fonts/ProductSans.ttf"), 20, Font.PLAIN), true, true);
		productSans24 = new MenaceFontRenderer(Fonts.fontFromTTF(new ResourceLocation("menace/fonts/ProductSans.ttf"), 20, Font.PLAIN), true, true);
		ascii24 = new MenaceFontRenderer(Fonts.fontFromTTF(new ResourceLocation("menace/fonts/ascii.ttf"), 24, Font.PLAIN), true, true);
		ascii18 = new MenaceFontRenderer(Fonts.fontFromTTF(new ResourceLocation("menace/fonts/ascii.ttf"), 18, Font.PLAIN), true, true);
		jetbrainsMono = new MenaceFontRenderer(Fonts.fontFromTTF(new ResourceLocation("menace/fonts/JetBrainsMono-Regular.ttf"), 15, Font.PLAIN), true, true);
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

		irc = new IRCClient("chat.freenode.net", 6667);
		
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

		moduleManager.clickGuiModule.csgoGui = new CSGOGui();
		moduleManager.clickGuiModule.limeGui = new LimeClickGUI();
		moduleManager.clickGuiModule.menaceGui = new MenaceClickGui();

		starting = false;
	}

	public void postinit() {
		new Thread() {
			@Override
			public void run() {
				irc.start();
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
		irc.stop();
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
	public void onReceive(@NotNull EventReceivePacket event) {
		if (event.getPacket() instanceof S02PacketChat) {
			String message = ((S02PacketChat) event.getPacket()).getChatComponent().getUnformattedText();
			if (message != null && message.contains(" was killed by " + MC.thePlayer.getName())) {
				this.hudManager.gameStatsElement.kills++;
			}

			final String[] formattedMessage = {((S02PacketChat) event.getPacket()).getChatComponent().getFormattedText()};
			onlineMenaceUsers.forEach((username, ign) -> {
				if (!Objects.equals(username, Menace.instance.user.getUsername()) && ign != null && formattedMessage[0].contains(ign)) {
					formattedMessage[0] = formattedMessage[0].replace(ign, ign + " §r(§b" + username + "§r) ");
				}
			});

			((S02PacketChat) event.getPacket()).setChatComponent(new ChatComponentText(formattedMessage[0]));

		} else if (event.getPacket() instanceof S45PacketTitle) {
			S45PacketTitle packet = (S45PacketTitle) event.getPacket();
			final String[] formattedMessage = {packet.getMessage().getFormattedText()};

			onlineMenaceUsers.forEach((username, ign) -> {
				if (!Objects.equals(username, Menace.instance.user.getUsername()) && ign != null && formattedMessage[0].contains(ign)) {
					formattedMessage[0] = formattedMessage[0].replace(ign, ign + " §r(§b" + username + "§r) ");
				}
			});

			packet.setMessage(new ChatComponentText(formattedMessage[0]));
		}
	}

	@EventTarget
	public void onWorldChange(EventWorldChange event) {
		onlineMenaceUsers.clear();

		try {
			final URL url = new URL("https://menaceapi.cf/getMenaceUsers/");
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

				JsonObject jsonObject = new JsonParser().parse(response.toString()).getAsJsonObject();
				//System.out.println("Balls" + jsonObject);
				if (jsonObject.has(ServerUtils.getRemoteIp())) {
					JsonObject server = jsonObject.get(ServerUtils.getRemoteIp().toLowerCase()).getAsJsonObject();
					server.entrySet().forEach(entry -> {
						onlineMenaceUsers.put(entry.getKey(), entry.getValue().getAsString());
					});
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public int getClientColor() {
		return Color.RED.getRGB();
	}

	public int getClientColorDarker() {
		return new Color(203, 26, 26).getRGB();
	}
}
