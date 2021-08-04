package me.gavin.calypso.module.mod;

import me.gavin.calypso.events.PacketEvent;
import me.gavin.calypso.misc.TickTimer;
import me.gavin.calypso.module.ModCategory;
import me.gavin.calypso.module.Module;
import me.gavin.calypso.settings.EnumSetting;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketUseEntity.Action;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Criticals extends Module {

    public final EnumSetting critMode = new EnumSetting("Mode", CritMode.SIMPLE);
    public final TickTimer timer = new TickTimer();

    public Criticals() {
        super("Criticals", "Tries to make you hit more crits", ModCategory.Combat);
        this.getSettings().add(critMode);
    }

    @SubscribeEvent
    public void onPacket(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketUseEntity) {
            final CPacketUseEntity packet = (CPacketUseEntity) event.getPacket();
            if (packet.getAction() == Action.ATTACK && mc.player.onGround) {
                if (critMode.getValue() == CritMode.SIMPLE) {
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.1f, mc.player.posZ, false));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
                } else if (critMode.getValue() == CritMode.WEIRD) {
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.11, mc.player.posZ, false));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.11, mc.player.posZ, false));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.1100013579, mc.player.posZ, false));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.1100013579, mc.player.posZ, false));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.1100013579, mc.player.posZ, false));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.1100013579, mc.player.posZ, false));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
                } else if (critMode.getValue() == CritMode.BYPASS) {
                    if (this.mc.player.fallDistance > 0.0f) {
                        return;
                    } if (this.mc.player.isInLava() || this.mc.player.isInWater()) {
                        return;
                    } if (this.timer.passed(1000)) {
                        this.timer.reset();
                        this.mc.player.connection.sendPacket(new CPacketPlayer.Position(this.mc.player.posX, this.mc.player.posY + 0.11, this.mc.player.posZ, false));
                        this.mc.player.connection.sendPacket(new CPacketPlayer.Position(this.mc.player.posX, this.mc.player.posY + 0.1100013579, this.mc.player.posZ, false));
                        this.mc.player.connection.sendPacket(new CPacketPlayer.Position(this.mc.player.posX, this.mc.player.posY + 1.3579E-6, this.mc.player.posZ, false));
                    }
                } else {
                    mc.player.jump();
                }
            }
        }
    }

    public enum CritMode {
        SIMPLE,
        JUMP,
        WEIRD,
        BYPASS
    }
}
