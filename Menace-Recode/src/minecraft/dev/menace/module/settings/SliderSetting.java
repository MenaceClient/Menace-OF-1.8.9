package dev.menace.module.settings;

public class SliderSetting extends Setting {

	private double value, min, max, increment;
	private boolean intOnly;
	
	public SliderSetting(String name, boolean visible, double value, double min, double max, boolean intOnly) {
		super(name, visible);
		this.value = value;
		this.min = min;
		this.max = max;
		this.intOnly = intOnly;
		this.increment = 1;
	}
	
	public SliderSetting(String name, boolean visible, double value, double min, double max, double increment, boolean intOnly) {
		super(name, visible);
		this.value = value;
		this.min = min;
		this.max = max;
		this.intOnly = intOnly;
		this.increment = increment;
	}

	public double getMin() {
		return min;
	}
	
	public double getMax() {
		return max;
	}

	public boolean onlyInt() {
		return intOnly;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public double getIncrement() {
		return increment;
	}

}
