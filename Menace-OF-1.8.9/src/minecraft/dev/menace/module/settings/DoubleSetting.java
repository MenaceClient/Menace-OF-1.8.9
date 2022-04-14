package dev.menace.module.settings;

import dev.menace.module.Module;

public class DoubleSetting extends Setting {

	private double value, min, max;
	
	public DoubleSetting(String name, Module parent, double def, double min, double max) {
		
		this.name = name;
		this.parent = parent;
		this.setValue(def);
		this.min = min;
		this.max = max;
		
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public double getMin() {
		return min;
	}

	public double getMax() {
		return max;
	}
	
}
