package dev.menace.module.settings;

public class ToggleSetting extends Setting {

	private boolean toggled;
	
	public ToggleSetting(String name, boolean visible, boolean toggled) {
		super(name, visible);
		this.toggled = toggled;
	}

	public boolean getValue() {
		return toggled;
	}

	public void setValue(boolean toggled) {
		this.toggled = toggled;
	}

}
