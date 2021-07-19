package me.gavin.calypso.gui.setting;

import me.gavin.calypso.settings.NumSetting;

public abstract class CustomSliderComponent extends SliderComponent {
    public CustomSliderComponent(NumSetting setting, int x, int y, int width, int height) {
        super(setting, x, y, width, height);
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        super.updateLogic(mouseX, mouseY);
        drawCustomSlider(mouseX, mouseY, partialTicks);
    }

    public abstract void drawCustomSlider(int mouseX, int mouseY, float partialTicks);
}
