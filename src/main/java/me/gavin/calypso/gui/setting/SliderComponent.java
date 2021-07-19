package me.gavin.calypso.gui.setting;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.calypso.gui.SettingComponent;
import me.gavin.calypso.misc.Util;
import me.gavin.calypso.settings.NumSetting;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SliderComponent extends SettingComponent {

    private final NumSetting setting;

    public SliderComponent(NumSetting setting, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.setting = setting;
    }

    private boolean draggingSlider;
    private float sliderWidth;

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isWithin(mouseX, mouseY) && mouseButton == 0)
            draggingSlider = true;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && draggingSlider)
            draggingSlider = false;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        updateLogic(mouseX, mouseY);

        int col = 0x90BF0000;
        if (clickGUIModule.buttonBackground.getValue()) {
            Gui.drawRect(x, y, x + width, y + height, 0x90060606);
            col = 0xBEBF0000;
        }
        Gui.drawRect(x, y, x + (int)sliderWidth, y + height, 0xBEBF0000);
        final ChatFormatting chatFormatting = isWithin(mouseX, mouseY) ? ChatFormatting.GRAY : ChatFormatting.WHITE;
        final String str = chatFormatting + setting.getName() + ": " + setting.getValue();
        GlStateManager.pushMatrix();
        GlStateManager.translate(
                x + (width / 2f) - ((mc.fontRenderer.getStringWidth(str) / 2f) * 0.85),
                y + (height / 2f) - ((mc.fontRenderer.FONT_HEIGHT / 2f) * 0.85), 0);
        GlStateManager.scale(0.85, 0.85, 0);
        mc.fontRenderer.drawStringWithShadow(str, 0, 0,-1);
        GlStateManager.popMatrix();
    }

    @Override
    public void keyTyped(char keyChar, int keycode) {
    }

    private void updateLogic(int mouseX, int mouseY) {
        float difference = Math.min(width, Math.max(0, mouseX - x));
        float min = setting.getMin();
        float max = setting.getMax();
        float value = setting.getValue();
        sliderWidth = width * (value - min) / (max - min);
        if (draggingSlider) {
            if (difference == 0) {
                setting.setValue(min);
            } else {
                float val = Util.roundNumber(difference / width * (max - min) + min, 1);
                setting.setValue(val);
            }
        }
    }


    @Override
    public int getTotalHeight() {
        return height;
    }
}