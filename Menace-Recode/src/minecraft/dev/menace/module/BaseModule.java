package dev.menace.module;

import java.io.File;
import java.util.ArrayList;

import com.google.gson.JsonObject;

import dev.menace.Menace;
import dev.menace.module.settings.Setting;
import dev.menace.module.settings.ToggleSetting;
import dev.menace.utils.file.FileManager;
import net.minecraft.client.Minecraft;

public class BaseModule {

	public float optionAnim = 0;
	public float optionAnimNow = 0;
	private final String name;
	private final String desc;
	private String displayName = null;
	private final Category category;
	private int keybind;
	protected boolean toggled;
	private final ToggleSetting visible;
	private final ArrayList<Setting> settings = new ArrayList<>();
	
	protected final Minecraft mc = Minecraft.getMinecraft();

	public BaseModule(String name, Category category, int keybind) {
		this.name = name;
		this.desc = "";
		this.category = category;
		this.toggled = false;
		
		if (!(new File(FileManager.getConfigFolder(), "Binds.json").exists())) {
        	this.keybind = keybind;
        } else {
    		JsonObject bindFile = FileManager.readJsonFromFile(new File(FileManager.getConfigFolder(), "Binds.json"));
			assert bindFile != null;
			if (bindFile.has(name)) {
    			this.keybind = bindFile.get(name).getAsInt();
    		} else {
    			this.keybind = keybind;
    		}
        }
		
		this.setup();
		
		visible = new ToggleSetting("Visible", true, true);
		this.rSetting(visible);
		
		if (this.toggled) {
        	this.onEnable();
        }
	}
	
	public BaseModule(String name, String desc, Category category, int keybind) {
		this.name = name;
		this.desc = desc;
		this.category = category;
		this.toggled = false;
		
		if (!(new File(FileManager.getConfigFolder(), "Binds.json").exists())) {
        	this.keybind = keybind;
        } else {
    		JsonObject bindFile = FileManager.readJsonFromFile(new File(FileManager.getConfigFolder(), "Binds.json"));
			assert bindFile != null;
			if (bindFile.has(name)) {
    			this.keybind = bindFile.get(name).getAsInt();
    		} else {
    			this.keybind = keybind;
    		}
        }
		
		this.setup();
		
		visible = new ToggleSetting("Visible", true, true);
		this.rSetting(visible);
		
		if (this.toggled) {
        	this.onEnable();
        }
	}
	
	public void setup() {}

	public void onEnable() {
		Menace.instance.eventManager.register(this);
	}
	
	public void onDisable() {
		Menace.instance.eventManager.unregister(this);
	}
	
	public int getKeybind() {
		return keybind;
	}

	public void setKeybind(int keybind) {
		this.keybind = keybind;
		Menace.instance.moduleManager.saveKeys();
	}
	
	public void setKeybindNoSave(int keybind) {
		this.keybind = keybind;
	}

	public boolean isToggled() {
		return toggled;
	}

	public void toggle() {
		this.setToggled(!this.toggled);
	}
	
	public void setToggled(boolean toggled) {
		
		if (mc.thePlayer == null && this.getClass().isAnnotationPresent(DontSaveState.class)) {
			this.toggled = false;
			return;
		}
		
		if (this.toggled == toggled) return;
		
		if (this.toggled) {
			this.onDisable();
		} else {
			this.onEnable();
		}
		
		this.toggled = toggled;
	}
	
	public void rSetting(Setting setting) {
		this.settings.add(setting);
	}

	public String getName() {
		return name;
	}

	public String getDesc() {
		return desc;
	}

	public Category getCategory() {
		return category;
	}

	public ArrayList<Setting> getSettings() {
		return settings;
	}

	public boolean hasSettings() {
		return !settings.isEmpty();
	}
	
	public String getDisplayName() {
		return displayName != null ? getName() + " §7- " + displayName : getName();
	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public boolean isVisible() {
		return this.visible.getValue();
	}
	
	public void setVisible(boolean visible) {
		this.visible.setValue(visible);
	}
	
}
