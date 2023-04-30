package dev.menace.module.settings;

import dev.menace.scripting.js.JSMapping;
import dev.menace.scripting.js.MappedName;

@JSMapping
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

	@MappedName(112)
	public String getValue() {
		return value;
	}

	@MappedName(113)
	public void setValue(String value) {
		this.value = value;
	}

	@MappedName(114)
	public String[] getOptions() {
		return options;
	}

	@MappedName(115)
	public String getDefaultValue() {
		return defaultValue;
	}
}
