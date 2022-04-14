package dev.menace.module.settings;

import dev.menace.module.Module;

public class BoolSetting extends Setting {

	private boolean checked;
	
	public BoolSetting(String name, Module parent, boolean checked) {
		
		this.name = name;
		this.parent = parent;
		this.setChecked(checked);
		
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
}
