package me.gavin.calypso.gui;

import me.gavin.calypso.gui.setting.QuadSliderComponent;
import me.gavin.calypso.module.HUDModule;
import me.gavin.calypso.module.ModCategory;
import me.gavin.calypso.module.Module;
import me.gavin.calypso.module.hud.ModList;
import me.gavin.calypso.module.hud.Watermark;
import me.gavin.calypso.settings.NumSetting;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;

public class HUDEditor extends GuiScreen {

    private final ArrayList<Module> hudModules;

    private final Panel panel;

    public HUDEditor() {
        this.hudModules = new ArrayList<>();
        this.initHudModules();
        this.panel = new Panel(ModCategory.HUD, hudModules, 50, 10, 105, 12);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void initHudModules() {
        hudModules.add(new Watermark());
        hudModules.add(new ModList());
    }

    public ArrayList<Module> getHudModules() {
        return hudModules;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        panel.mouseClicked(mouseX, mouseY, mouseButton);
        for (Module module : hudModules) {
            if (!module.isEnabled())
                continue;
            final HUDModule hudModule = (HUDModule) module;
            if (hudModule.getComponent().isWithin(mouseX, mouseY)) {
                hudModule.getComponent().mouseClicked(mouseX, mouseY, mouseButton);
                break;
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        panel.mouseReleased(mouseX, mouseY, mouseButton);
        for (Module module : hudModules) {
            if (module.isEnabled()) {
                final HUDModule hudModule = (HUDModule) module;
                hudModule.getComponent().mouseReleased(mouseX, mouseY, mouseButton);
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        panel.draw(mouseX, mouseY, partialTicks);
        for (Module module : hudModules) {
            final HUDModule hudModule = (HUDModule) module;
            if (hudModule.isEnabled()) {
                hudModule.getComponent().draw(mouseX, mouseY, partialTicks);
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public Panel getPanel() {
        return panel;
    }

    public HUDModule getHudModuleByName(String name) {
        for (Module module : hudModules) {
            if (module.getName().equals(name))
                return (HUDModule) module;
        }

        return null;
    }

    public HUDComponent getHudComponent(String name) {
        for (HUDComponent component : getHudComponents()) {
            if (component.getParent().getName().equals(name))
                return component;
        }

        return null;
    }

    public ArrayList<HUDComponent> getHudComponents() {
        final ArrayList<HUDComponent> list = new ArrayList<>();
        for (Module module : hudModules) {
            list.add(((HUDModule)module).getComponent());
        }

        return list;
    }
}
