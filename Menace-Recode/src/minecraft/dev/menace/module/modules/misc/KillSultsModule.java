package dev.menace.module.modules.misc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventDeath;
import dev.menace.event.events.EventReceivePacket;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.utils.file.FileManager;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.misc.MathUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S02PacketChat;
import org.jetbrains.annotations.NotNull;

public class KillSultsModule extends BaseModule {

	File insultFile;
	ArrayList<String> insults;

	public KillSultsModule() {
		super("KillSults", Category.MISC, 0);
	}

	@Override
	public void setup() {
		insultFile = new File(FileManager.getMenaceFolder(), "Insults.txt");
		insults = new ArrayList<>();
		if (!insultFile.exists()) {
			try {
				insultFile.createNewFile();
			} catch (IOException e2) {
				e2.printStackTrace();
			}

			try {
				final FileWriter writer = new FileWriter(insultFile);
				readInsults().forEach(s -> {
					try {
						writer.write(s + "\n");
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
				writer.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		reload();
	}

	@Override
	public void onEnable() {
		reload();
		super.onEnable();
	}

	public void reload() {
		try {

			if (!insultFile.exists()) {
				insultFile.createNewFile();

				final FileWriter writer = new FileWriter(insultFile);
				readInsults().forEach(s -> {
					try {
						writer.write(s + "\n");
					} catch (IOException e) {
						e.printStackTrace();
					}
				});

				writer.close();
			}

			FileInputStream inputStream = new FileInputStream(insultFile);
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String line;

			while ((line = bufferedReader.readLine()) != null) {
				insults.add(line);
			}

			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@EventTarget
	public void onRecieve(EventReceivePacket event) {
		if (event.getPacket() instanceof S02PacketChat) {
			String message = ((S02PacketChat) event.getPacket()).getChatComponent().getUnformattedText();
			if (message.contains(" was killed by " + mc.thePlayer.getName())) {
				insult(message.split(" ")[0]);
			}
		}
	}

	public void insult(String name) {
		if (!this.isToggled()) return;
		String insult = "";
		insult = insults.get(MathUtils.randInt(0, insults.size()));
		insult = insult.replaceAll("<player>", name);
		insult = insult.replaceAll("<discord>", Menace.instance.user.getDiscord());
		insult = insult.replaceAll("<username>", Menace.instance.user.getUsername());
		insult = insult.replaceAll("<hwid>", Menace.instance.user.getHwid());
		insult = insult.replaceAll("<uid>", String.valueOf(Menace.instance.user.getUID()));
		if (insult.isEmpty()) insult = "L";

		List <String> ascii = new ArrayList<>(26);

		for (char c = 'A'; c <= 'Z'; c++) {
			ascii.add (String.valueOf (c));
		}

		Random rand = new Random();
		String random = ascii.get(rand.nextInt(ascii.size()));
		random = random +  ascii.get(rand.nextInt(ascii.size()));
		random = random +  ascii.get(rand.nextInt(ascii.size()));
		random = random +  ascii.get(rand.nextInt(ascii.size()));
		random = random +  ascii.get(rand.nextInt(ascii.size()));

		mc.thePlayer.sendChatMessage(insult + " [" + random + "]");
	}

	public static @NotNull List<String> readInsults() {
		List<String> s = new ArrayList<>();
		try {
			final URL url = new URL("https://menaceapi.cf/assets/876940587946589786/Insults.txt");
            URLConnection uc = url.openConnection();
            uc.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(uc.getInputStream(), StandardCharsets.UTF_8));
			String insult;
			while ((insult = bufferedReader.readLine()) != null) {
				s.add(insult);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

}
