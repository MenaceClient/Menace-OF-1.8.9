package dev.menace.ui.hud;

import com.google.gson.JsonObject;
import dev.menace.scripting.ScriptElement;
import dev.menace.ui.hud.elements.*;
import dev.menace.utils.file.FileManager;

import java.io.File;
import java.util.ArrayList;

public class HUDManager {

	public static ArrayList<BaseElement> hudElements = new ArrayList<>();

	//Elements
	public ArmourElement armourElement = new ArmourElement();
	public ArrayElement arrayElement = new ArrayElement();
	public GameStatsElement gameStatsElement = new GameStatsElement();
	public NotificationElement notificationElement = new NotificationElement();
	public PingElement pingElement = new PingElement();
	public PosElement posElement = new PosElement();
	public ScoreboardElement scoreboardElement = new ScoreboardElement();
	public TabGuiElement tabGuiElement = new TabGuiElement();
	public TargetHudElement targetHudElement = new TargetHudElement();
	public WatermarkElement watermarkElement = new WatermarkElement();

	public static void save() {
		JsonObject hudFile = new JsonObject();
		hudElements.forEach(element -> {
			JsonObject elementFile = new JsonObject();
			elementFile.addProperty("X", element.getAbsoluteX());
			elementFile.addProperty("Y", element.getAbsoluteY());
			elementFile.addProperty("Visible", element.isVisible());

			if (element instanceof ScriptElement) {
				hudFile.add(((ScriptElement)element).getElementMap().getName(), elementFile);
			} else {
				hudFile.add(element.getClass().getSimpleName(), elementFile);
			}
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
		hudElements.stream().filter(e -> hudFile.has(e.getClass().getSimpleName())
				|| (e instanceof ScriptElement && hudFile.has(((ScriptElement)e).getElementMap().getName()))).forEach(element -> {

			JsonObject elementSave;

			if (element instanceof ScriptElement) {
				elementSave = hudFile.get(((ScriptElement)element).getElementMap().getName()).getAsJsonObject();
			} else {
				elementSave = hudFile.get(element.getClass().getSimpleName()).getAsJsonObject();
			}

			element.setAbsolute(elementSave.get("X").getAsInt(), elementSave.get("Y").getAsInt());
			element.setVisible(elementSave.get("Visible").getAsBoolean());
		});
	}

}
