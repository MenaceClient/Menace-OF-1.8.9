package dev.menace.module;

import java.io.File;
import java.util.ArrayList;

import com.google.gson.JsonObject;

import dev.menace.Menace;
import dev.menace.module.settings.Setting;
import dev.menace.utils.file.FileManager;
import net.minecraft.client.Minecraft;

public class BaseModule {

	private String name;
	private String desc;
	private Category category;
	private int keybind;
	private boolean toggled;
	private ArrayList<Setting> settings = new ArrayList<>();
	
	protected final Minecraft MC = Minecraft.getMinecraft();
	
	public BaseModule(String name, Category category, int keybind) {
		this.name = name;
		this.desc = "";
		this.category = category;
		this.toggled = false;
		
		if (!(new File(FileManager.getConfigFolder(), "Binds.json").exists())) {
        	this.keybind = keybind;
        } else {
    		JsonObject bindFile = FileManager.readJsonFromFile(new File(FileManager.getConfigFolder(), "Binds.json"));
    		if (bindFile.has(name)) {
    			this.keybind = bindFile.get(name).getAsInt();
    		} else {
    			this.keybind = keybind;
    		}
        }
		
		this.setup();
		
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
    		if (bindFile.has(name)) {
    			this.keybind = bindFile.get(name).getAsInt();
    		} else {
    			this.keybind = keybind;
    		}
        }
		
		this.setup();
		
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

	public boolean isToggled() {
		return toggled;
	}

	public void toggle() {
		this.setToggled(!this.toggled);
	}
	
	public void setToggled(boolean toggled) {
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
	
}
