package dev.menace.module.modules.misc;

import java.awt.*;
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
import dev.menace.event.events.EventRender2D;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.settings.ListSetting;
import dev.menace.module.settings.ToggleSetting;
import dev.menace.utils.file.FileManager;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.misc.MathUtils;
import dev.menace.utils.misc.SoundPlayer;
import dev.menace.utils.render.RenderUtils;
import dev.menace.utils.timer.MSTimer;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class KillFXModule extends BaseModule {

	File insultFile;
	ArrayList<String> insults;
	MSTimer killTimer = new MSTimer();
	int killcount = 1;

	ToggleSetting insultsSet;
	ToggleSetting valOverlaySet;
	ListSetting valTypeSet;

	public KillFXModule() {
		super("KillFX", Category.MISC, 0);
	}

	@Override
	public void setup() {
		insultsSet = new ToggleSetting("Insults", true, true);
		valOverlaySet = new ToggleSetting("Valorant", true, true);
		valTypeSet = new ListSetting("Type", true, "Basic", new String[] {"Basic", "Spectrum"});
		this.rSetting(insultsSet);
		this.rSetting(valOverlaySet);
		this.rSetting(valTypeSet);

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
		killTimer.reset();
		killcount = 1;
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

	public void onKill(String name) {
		if (!this.isToggled()) return;
		if (insultsSet.getValue()) insult(name);
		if (valOverlaySet.getValue()) valOverlay();
	}

	public void insult(String name) {
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

	public void valOverlay() {
		if (killTimer.hasTimePassed(15000)) {
			killTimer.reset();
			killcount = 1;
		}

		if (killcount < 5) {
			SoundPlayer.playSound(new ResourceLocation("menace/sound/kill_" + killcount + "_" + valTypeSet.getValue().toLowerCase() + ".wav"));
			killcount++;
			killTimer.reset();
		} else if (valTypeSet.getValue().equalsIgnoreCase("Spectrum") && killcount > 5) {
			SoundPlayer.playSound(new ResourceLocation("menace/sound/kill_overdrive_spectrum.wav"));
			killcount++;
			killTimer.reset();
		} else {
			SoundPlayer.playSound(new ResourceLocation("menace/sound/kill_5_" + valTypeSet.getValue().toLowerCase() + ".wav"));
			killcount++;
			killTimer.reset();
		}
	}

	@EventTarget
	public void onRender2D(EventRender2D event) {
		if (!valOverlaySet.getValue()) return;

		if (!killTimer.hasTimePassed(1000)) {
			RenderUtils.drawImage(430, 355, 100, 100, new ResourceLocation("menace/killfx/" + valTypeSet.getValue().toLowerCase() + "_kill_banner.png"), new Color(255, 255, 255, 0));
		}

		//Render kill timer progress bar starting from the max time and going down to 0
		//new Color(87,183,171,255).getRGB()
		float progress = (float) killTimer.timePassed() / 15000;
		progress = 1 - progress;
		if (progress > 0) {
			RenderUtils.drawHollowRect(390, 460, 570, 470, 1, -1);
			RenderUtils.drawRect(391, 461, 391 + (180 * progress), 469, new Color(87, 183, 171, 255).getRGB());
		}
	}

	public static List<String> readInsults() {
		List<String> s = new ArrayList<>();
		try {
			final URL url = new URL("https://menaceapi.cf/Insults.txt");
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
