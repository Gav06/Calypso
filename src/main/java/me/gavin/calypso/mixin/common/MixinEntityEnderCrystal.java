package me.gavin.calypso.mixin.common;

import me.gavin.calypso.events.CrystalExplosionEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityEnderCrystal.class)
public class MixinEntityEnderCrystal {

    // TODO: make this not trans
    @Redirect(method = "attackEntityFrom", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;createExplosion(Lnet/minecraft/entity/Entity;DDDFZ)Lnet/minecraft/world/Explosion;"))
    public Explosion attackEntityFrom$Inject$INVOKE$createExplosion(World world, Entity entityIn, double x, double y, double z, float strength, boolean damagesTerrain) {
        MinecraftForge.EVENT_BUS.post(new CrystalExplosionEvent(x, y, z, strength));
        return world.createExplosion((Entity)null, x, y, z, 6.0F, true);
    }
}
