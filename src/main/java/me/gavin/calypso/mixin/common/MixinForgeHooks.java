package me.gavin.calypso.mixin.common;

import me.gavin.calypso.Calypso;
import me.gavin.calypso.module.mod.MiddleClickXP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ForgeHooks.class)
public class MixinForgeHooks {

    @Inject(method = "onPickBlock", at = @At("HEAD"), cancellable = true, remap = false)
    private static void onPickBlockInject(RayTraceResult target, EntityPlayer player, World world, CallbackInfoReturnable<Boolean> cir) {
        final MiddleClickXP mcxp = Calypso.INSTANCE.getModuleManager().getModule(MiddleClickXP.class);
        if (mcxp.isEnabled() && mcxp.cancelSwitchSetting.getValue())
            cir.cancel();
    }
}
