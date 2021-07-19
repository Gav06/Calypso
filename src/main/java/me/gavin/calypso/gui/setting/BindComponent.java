package me.gavin.calypso.gui.setting;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.calypso.gui.SettingComponent;
import me.gavin.calypso.settings.KeybindSetting;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;

public class BindComponent extends SettingComponent {

    private final KeybindSetting setting;

    public BindComponent(KeybindSetting setting, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.setting = setting;
    }

    private boolean binding;

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isWithin(mouseX, mouseY) && mouseButton == 0)
            binding = !binding;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        if (clickGUIModule.buttonBackground.getValue())
            Gui.drawRect(x, y, x + width, y + height, 0x90060606);
        final ChatFormatting chatFormatting = isWithin(mouseX, mouseY) ? ChatFormatting.GRAY : ChatFormatting.WHITE;
        final String str = chatFormatting + (binding ? "Listening..." : setting.getName() + ": " + setting.getKeyName());
        GlStateManager.pushMatrix();
        GlStateManager.translate(
                x + (width / 2f) - ((mc.fontRenderer.getStringWidth(str) / 2f) * 0.85),
                y + (height / 2f) - ((mc.fontRenderer.FONT_HEIGHT / 2f) * 0.85), 0);
        GlStateManager.scale(0.85, 0.85, 0);
        mc.fontRenderer.drawStringWithShadow(str, 0, 0, -1);
        GlStateManager.popMatrix();
    }

    @Override
    public int getTotalHeight() {
        return height;
    }

    @Override
    public void keyTyped(char keyChar, int keycode) {
        if (binding) {
            if (keycode == Keyboard.KEY_DELETE || keycode == Keyboard.KEY_BACK) {
                setting.setBind(Keyboard.KEY_NONE);
            } else {
                setting.setBind(keycode);
            }
            binding = false;
        }
    }
}