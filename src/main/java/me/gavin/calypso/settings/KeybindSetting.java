package me.gavin.calypso.settings;

import org.lwjgl.input.Keyboard;

public class KeybindSetting extends AbstractSetting {

    private int keybind;

    public KeybindSetting(String name, int keybind) {
        super(name);
        this.keybind = keybind;
    }

    public int getBind() {
        return keybind;
    }

    public void setBind(int bind) {
        this.keybind = bind;
    }

    public String getKeyName() {
        return Keyboard.getKeyName(keybind);
    }
}
