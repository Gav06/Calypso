package me.gavin.calypso.gui.setting;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.calypso.gui.SettingComponent;
import me.gavin.calypso.settings.EnumSetting;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

public class ModeComponent extends SettingComponent {

    private final EnumSetting setting;

    public ModeComponent(EnumSetting setting, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.setting = setting;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isWithin(mouseX, mouseY)) {
            if (mouseButton == 0) {
                setting.incrementValue(false);
            } else if (mouseButton == 1) {
                setting.incrementValue(true);
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) { }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        if (clickGUIModule.buttonBackground.getValue())
            Gui.drawRect(x, y, x + width, y + height, 0x90060606);
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
    public int getTotalHeight() {
        return height;
    }

    @Override
    public void keyTyped(char keyChar, int keycode) { }
}
