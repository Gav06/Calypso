package me.gavin.calypso.gui.setting;

import me.gavin.calypso.gui.Component;
import me.gavin.calypso.misc.Util;
import me.gavin.calypso.settings.NumSetting;
import net.minecraft.client.gui.Gui;

public class QuadSliderComponent extends Component {

    private final NumSetting xSetting;
    private final NumSetting ySetting;

    private boolean dragging;
    private float sliderWidth;
    private float sliderHeight;


    public QuadSliderComponent(NumSetting xSetting, NumSetting ySetting, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.xSetting = xSetting;
        this.ySetting = ySetting;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isWithin(mouseX, mouseY) && mouseButton == 0)
            dragging = true;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0)
            dragging = false;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        updateLogicX(mouseX);
        updateLogicY(mouseY);

        Gui.drawRect(x, y, x + width, y + height, 0x90FFFFFF);
        Gui.drawRect((int) (x + sliderWidth - 1), (int) (y + sliderHeight - 1), (int) (x + sliderWidth + 1), (int) (y + sliderHeight + 1), 0xFF000000);
        mc.fontRenderer.drawStringWithShadow("x: " + xSetting.getValue() + " y: " + ySetting.getValue(), x, y - mc.fontRenderer.FONT_HEIGHT - 1, -1);
    }

    private void updateLogicX(int mouseX) {
        float difference = Math.min(width, Math.max(0, mouseX - x));
        float min = xSetting.getMin();
        float max = xSetting.getMax();
        float value = xSetting.getValue();
        sliderWidth = width * (value - min) / (max - min);
        if (dragging) {
            if (difference == 0) {
                xSetting.setValue(min);
            } else {
                float val = Util.roundNumber(difference / width * (max - min) + min, 1);
                xSetting.setValue(val);
            }
        }
    }

    private void updateLogicY(int mouseY) {
        float difference = Math.min(height, Math.max(0, mouseY - y));
        float min = ySetting.getMin();
        float max = ySetting.getMax();
        float value = ySetting.getValue();
        sliderHeight = height * (value - min) / (max - min);
        if (dragging) {
            if (difference == 0) {
                ySetting.setValue(min);
            } else {
                float val = Util.roundNumber(difference / height * (max - min) + min, 1);
                ySetting.setValue(val);
            }
        }
    }
}
