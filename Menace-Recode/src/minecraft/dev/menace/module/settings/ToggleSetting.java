package dev.menace.module.settings;

import dev.menace.scripting.js.JSMapping;
import dev.menace.scripting.js.MappedName;

@JSMapping
public class ToggleSetting extends Setting {

	private boolean toggled;
	
	public ToggleSetting(String name, boolean visible, boolean toggled) {
		super(name, visible);
		this.toggled = toggled;
	}

	@MappedName(112)
	public boolean getValue() {
		return toggled;
	}

	@MappedName(113)
	public void setValue(boolean toggled) {
		this.toggled = toggled;
	}

}
