package me.gavin.calypso.settings;

public abstract class AbstractSetting {

    private final String name;

    public AbstractSetting(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
