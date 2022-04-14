package dev.menace.module;

import java.io.File;
import java.util.ArrayList;

import com.google.gson.annotations.Expose;

import dev.menace.Menace;
import dev.menace.gui.hud.ScreenPosition;
import dev.menace.module.settings.BoolSetting;
import dev.menace.module.settings.DoubleSetting;
import dev.menace.module.settings.Setting;
import dev.menace.module.settings.StringSetting;
import dev.menace.utils.misc.file.FileManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.src.Config;
import net.minecraft.util.ResourceLocation;

public class Module {
	
    protected Minecraft MC = Minecraft.getMinecraft();
    protected Menace menace = Menace.instance;
    private String name, displayName;
    private int key;
    private Category category;
    private boolean toggled;
    private boolean isVisible;
    
    public ModuleSave moduleSave;
    
    public ArrayList<Setting> settings;

    public Module(String name, int key, Category category) {
        this.name = name;
        this.key = key;
        this.category = category;
        this.toggled = false;
        this.moduleSave = loadModuleFromFile();
        this.settings = new ArrayList<Setting>();
        this.isVisible = true;

        setup();
        
        if (Menace.instance.isFirstLaunch) {
        	this.settingsSave();
        } else {
        	this.settingsLoad();
        	this.settingsSave();
        }
        
        if (this.toggled) {
        	this.onEnable();
        }
        
    }

    public void onEnable() {
    	
    	if(!Menace.instance.initializing && Menace.instance.moduleManager.clickGuiModule.sound.isChecked()) {
			MC.thePlayer.playSound("random.click", 0.5f, 0.5f);
    	}
        Menace.instance.eventManager.register(this);
    }
    public void onDisable() {
    	if(!Menace.instance.initializing && Menace.instance.moduleManager.clickGuiModule.sound.isChecked()) {
			MC.thePlayer.playSound("random.click", 0.5f, 0.5f);
    	}
        Menace.instance.eventManager.unregister(this);
    }
    public void onToggle() {this.save(new ModuleSave(this.key, this.toggled, this.isVisible));}
    public void toggle() {
        toggled = !toggled;
        onToggle();
        if(toggled)
            onEnable();
        else
            onDisable();
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getKey() {
        return key;
    }
    public void setKey(int key) {
        this.key = key;
        this.save(new ModuleSave(this.key, this.toggled, this.isVisible));
    }
    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }
    public boolean isToggled() {
        return toggled;
    }
    public String getDisplayName() {
        return displayName == null ? name : displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public boolean isVisible() {
    	return this.isVisible;
    }
    public void setVisible(boolean visible) {
    	this.isVisible = visible;
    	this.save(new ModuleSave(this.key, this.toggled, this.isVisible));
    }
    public void setup() {}
    
    public void rSetting(Setting setting) {
    	settings.add(setting);
    }
    
	protected File getBaseFolder() {
		File folder = new File(FileManager.getModuleFolder(), this.getClass().getSimpleName());
		folder.mkdirs();
		return folder;
	}
	
	protected void saveModuleToFile() {
		FileManager.writeJsonToFile(new File(getBaseFolder(), this.getClass().getSimpleName() + ".json"), moduleSave);
	}

	protected ModuleSave loadModuleFromFile() {

		ModuleSave loaded = FileManager.readFromJson(new File(getBaseFolder(), this.getClass().getSimpleName() + ".json"), ModuleSave.class);
		
		if (loaded == null) {
			loaded = new ModuleSave(key, false, true);
			this.moduleSave = loaded;
			saveModuleToFile();
		}
		
		this.key = loaded.key;
		this.toggled = loaded.toggled;
		this.isVisible = loaded.isVisible;
		
		return loaded;
		
	}
	
	public void save(ModuleSave save) {
		this.moduleSave = save;
		saveModuleToFile();
	}
	
	public ModuleSave load() {
		return this.moduleSave;
	}
	
	public void settingsSave() {
		for (Setting s : this.settings) {
			if (s instanceof BoolSetting) {
				BoolSetting set = (BoolSetting) s;
				BoolSettingSave save = new BoolSettingSave(set.isChecked());
				
				FileManager.writeJsonToFile(new File(getBaseFolder(), set.getName() + ".json"), save);
				
			} else if (s instanceof DoubleSetting) {
				DoubleSetting set = (DoubleSetting) s;
				DoubleSettingSave save = new DoubleSettingSave(set.getValue());
				
				FileManager.writeJsonToFile(new File(getBaseFolder(), set.getName() + ".json"), save);
				
			} else if (s instanceof StringSetting) {
				StringSetting set = (StringSetting) s;
				StringSettingSave save = new StringSettingSave(set.getString());
				
				FileManager.writeJsonToFile(new File(getBaseFolder(), set.getName() + ".json"), save);
				
			}
		}
	}
	
	public void settingsLoad() {
		for (Setting s : this.settings) {
			
			if (!(new File(getBaseFolder(), s.getName() + ".json").exists())) {
				continue;
			}
			
			if (s instanceof BoolSetting) {
				BoolSetting set = (BoolSetting) s;
				BoolSettingSave save = FileManager.readFromJson(new File(getBaseFolder(), set.getName() + ".json"), BoolSettingSave.class);
				set.setChecked(save.toggled);
				
			} else if (s instanceof DoubleSetting) {
				DoubleSetting set = (DoubleSetting) s;
				DoubleSettingSave save = FileManager.readFromJson(new File(getBaseFolder(), set.getName() + ".json"), DoubleSettingSave.class);
				set.setValue(save.value);
				
			} else if (s instanceof StringSetting) {
				StringSetting set = (StringSetting) s;
				StringSettingSave save = FileManager.readFromJson(new File(getBaseFolder(), set.getName() + ".json"), StringSettingSave.class);
				set.setString(save.value);
			}
		}
	}
	
	public class ModuleSave {
		int key;
		boolean toggled;
		boolean isVisible;
		public ModuleSave(int key, boolean toggled, boolean isVisible) {
			this.key = key;
			this.toggled = toggled;
			this.isVisible = isVisible;
		}
	}
	
	public class BoolSettingSave {
		boolean toggled;
		public BoolSettingSave(boolean toggled) {
			this.toggled = toggled;
		}
	}
	
	public class DoubleSettingSave {
		double value;
		public DoubleSettingSave(double value) {
			this.value = value;
		}
	}
	
	public class StringSettingSave {
		String value;
		public StringSettingSave(String value) {
			this.value = value;
		}
	}
	
}
