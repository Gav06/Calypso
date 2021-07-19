package me.gavin.calypso.gui;

import net.minecraft.client.Minecraft;

public abstract class Component extends Rect implements Clickable, Drawable {
    public Component(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    protected final Minecraft mc = Minecraft.getMinecraft();
}
