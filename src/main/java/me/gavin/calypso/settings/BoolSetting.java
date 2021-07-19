package me.gavin.calypso.settings;

public class BoolSetting extends AbstractSetting {

    private boolean value;

    public BoolSetting(String name, boolean value) {
        super(name);
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    public void toggle() {
        this.value = !this.value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }
}
