package me.gavin.calypso.module.hud;

import me.gavin.calypso.Calypso;
import me.gavin.calypso.module.HUDModule;

public class Watermark extends HUDModule {

    private final String watermark = Calypso.MOD_NAME + " v" + Calypso.VERSION;

    public Watermark() {
        super("Watermark", "Client name and version", 1, 1);

        component.setHeight(mc.fontRenderer.FONT_HEIGHT);
        component.setWidth(mc.fontRenderer.getStringWidth(watermark));
    }

    @Override
    public void drawComponent(float partialTicks) {
        mc.fontRenderer.drawStringWithShadow(watermark, component.getX(), component.getY(), 0xFF0000);
    }
}
