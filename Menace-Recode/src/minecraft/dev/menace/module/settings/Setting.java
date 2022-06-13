package dev.menace.module.settings;

public class Setting {

	private final String name;
	private boolean visible;
	
	public Setting(String name, boolean visible) {
		this.name = name;
		this.visible = visible;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public String getName() {
		return name;
	}
	
	public boolean isToggle() {
		return this instanceof ToggleSetting;
	}
	
	public boolean isSlider() {
		return this instanceof SliderSetting;
	}
	
	public boolean isList() {
		return this instanceof ListSetting;
	}

	public void constantCheck() {
		
	}
	
}
