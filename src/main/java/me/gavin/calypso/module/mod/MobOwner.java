package me.gavin.calypso.module.mod;

import me.gavin.calypso.events.PlayerUpdateEvent;
import me.gavin.calypso.misc.ProjectionUtil;
import me.gavin.calypso.misc.UUIDResolver;
import me.gavin.calypso.module.ModCategory;
import me.gavin.calypso.module.Module;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;

public class MobOwner extends Module {

    private final HashMap<EntityTameable, String> resolvedEntities = new HashMap<>();

    public MobOwner() {
        super("MobOwner", "Show the owner's name of tamed mobs", ModCategory.Misc);
    }

    @SubscribeEvent
    public void onTick(PlayerUpdateEvent event) {
        for (Entity entity : mc.world.loadedEntityList) {
            if (entity instanceof EntityTameable) {
                final EntityTameable tameable = (EntityTameable) entity;
                if (tameable.getOwnerId() != null) {
                    final String ownerUUID = tameable.getOwnerId().toString();
                    if (!resolvedEntities.containsKey(tameable)) {
                        resolvedEntities.put(tameable, null);
                        new Thread(() -> resolvedEntities.put(tameable, UUIDResolver.resolveName(ownerUUID))).start();
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        for (Entity entity : mc.world.loadedEntityList) {
            if (entity instanceof EntityTameable) {
                final EntityTameable entityTameable = (EntityTameable) entity;
                if (entityTameable.getOwnerId() != null) {
                    if (resolvedEntities.containsKey(entityTameable)) {
                        String s;
                        if (resolvedEntities.get(entityTameable) != null) {
                            s = resolvedEntities.get(entityTameable);
                        } else {
                            s = entityTameable.getOwnerId().toString();
                        }

                        final double lerpX = MathHelper.clampedLerp(entityTameable.lastTickPosX, entityTameable.posX, event.getPartialTicks());
                        final double lerpY = MathHelper.clampedLerp(entityTameable.lastTickPosY, entityTameable.posY, event.getPartialTicks());
                        final double lerpZ = MathHelper.clampedLerp(entityTameable.lastTickPosZ, entityTameable.posZ, event.getPartialTicks());

                        final Vec3d projection = ProjectionUtil.toScaledScreenPos(new Vec3d(lerpX, lerpY + entityTameable.height, lerpZ));

                        GlStateManager.pushMatrix();
                        GlStateManager.translate(projection.x, projection.y, 0);
                        GlStateManager.scale(0.85f, 0.85f, 0f);
                        mc.fontRenderer.drawStringWithShadow(s, -(mc.fontRenderer.getStringWidth(s) / 2f), -mc.fontRenderer.FONT_HEIGHT, -1);
                        GlStateManager.popMatrix();
                    }
                }
            }
        }
    }
}