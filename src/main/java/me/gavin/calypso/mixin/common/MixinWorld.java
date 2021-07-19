package me.gavin.calypso.mixin.common;

import me.gavin.calypso.Calypso;
import me.gavin.calypso.module.mod.NoWeather;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public class MixinWorld {

    @Inject(method = "getRainStrength", at = @At("HEAD"), cancellable = true)
    public void getRainStrengthInject(float partialTicks, CallbackInfoReturnable<Float> cir) {
        final NoWeather noWeather = Calypso.INSTANCE.getModuleManager().getModule(NoWeather.class);
        if (noWeather.isEnabled()) {
            cir.setReturnValue(noWeather.rainStrength.getValue());
        }
    }
}
