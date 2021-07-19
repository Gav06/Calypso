package me.gavin.calypso.gui;

import me.gavin.calypso.module.HUDModule;
import net.minecraft.client.gui.Gui;

public class HUDComponent extends DragComponent {

    private final HUDModule hudModule;

    public HUDComponent(HUDModule module, int width, int height) {
        super(10, 10, width, height);
        this.hudModule = module;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isWithin(mouseX, mouseY) && mouseButton == 0)
            startDragLogic(mouseX, mouseY);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0)
            stopDragLogic(mouseX, mouseY);
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        updateDragPos(mouseX, mouseY);
        int color = 0x90000000;
        if (dragging)
            color = 0x90FFFFFF;
        Gui.drawRect(x, y, x + width, y + height, color);
        hudModule.drawComponent(partialTicks);
    }

    public HUDModule getParent() {
        return hudModule;
    }
}