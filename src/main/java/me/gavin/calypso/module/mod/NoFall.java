package me.gavin.calypso.module.mod;

import me.gavin.calypso.events.PacketEvent;
import me.gavin.calypso.events.PlayerUpdateEvent;
import me.gavin.calypso.module.ModCategory;
import me.gavin.calypso.module.Module;
import me.gavin.calypso.settings.EnumSetting;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoFall extends Module {

    public final EnumSetting nofallMode = new EnumSetting("Mode", NoFallMode.PACKET);

    public NoFall() {
        super("NoFall", "Tries to prevent fall damage", ModCategory.Movement);
        this.getSettings().add(nofallMode);
    }

    @SubscribeEvent
    public void onPlayerTick(PlayerUpdateEvent event) {
        if (nofallMode.getValue() == NoFallMode.TICK) {
            if (mc.player.fallDistance > 3.0f) {
                mc.player.connection.sendPacket(new CPacketPlayer(true));
            }
        }
    }

    @SubscribeEvent
    public void onPacket(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketPlayer) {
            final CPacketPlayer packet = (CPacketPlayer) event.getPacket();
            if (nofallMode.getValue() == NoFallMode.PACKET) {
                if (mc.player.fallDistance > 3f) {
                    packet.onGround = true;
                }
            }
//            } else if (nofallMode.getValue() == NoFallMode.POSITION) {
//                if (mc.player.fallDistance > 3f) {
//                    packet.y = mc.player.posY + 1.3262473694E-314;
//                }
//            } else if (nofallMode.getValue() == NoFallMode.WEIRD) {
//                if (mc.player.fallDistance > 3f) {
//                    mc.player.onGround = true;
//                    mc.player.capabilities.isFlying = true;
//                    mc.player.capabilities.allowFlying = true;
//                    packet.onGround = false;
//                    mc.player.velocityChanged = true;
//                    mc.player.capabilities.isFlying = false;
//                    mc.player.jump();
//                }
//            }
        }
    }

    public enum NoFallMode {
        TICK,
        PACKET
    }
}