package me.gavin.calypso.module;

import me.gavin.calypso.gui.HUDComponent;
import net.minecraft.client.Minecraft;

public abstract class HUDModule extends Module {

    protected final Minecraft mc;

    protected final HUDComponent component;

    public HUDModule(String name, String description, int width, int height) {
        super(name, description, ModCategory.HUD);
        this.component = new HUDComponent(this, width, height);
        this.mc = Minecraft.getMinecraft();
    }


    public abstract void drawComponent(float partialTicks);

    public HUDComponent getComponent() {
        return component;
    }

}