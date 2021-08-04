package me.gavin.calypso.module.mod;

import me.gavin.calypso.events.PacketEvent;
import me.gavin.calypso.module.ModCategory;
import me.gavin.calypso.module.Module;
import me.gavin.calypso.settings.BoolSetting;
import me.gavin.calypso.settings.NumSetting;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Velocity extends Module {

    public final NumSetting horizontal = new NumSetting("Horizontal", 0f, 0f, 100f);
    public final NumSetting vertical = new NumSetting("Vertical", 0f, 0f, 100f);

    public Velocity() {
        super("Velocity", "Prevent knockback", ModCategory.Movement);
        this.getSettings().add(horizontal);
        this.getSettings().add(vertical);
    }

    @SubscribeEvent
    public void onPacket(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketEntityVelocity) {
            final SPacketEntityVelocity packet = (SPacketEntityVelocity) event.getPacket();
            if (packet.getEntityID() == mc.player.getEntityId()) {
                if (horizontal.getValue() == 0f && vertical.getValue() == 0f) {
                    event.setCanceled(true);
                    return;
                }

                packet.motionX = (int) (packet.motionX * (horizontal.getValue() / 100f));
                packet.motionY = (int) (packet.motionY * (vertical.getValue() / 100f));
                packet.motionZ = (int) (packet.motionZ * (horizontal.getValue() / 100f));
            }
        } else if (event.getPacket() instanceof SPacketExplosion) {
            final SPacketExplosion packet = (SPacketExplosion) event.getPacket();
            packet.motionX *= horizontal.getValue();
            packet.motionY *= vertical.getValue();
            packet.motionZ *= horizontal.getValue();
        } else if (event.getPacket() instanceof SPacketEntityStatus) {
            final SPacketEntityStatus packet = (SPacketEntityStatus) event.getPacket();
            if (packet.getOpCode() == 31 && packet.getEntity(mc.world) instanceof EntityFishHook) {
                final EntityFishHook fishHook = (EntityFishHook) packet.getEntity(mc.world);
                if (fishHook.caughtEntity == mc.player) {
                    event.setCanceled(true);
                }
            }
        }
    }
}
