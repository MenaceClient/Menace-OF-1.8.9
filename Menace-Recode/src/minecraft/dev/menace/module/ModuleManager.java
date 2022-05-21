package dev.menace.module;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;

import com.google.gson.JsonObject;

import dev.menace.module.modules.combat.*;
import dev.menace.module.modules.misc.*;
import dev.menace.module.modules.movement.*;
import dev.menace.module.modules.player.*;
import dev.menace.module.modules.render.*;
import dev.menace.module.modules.world.*;
import dev.menace.module.settings.ListSetting;
import dev.menace.module.settings.SliderSetting;
import dev.menace.module.settings.ToggleSetting;
import dev.menace.utils.file.FileManager;
import net.minecraft.crash.CrashReport;

public class ModuleManager {

	public ArrayList<BaseModule> modules = new ArrayList<BaseModule>();
	public String selectedConfig = "default";

	//COMBAT
	VelocityModule velocityModule = new VelocityModule();
	
	//MOVEMENT
	public FlightModule flightModule = new FlightModule();
	SprintModule sprintModule = new SprintModule();
	
	//PLAYER
	NoFallModule noFallModule = new NoFallModule();
	
	//WORLD
	FastPlaceModule fastPlaceModule = new FastPlaceModule();
	
	//RENDER
	public ClickGuiModule clickGuiModule = new ClickGuiModule();
	
	//MISC
	DevModule devModule = new DevModule();

	public ModuleManager() {
		try
		{
			for(Field field : ModuleManager.class.getDeclaredFields())
			{
				if(!field.getName().endsWith("Module"))
					continue;

				BaseModule module = (BaseModule)field.get(this);
				modules.add(module);
			}

		}catch(Exception e)
		{
			String message = "Initializing Menace modules";
			CrashReport report = CrashReport.makeCrashReport(e, message);
		}
	}

	public ArrayList<BaseModule> getModules() {
		return modules;
	}

	public ArrayList<BaseModule> getActiveModules() {
		ArrayList<BaseModule> activeModules = new ArrayList<BaseModule>();

		for (BaseModule m : modules) {
			if (m.isToggled()) activeModules.add(m);
		}

		return activeModules;
	}
	
	public BaseModule getModuleByName(String name) {
		return modules.stream().filter(module -> module.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
	}

	public ArrayList<BaseModule> getModulesByCategory(Category c) {
		ArrayList<BaseModule> list = new ArrayList<BaseModule>();
		modules.stream().forEach(module ->{ 
			if (module.getCategory().equals(c)) {
				list.add(module);
			}
		});
		return list;
	}
	
	public void saveModules(String configName) {
		JsonObject configFile = new JsonObject();
		this.modules.forEach(module -> {
			JsonObject modSave = new JsonObject();

			modSave.addProperty("Toggled", module.isToggled());

			if (!module.getSettings().isEmpty()) {
				JsonObject settingSave = new JsonObject();
				module.getSettings().forEach(setting -> {

					if (setting instanceof ToggleSetting) {
						settingSave.addProperty(setting.getName(), ((ToggleSetting)setting).isToggled());
					} else if (setting instanceof SliderSetting) {
						settingSave.addProperty(setting.getName(), ((SliderSetting)setting).getValue());
					} else if (setting instanceof ListSetting) {
						settingSave.addProperty(setting.getName(), ((ListSetting)setting).getValue());
					}

				});
				modSave.add("Settings", settingSave);
			}

			configFile.add(module.getName(), modSave);
		});
		FileManager.writeJsonToFile(new File(FileManager.getConfigFolder(), configName + ".json"), configFile);
		this.selectedConfig = configName;
	}

	public void loadModules(String configName) {
		JsonObject configFile = FileManager.readJsonFromFile(new File(FileManager.getConfigFolder(), configName + ".json"));
		this.modules.stream().filter(mod -> configFile.has(mod.getName())).forEach(module -> {
			JsonObject modSave = configFile.get(module.getName()).getAsJsonObject();
			boolean toggled = modSave.get("Toggled").getAsBoolean();
			if ((toggled && !module.isToggled()) || (!toggled && module.isToggled())) {
				module.toggle();
			}
			if (!module.getSettings().isEmpty() && modSave.has("Settings")) {
				JsonObject settingSave = modSave.get("Settings").getAsJsonObject();
				module.getSettings().forEach(setting -> {
					if (settingSave.has(setting.getName())) {
						if (setting instanceof ToggleSetting) {
							((ToggleSetting)setting).setToggled(settingSave.get(setting.getName()).getAsBoolean());
						} else if (setting instanceof SliderSetting) {
							((SliderSetting)setting).setValue(settingSave.get(setting.getName()).getAsDouble());
						} else if (setting instanceof ListSetting) {
							((ListSetting)setting).setValue(settingSave.get(setting.getName()).getAsString());
						}
					}
				});
			}
		});
		this.selectedConfig = configName;
	}

	public void saveKeys() {
		JsonObject bindFile = new JsonObject();
		this.modules.forEach(module -> {
			bindFile.addProperty(module.getName(), module.getKeybind());
		});
		FileManager.writeJsonToFile(new File(FileManager.getConfigFolder(), "Binds.json"), bindFile);
	}

}