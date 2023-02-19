package dev.menace.module.settings;

public class ListSetting extends Setting {

	private final String defaultValue;
	private String value;
	private String[] options;
	
	public ListSetting(String name, boolean visible, String value, String[] options) {
		super(name, visible);
		this.value = value;
		this.defaultValue = value;
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

	public String getDefaultValue() {
		return defaultValue;
	}
}
