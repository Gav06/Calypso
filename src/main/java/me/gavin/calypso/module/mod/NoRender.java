package me.gavin.calypso.module.mod;

import me.gavin.calypso.events.ArmorVisibilityEvent;
import me.gavin.calypso.events.HurtcamRenderEvent;
import me.gavin.calypso.module.ModCategory;
import me.gavin.calypso.module.Module;
import me.gavin.calypso.settings.BoolSetting;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoRender extends Module {

    public NoRender() {
        super("NoRender", "Prevent rendering certain things", ModCategory.Visual);
        this.getSettings().add(fire);
        this.getSettings().add(water);
        this.getSettings().add(block);
        this.getSettings().add(items);
        this.getSettings().add(xp);
        //this.getSettings().add(skylight);
        this.getSettings().add(hurtCam);
        this.getSettings().add(armor);
    }

    private final BoolSetting fire = new BoolSetting("Fire", true);
    private final BoolSetting water = new BoolSetting("Water Overlay", true);
    private final BoolSetting block = new BoolSetting("Block Overlay", true);

    private final BoolSetting items = new BoolSetting("Items", false);
    private final BoolSetting xp = new BoolSetting("XP", false);

    private final BoolSetting skylight = new BoolSetting("Skylight Updates", false);

    private final BoolSetting hurtCam = new BoolSetting("Hurt Camera", true);

    private final BoolSetting armor = new BoolSetting("Armor", false);

    @SubscribeEvent
    public void onFire(RenderBlockOverlayEvent event) {
        if (event.getOverlayType() == RenderBlockOverlayEvent.OverlayType.FIRE && fire.getValue()) {
            event.setCanceled(true);
        } else if (event.getOverlayType() == RenderBlockOverlayEvent.OverlayType.WATER && water.getValue()) {
            event.setCanceled(true);
        } else if (event.getOverlayType() == RenderBlockOverlayEvent.OverlayType.BLOCK && block.getValue()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onHurtcam(HurtcamRenderEvent event) {
        if (hurtCam.getValue())
            event.setCanceled(true);
    }

    @SubscribeEvent
    public void onArmorShow(ArmorVisibilityEvent event) {
        if (armor.getValue())
            event.setCanceled(true);
    }
}
