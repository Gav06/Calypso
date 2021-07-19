package me.gavin.calypso.mixin.common;

import me.gavin.calypso.events.PlayerPostUpdateEvent;
import me.gavin.calypso.events.PlayerPreUpdateEvent;
import me.gavin.calypso.events.PlayerUpdateEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayerSP.class)
public class MixinEntityPlayerSP {

    @Inject(method = "onUpdate", at = @At("HEAD"))
    public void onUpdateInject(CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new PlayerUpdateEvent());
    }

    @Inject(method = "onUpdateWalkingPlayer", at = @At("HEAD"))
    public void onUpdateWalkingPlayerPreInject(CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new PlayerPreUpdateEvent());
    }

    @Inject(method = "onUpdateWalkingPlayer", at = @At("RETURN"))
    public void onUpdateWalkingPlayerPostInject(CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new PlayerPostUpdateEvent());
    }
}
