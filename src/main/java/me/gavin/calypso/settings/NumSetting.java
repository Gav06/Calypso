package me.gavin.calypso.settings;

import java.util.function.Predicate;

public class NumSetting extends AbstractSetting {

    private float value;
    private final float min;
    private final float max;

    public NumSetting(String name, float value, float min, float max) {
        super(name);
        this.value = value;
        this.min = min;
        this.max = max;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = getClampedValue(value);
    }

    private float getClampedValue(float newValue) {
        return Math.min(max, Math.max(min, newValue));
    }

    public float getMin() {
        return min;
    }

    public float getMax() {
        return max;
    }
}
