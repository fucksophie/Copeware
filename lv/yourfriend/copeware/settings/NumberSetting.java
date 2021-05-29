package lv.yourfriend.copeware.settings;

public class NumberSetting extends Setting {
	public double value, minimum, maximum, increment;

	public NumberSetting(String name, double value, double minium, double maximum, double increment) {
		this.name = name;
		this.value = value;
		this.minimum = minium;
		this.maximum = maximum;
		this.increment = increment;
		// "Number", now, nim, max, howmanyfor
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		double precision = 1 / increment;
		this.value = Math.round(Math.max(minimum, Math.min(maximum, value)) * precision) / precision;
	}
	
	public void increment(boolean pos) {
		setValue(getValue() + (pos ? 1 : -1) * increment);
	}
	public double getMinium() {
		return minimum;
	}

	public void setMinium(double minium) {
		this.minimum = minium;
	}

	public double getMaximum() {
		return maximum;
	}

	public void setMaximum(double maximum) {
		this.maximum = maximum;
	}

	public double getIncrement() {
		return increment;
	}

	public void setIncrement(double increment) {
		this.increment = increment;
	}
	
}
