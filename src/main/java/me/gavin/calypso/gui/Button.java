package me.gavin.calypso.gui;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.calypso.Calypso;
import me.gavin.calypso.gui.setting.BindComponent;
import me.gavin.calypso.gui.setting.BooleanComponent;
import me.gavin.calypso.gui.setting.ModeComponent;
import me.gavin.calypso.gui.setting.SliderComponent;
import me.gavin.calypso.module.HUDModule;
import me.gavin.calypso.module.Module;
import me.gavin.calypso.module.mod.ClickGUIModule;
import me.gavin.calypso.settings.AbstractSetting;
import me.gavin.calypso.settings.BoolSetting;
import me.gavin.calypso.settings.EnumSetting;
import me.gavin.calypso.settings.NumSetting;
import net.minecraft.client.gui.Gui;

import java.util.ArrayList;

public class Button extends Component implements Typeable {

    private final ArrayList<SettingComponent> settingComponents;
    private final Module module;

    private boolean open;

    private final ClickGUIModule gui;

    public Button(Module module, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.settingComponents = new ArrayList<>();
        this.module = module;
        for (AbstractSetting setting : module.getSettings()) {
            if (setting instanceof EnumSetting) {
                this.settingComponents.add(new ModeComponent((EnumSetting) setting, x, y, width - 4, height - 2));
            } else if (setting instanceof BoolSetting) {
                this.settingComponents.add(new BooleanComponent((BoolSetting) setting, x, y, width - 4, height - 2));
            } else if (setting instanceof NumSetting) {
                this.settingComponents.add(new SliderComponent((NumSetting) setting, x, y, width - 4, height - 2));
            }
        }

        if (!(module instanceof HUDModule)) {
            this.settingComponents.add(new BooleanComponent(module.visibleSetting, x, y, width - 4, height - 2));
            this.settingComponents.add(new BindComponent(module.getKeybind(), x, y, width - 4, height - 2));
        }

        this.gui = Calypso.INSTANCE.getModuleManager().getModule(ClickGUIModule.class);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isWithin(mouseX, mouseY)) {
            if (mouseButton == 0) {
                module.toggle();
            } else if (mouseButton == 1) {
                open = !open;
            }
        }

        if (open) {
            for (SettingComponent component : settingComponents) {
                component.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (open) {
            for (SettingComponent component : settingComponents) {
                component.mouseReleased(mouseX, mouseY, mouseButton);
            }
        }
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {

        if (open) {
            int yoffset = height + 1;
            for (SettingComponent settingComponent : settingComponents) {
                settingComponent.x = x + 2;
                settingComponent.y = y + yoffset;

                yoffset += settingComponent.getTotalHeight() + 1;

                settingComponent.draw(mouseX, mouseY, partialTicks);
            }
        }
        final ChatFormatting chatFormatting = isWithin(mouseX, mouseY) ? ChatFormatting.GRAY : ChatFormatting.WHITE;
        if (gui.buttonBackground.getValue())
            Gui.drawRect(x, y, x + width, y + height, module.isEnabled() ? 0xF0BF0000 : 0x60BF0000);
        String str = module.getName();
        if (!gui.buttonBackground.getValue() && module.isEnabled())
            str = ChatFormatting.GREEN + str;
        mc.fontRenderer.drawStringWithShadow(chatFormatting + str, x + 2f, y + (height / 2f) - (mc.fontRenderer.FONT_HEIGHT / 2f),-1);
        final String txt = open ? "." : "...";
        mc.fontRenderer.drawStringWithShadow(chatFormatting + txt,
                x + width - (mc.fontRenderer.getStringWidth(txt)) - 2,
                y + (height / 2f) - (mc.fontRenderer.FONT_HEIGHT / 2f), -1);
    }

    @Override
    public void keyTyped(char keyChar, int keycode) {
        if (open) {
            for (SettingComponent component : settingComponents) {
                component.keyTyped(keyChar, keycode);
            }
        }
    }

    public int getTotalHeight() {
        int height = this.height + 1;
        if (open) {
            for (SettingComponent settingComponent : settingComponents) {
                height += settingComponent.getTotalHeight() + 1;
            }
        }
        return height;
    }

    public ArrayList<SettingComponent> getSettingComponents() {
        return this.settingComponents;
    }

    public Module getModule() {
        return module;
    }

    public boolean isOpen() {
        return open;
    }
}