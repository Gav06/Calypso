package me.gavin.calypso.settings;

import java.awt.*;

public class ColorSetting extends AbstractSetting {

    private final NumSetting hue;
    private final NumSetting saturation;
    private final NumSetting brightness;
    private final NumSetting alpha;

    public ColorSetting(String name, int red, int green, int blue, int alpha) {
        super(name);
        final float[] arr = Color.RGBtoHSB(red, green, blue, null);
        this.hue = new NumSetting("Hue", arr[0], 0f, 1.0f);
        this.saturation = new NumSetting("Saturation", arr[1], 0f, 1.0f);
        this.brightness = new NumSetting("Brightness", arr[2], 0f, 1.0f);
        this.alpha = new NumSetting("Alpha", alpha, 0, 255);
    }

    public NumSetting getHue() {
        return hue;
    }

    public NumSetting getSaturation() {
        return saturation;
    }

    public NumSetting getBrightness() {
        return brightness;
    }

    public NumSetting getAlpha() {
        return alpha;
    }

    public Color getColor() {
        return Color.getHSBColor(hue.getValue(), saturation.getValue(), brightness.getValue());
    }
}