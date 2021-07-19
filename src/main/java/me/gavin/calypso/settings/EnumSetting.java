package me.gavin.calypso.settings;

public class EnumSetting extends AbstractSetting {

    private Enum value;

    public EnumSetting(String name, Enum value) {
        super(name);
        this.value = value;
    }

    public Enum getValue() {
        return value;
    }

    public void setValue(Enum value) {
        this.value = value;
    }

    public void incrementValue(boolean backwards) {
        if (backwards) value = EnumIndexer.decreaseEnum(value); else value = EnumIndexer.increaseEnum(value);
    }
}