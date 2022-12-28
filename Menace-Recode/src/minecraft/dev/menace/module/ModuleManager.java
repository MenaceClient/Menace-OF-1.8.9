package dev.menace.module;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;

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

	public static ArrayList<BaseModule> modules = new ArrayList<>();

	//COMBAT
	AutoPotModule autoPotModule = new AutoPotModule();
	ComboOneTapModule comboOneTapModule = new ComboOneTapModule();
	CriticalsModule criticalsModule = new CriticalsModule();
	public KillAuraModule killAuraModule = new KillAuraModule();
	//TPAuraModule tpAuraModule = new TPAuraModule();
	public VelocityModule velocityModule = new VelocityModule();
	
	//MOVEMENT
	AirHopModule airHopModule = new AirHopModule();
	public FlightModule flightModule = new FlightModule();
	LongJumpModule longJumpModule = new LongJumpModule();
	public SpeedModule speedModule = new SpeedModule();
	public SprintModule sprintModule = new SprintModule();
	StepModule stepModule = new StepModule();
	StrafeModule strafeModule = new StrafeModule();
	TargetStrafeModule targetStrafeModule = new TargetStrafeModule();
	
	//PLAYER
	AntiVoidModule antiVoidModule = new AntiVoidModule();
	public BlinkModule blinkModule = new BlinkModule();
	public InvManagerModule invManagerModule = new InvManagerModule();
	InvMoveModule invMoveModule = new InvMoveModule();
	NoFallModule noFallModule = new NoFallModule();
	NoSlowModule noSlowModule = new NoSlowModule();
	public SafeWalkModule safeWalkModule = new SafeWalkModule();
	public ScaffoldModule scaffoldModule = new ScaffoldModule();
	
	//WORLD
	AntiCactusModule antiCactusModule = new AntiCactusModule();
	public ChestStealerModule chestStealerModule = new ChestStealerModule();
	FastPlaceModule fastPlaceModule = new FastPlaceModule();
	TimerModule timerModule = new TimerModule();
	
	//RENDER
	public AnimationsModule animationsModule = new AnimationsModule();
	CapeModule capeModule = new CapeModule();
	public ClickGuiModule clickGuiModule = new ClickGuiModule();
	ESPModule espModule = new ESPModule();
	FullbrightModule fullbrightModule = new FullbrightModule();
	public HUDModule hudModule = new HUDModule();
	public ItemPhysicsModule itemPhysicsModule = new ItemPhysicsModule();
	TimeChangerModule timeChangerModule = new TimeChangerModule();
	public XRayModule xrayModule = new XRayModule();
	
	//MISC
	AutoLoginModule autoLoginModule = new AutoLoginModule();
	public AutoPlayModule autoPlayModule = new AutoPlayModule();
	DevModule devModule = new DevModule();
	DisablerModule disablerModule = new DisablerModule();
	public KillSultsModule killSultsModule = new KillSultsModule();
	public SecurityFeaturesModule securityFeaturesModule = new SecurityFeaturesModule();
	StaffDetectorModule staffDetectorModule = new StaffDetectorModule();

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
		ArrayList<BaseModule> list = new ArrayList<>();
		modules.forEach(module ->{
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
						settingSave.addProperty(setting.getName(), ((ToggleSetting)setting).getValue());
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
							((ToggleSetting)setting).setValue(settingSave.get(setting.getName()).getAsBoolean());
						} else if (setting instanceof SliderSetting) {
							((SliderSetting)setting).setValue(settingSave.get(setting.getName()).getAsDouble());
						} else if (setting instanceof ListSetting) {
							((ListSetting)setting).setValue(settingSave.get(setting.getName()).getAsString());
						}
					}
				});
			}
		});
	}

	public void saveKeys() {
		JsonObject bindFile = new JsonObject();
		this.modules.forEach(module -> bindFile.addProperty(module.getName(), module.getKeybind()));
		FileManager.writeJsonToFile(new File(FileManager.getConfigFolder(), "Binds.json"), bindFile);
	}

}