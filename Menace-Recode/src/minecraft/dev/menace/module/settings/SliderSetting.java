package dev.menace.module.settings;

import dev.menace.scripting.js.JSMapping;
import dev.menace.scripting.js.MappedName;

@JSMapping
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

	@MappedName(116)
	public double getMin() {
		return min;
	}

	@MappedName(117)
	public double getMax() {
		return max;
	}

	@MappedName(118)
	public boolean onlyInt() {
		return intOnly;
	}

	@MappedName(112)
	public double getValue() {
		return value;
	}

	@MappedName(119)
	public float getValueF() {
		return (float) value;
	}

	@MappedName(120)
	public long getValueL() {
		return (long) value;
	}

	@MappedName(121)
	public int getValueI() {
		return (int) value;
	}

	@MappedName(113)
	public void setValue(double value) {
		this.value = value;
	}

	@MappedName(122)
	public double getIncrement() {
		return increment;
	}

}
