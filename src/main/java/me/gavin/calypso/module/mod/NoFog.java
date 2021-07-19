package me.gavin.calypso.module.mod;

import me.gavin.calypso.events.RenderFogPostEvent;
import me.gavin.calypso.module.ModCategory;
import me.gavin.calypso.module.Module;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoFog extends Module {

    public NoFog() {
        super("NoFog", "(bypasses on oldfag)", ModCategory.Visual);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onFog(RenderFogPostEvent event) {
        GlStateManager.disableFog();
    }
}
