package me.gavin.calypso.misc;

import java.awt.*;

public class ColorUtil {

    public static int getRainbow(float time, float saturation) {
        float hue = (System.currentTimeMillis() % (int) (time * 1000)) / (time * 1000);
        return Color.HSBtoRGB(hue, saturation, 1f);
    }

    public static int getRGBWave(float seconds, float saturation, long index) {
        float hue = (float) ((System.currentTimeMillis() + index) % (long) ((int) (seconds * 1000.0F))) / (seconds * 1000.0F);
        return Color.HSBtoRGB(hue, saturation, 1f);
    }

    public static Color normalizedFade(float value, Color startColor, Color endColor) {

        final float sr = startColor.getRed() / 255f;
        final float sg = startColor.getGreen() / 255f;
        final float sb = startColor.getBlue() / 255f;

        final float er = endColor.getRed() / 255f;
        final float eg = endColor.getGreen() / 255f;
        final float eb = endColor.getBlue() / 255f;

        final float r = sr * value + er * (1f - value);
        final float g = sg * value + eg * (1f - value);
        final float b = sb * value + eb * (1f - value);

        return new Color(r, g, b);
    }

    public static Color getColorFlow(double time, double speed, Color startColor, Color endColor) {
        final double sin = (Math.sin(((System.currentTimeMillis() / speed) + time)) * 0.5) + 0.5;
        return normalizedFade(
                (float)sin,
                startColor, endColor);
    }
}
