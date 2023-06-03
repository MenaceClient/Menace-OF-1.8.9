package dev.menace.module;

import java.io.File;
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

public class ModuleManager {

	public static ArrayList<BaseModule> modules = new ArrayList<>();

	//COMBAT
	public AntiBotModule antiBotModule = new AntiBotModule();
	AutoPotModule autoPotModule = new AutoPotModule();
	public BackTrackerModule backTrackerModule = new BackTrackerModule();
	CriticalsModule criticalsModule = new CriticalsModule();
	public KillAuraModule killAuraModule = new KillAuraModule();
	public TPAuraModule tpAuraModule = new TPAuraModule();
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
	JesusModule jesusModule = new JesusModule();
	NoFallModule noFallModule = new NoFallModule();
	NoSlowModule noSlowModule = new NoSlowModule();
	public SafeWalkModule safeWalkModule = new SafeWalkModule();
	public ScaffoldModule scaffoldModule = new ScaffoldModule();
	
	//WORLD
	AntiCactusModule antiCactusModule = new AntiCactusModule();
	BedNukerModule bedNukerModule = new BedNukerModule();
	public ChestStealerModule chestStealerModule = new ChestStealerModule();
	FastPlaceModule fastPlaceModule = new FastPlaceModule();
	PhaseModule phaseModule = new PhaseModule();
	TimerModule timerModule = new TimerModule();
	
	//RENDER
	public AnimationsModule animationsModule = new AnimationsModule();
	CapeModule capeModule = new CapeModule();
	ChestESPModule chestESPModule = new ChestESPModule();
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
	public DevModule devModule = new DevModule();
	public DisablerModule disablerModule = new DisablerModule();
	ExitFlagModule exitFlagModule = new ExitFlagModule();
	public KillFXModule killFXModule = new KillFXModule();
	MCFModule mcfModule = new MCFModule();
	public HackerDetectModule hackerDetectModule = new HackerDetectModule();
	public SecurityFeaturesModule securityFeaturesModule = new SecurityFeaturesModule();
	SpooferModule spooferModule = new SpooferModule();
	StaffDetectorModule staffDetectorModule = new StaffDetectorModule();
	TranslatorModule translatorModule = new TranslatorModule();

	public ArrayList<BaseModule> getModules() {
		return modules;
	}

	public void removeModule(BaseModule module) {
		modules.remove(module);
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
		modules.stream().filter(mod -> configFile.has(mod.getName())).forEach(module -> {
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
							//((SliderSetting)setting).setValue(Math.min(settingSave.get(setting.getName()).getAsDouble(), ((SliderSetting)setting).getMax()));
							((SliderSetting)setting).setValue(settingSave.get(setting.getName()).getAsDouble());
						} else if (setting instanceof ListSetting) {
							boolean found = false;
							for (String s : ((ListSetting)setting).getOptions()) {
								if (s.equalsIgnoreCase(settingSave.get(setting.getName()).getAsString()) && !found) {
									((ListSetting)setting).setValue(s);
									found = true;
								}
							}

							if (!found) {
								((ListSetting)setting).setValue(((ListSetting)setting).getDefaultValue());
							}

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