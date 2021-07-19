package me.gavin.calypso.mixin.common;

import me.gavin.calypso.Calypso;
import me.gavin.calypso.events.HurtcamRenderEvent;
import me.gavin.calypso.events.RenderFogPostEvent;
import me.gavin.calypso.module.mod.Fullbright;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.settings.GameSettings;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class MixinEntityRenderer {

    @Inject(method = "setupFog", at = @At("RETURN"))
    public void setupFogInject(int startCoords, float partialTicks, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new RenderFogPostEvent());
    }

    @Inject(method = "hurtCameraEffect", at = @At("HEAD"), cancellable = true)
    public void hurtCameraEffectInject(float partialTicks, CallbackInfo ci) {
        final HurtcamRenderEvent event = new HurtcamRenderEvent();
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled())
            ci.cancel();
    }

    @Redirect(method = "updateLightmap(F)V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/settings/GameSettings;gammaSetting:F"))
    public float redirectGammaSetting(GameSettings settings) {
        if (Calypso.INSTANCE.getModuleManager().isModuleEnabled(Fullbright.class))
            return 100f;
        return settings.gammaSetting;
    }
}