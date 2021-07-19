package me.gavin.calypso.gui.setting;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.calypso.gui.SettingComponent;
import me.gavin.calypso.settings.BoolSetting;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

public class BooleanComponent extends SettingComponent {

    private final BoolSetting setting;

    public BooleanComponent(BoolSetting setting, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.setting = setting;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isWithin(mouseX, mouseY) && mouseButton == 0)
            setting.setValue(!(boolean)setting.getValue());
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) { }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        int color = 0x90060606;
        if (setting.getValue()) {
            color = 0x90BF0000;
        }

        ChatFormatting formatting = isWithin(mouseX, mouseY) ? ChatFormatting.GRAY : ChatFormatting.WHITE;
        if (clickGUIModule.buttonBackground.getValue()) {
            Gui.drawRect(x, y, x + width, y + height, color);
        } else {
            if (setting.getValue())
                formatting = ChatFormatting.GREEN;
        }
        GlStateManager.pushMatrix();
        GlStateManager.translate(
                x + (width / 2f) - ((mc.fontRenderer.getStringWidth(setting.getName()) / 2f) * 0.85),
                y + (height / 2f) - ((mc.fontRenderer.FONT_HEIGHT / 2f) * 0.85), 0);
        GlStateManager.scale(0.85, 0.85, 0);
        mc.fontRenderer.drawStringWithShadow(formatting + setting.getName(), 0, 0, -1);
        GlStateManager.popMatrix();
    }

    @Override
    public int getTotalHeight() {
        return height;
    }

    @Override
    public void keyTyped(char keyChar, int keycode) { }
}