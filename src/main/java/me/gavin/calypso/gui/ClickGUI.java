package me.gavin.calypso.gui;

import com.google.common.collect.Lists;
import me.gavin.calypso.Calypso;
import me.gavin.calypso.gui.setting.ColorComponent;
import me.gavin.calypso.misc.Util;
import me.gavin.calypso.module.ModCategory;
import me.gavin.calypso.module.Module;
import me.gavin.calypso.module.mod.ClickGUIModule;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class ClickGUI extends GuiScreen {

    private final ArrayList<Panel> panels;

    private final ClickGUIModule module;

    public ClickGUI() {
        panels = new ArrayList<>();

        int xoffset = 10;
        for (ModCategory category : ModCategory.values()) {
            if (category != ModCategory.HUD) {
                final ArrayList<Module> list = Calypso.INSTANCE.getModuleManager().getModulesFromCategory(category);
                panels.add(new Panel(category, list, xoffset, 10, 105, 12));
                xoffset += 120;
            }
        }

        this.module = Calypso.INSTANCE.getModuleManager().getModule(ClickGUIModule.class);
    }

    private final ResourceLocation blur = new ResourceLocation("shaders/post/blur.json");

    @Override
    public void initGui() {
        if (module.blur.getValue()) {
            mc.entityRenderer.loadShader(blur);
        }
    }

    @Override
    public void onGuiClosed() {
        if (mc.entityRenderer.getShaderGroup() != null) {
            mc.entityRenderer.getShaderGroup().deleteShaderGroup();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        for (Panel panel : panels) {
            panel.draw(mouseX, mouseY, partialTicks);
        }
        if (module.descriptions.getValue()) {
            for (Panel panel : Lists.reverse(panels)) {
                if (panel.getPanelRect().isWithin(mouseX, mouseY)) {
                    for (Button button : panel.getButtons()) {
                        if (button.isWithin(mouseX, mouseY)) {
                            final String desc = button.getModule().getDescription();
                            final int x = mouseX + 8;
                            final int y = mouseY - 10;
                            Gui.drawRect(x - 2, y - 2, (x - 2) + mc.fontRenderer.getStringWidth(desc) + 3, (y - 2) + mc.fontRenderer.FONT_HEIGHT + 3, 0x90000000);
                            mc.fontRenderer.drawStringWithShadow(desc, x, y, -1);
                            return;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        for (Panel panel : Lists.reverse(panels)) {
            if (panel.getPanelRect().isWithin(mouseX, mouseY)) {
                panel.mouseClicked(mouseX, mouseY, mouseButton);
                break;
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        for (Panel panel : panels) {
            panel.mouseReleased(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void keyTyped(char keyChar, int keycode) throws IOException {
        for (Panel panel : panels) {
            panel.keyTyped(keyChar, keycode);
        }

        super.keyTyped(keyChar, keycode);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public ArrayList<Panel> getPanels() {
        return panels;
    }

    public Panel getPanelByName(String name) {
        for (Panel panel : panels) {
            if (panel.getCategory().name().equalsIgnoreCase(name))
                return panel;
        }

        return null;
    }
}
