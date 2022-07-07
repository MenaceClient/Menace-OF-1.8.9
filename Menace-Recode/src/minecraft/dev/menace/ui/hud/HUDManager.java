package dev.menace.ui.hud;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;

import com.google.gson.JsonObject;

import dev.menace.ui.hud.elements.*;
import dev.menace.utils.file.FileManager;
import dev.menace.utils.render.MenaceFontRenderer;
import net.minecraft.crash.CrashReport;

public class HUDManager {

	public static ArrayList<BaseElement> hudElements = new ArrayList<>();

	//Elements
	public ArrayElement arrayElement = new ArrayElement();
	public GameStatsElement gameStatsElement = new GameStatsElement();
	//public NotificationElement notificationElement = new NotificationElement();
	public PosElement posElement = new PosElement();
	//public TargetHudElement targetHudElement = new TargetHudElement();
	public WatermarkElement watermarkElement = new WatermarkElement();

	public HUDManager() {
		try
		{
			for(Field field : HUDManager.class.getDeclaredFields())
			{
				if(!field.getName().endsWith("Element"))
					continue;

				BaseElement module = (BaseElement)field.get(this);
				hudElements.add(module);
			}

		}catch(Exception e)
		{
			String message = "Initializing Menace Hud";
			CrashReport report = CrashReport.makeCrashReport(e, message);
		}
	}

	public static void save() {
		JsonObject hudFile = new JsonObject();
		hudFile.addProperty("Font", BaseElement.font);
		hudElements.forEach(element -> {
			JsonObject elementFile = new JsonObject();
			elementFile.addProperty("X", element.getAbsoluteX());
			elementFile.addProperty("Y", element.getAbsoluteY());
			elementFile.addProperty("Visible", element.isVisible());
			hudFile.add(element.getClass().getSimpleName(), elementFile);
		});
		FileManager.writeJsonToFile(new File(FileManager.getHudFolder(), "Hud.json"), hudFile);
	}

	public static void load() {

		if (!(new File(FileManager.getHudFolder(), "Hud.json")).exists()) {
			save();
			return;
		}

		JsonObject hudFile = FileManager.readJsonFromFile(new File(FileManager.getHudFolder(), "Hud.json"));
		assert hudFile != null;
		if (!hudFile.get("Font").getAsString().equalsIgnoreCase(BaseElement.font)) {
			BaseElement.font = hudFile.get("Font").getAsString();
			BaseElement.fr = MenaceFontRenderer.getFontOnPC(BaseElement.font, 20);
		}
		
		hudElements.stream().filter(e -> hudFile.has(e.getClass().getSimpleName())).forEach(element -> {
			JsonObject elementSave = hudFile.get(element.getClass().getSimpleName()).getAsJsonObject();

			element.setAbsolute(elementSave.get("X").getAsInt(), elementSave.get("Y").getAsInt());
			element.setVisible(elementSave.get("Visible").getAsBoolean());
		});
	}

}
