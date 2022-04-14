package dev.menace.module.settings;

import java.util.ArrayList;

import dev.menace.module.Module;

public class StringSetting extends Setting {
	
	private String value;
	ArrayList<String> options;
	
	public StringSetting(String name, Module parent, String def, ArrayList<String> options) {
		
		this.name = name;
		this.parent = parent;
		this.value = def;
		this.options = options;
		
	}

	public String getString() {
		return value;
	}

	public void setString(String def) {
		this.value = def;
	}

	public ArrayList<String> getOptions() {
		return options;
	}

}
