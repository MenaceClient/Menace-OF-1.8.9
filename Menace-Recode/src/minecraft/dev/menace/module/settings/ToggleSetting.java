package dev.menace.module.settings;

public class ToggleSetting extends Setting {

	private boolean toggled;
	
	public ToggleSetting(String name, boolean visible, boolean toggled) {
		super(name, visible);
		this.toggled = toggled;
	}

	public boolean isToggled() {
		return toggled;
	}

	public void setToggled(boolean toggled) {
		this.toggled = toggled;
	}

}
