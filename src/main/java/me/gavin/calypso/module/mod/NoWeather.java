package me.gavin.calypso.module.mod;

import me.gavin.calypso.module.ModCategory;
import me.gavin.calypso.module.Module;
import me.gavin.calypso.settings.NumSetting;
import org.lwjgl.input.Keyboard;

public class NoWeather extends Module {

    public NumSetting rainStrength = new NumSetting("Rain Strength", 0f, 0f, 1f);

    public NoWeather() {
        super("Weather", "Change the rain", ModCategory.Visual);
        this.getSettings().add(rainStrength);
    }
}