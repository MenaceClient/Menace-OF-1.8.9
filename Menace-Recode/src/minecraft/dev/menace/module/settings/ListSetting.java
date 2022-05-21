package dev.menace.module.settings;

public class ListSetting extends Setting {

	private String value;
	private String[] options;
	
	public ListSetting(String name, boolean visible, String value, String[] options) {
		super(name, visible);
		this.value = value;
		this.options = options;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String[] getOptions() {
		return options;
	}

}
