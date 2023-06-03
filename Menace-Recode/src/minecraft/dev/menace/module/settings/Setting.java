package dev.menace.module.settings;

public class Setting {

	private DividerSetting parent;
	public float optionAnim = 0;
	public float optionAnimNow = 0;
	private final String name;
	private boolean visible;
	
	public Setting(String name, boolean visible) {
		this.name = name;
		this.visible = visible;
	}

	public Setting(String name, boolean visible, DividerSetting parent) {
		this.name = name;
		this.visible = visible;
		this.parent = parent;
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
	public DividerSetting getParent() {
		return parent;
	}

	public void constantCheck() {
		
	}
	
}
