package me.gavin.calypso.module.hud;

import me.gavin.calypso.misc.ColorUtil;
import me.gavin.calypso.module.HUDModule;
import me.gavin.calypso.module.Module;
import me.gavin.calypso.settings.EnumSetting;

import java.util.ArrayList;

public class ModList extends HUDModule {
    public ModList() {
        super("ModList", "Shows a list of the enabled modules", 1, 1);
        this.getSettings().add(colorMode);
    }

    private final EnumSetting colorMode = new EnumSetting("Color", ColorMode.RAINBOW);

    public enum ColorMode {
        RAINBOW,
        STATIC
    }

    @Override
    public void drawComponent(float partialTicks) {
        final ArrayList<Module> list = calypso.getModuleManager().getEnabledModules();
        if (!list.isEmpty()) {
            component.setWidth(mc.fontRenderer.getStringWidth(list.get(0).getName()));
            component.setHeight(list.size() * (mc.fontRenderer.FONT_HEIGHT + 1));

            int yoffset = 0;
            for (Module module : list) {
                final int color;
                if (colorMode.getValue() == ColorMode.RAINBOW) {
                    color = ColorUtil.getRGBWave(8f, 0.8f, yoffset * 10L);
                } else {
                    color = 0xFF0000;
                }

                mc.fontRenderer.drawStringWithShadow(module.getName(),
                        component.getX(), component.getY() + yoffset,
                        color);
                yoffset += mc.fontRenderer.FONT_HEIGHT + 1;
            }
        } else {
            component.setWidth(10);
            component.setHeight(10);
        }
    }
}
