package me.gavin.calypso.module.mod;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.calypso.events.PacketEvent;
import me.gavin.calypso.events.PlayerUpdateEvent;
import me.gavin.calypso.misc.Util;
import me.gavin.calypso.module.ModCategory;
import me.gavin.calypso.module.Module;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;

public class TotemPopCounter extends Module {
    public TotemPopCounter() {
        super("TotemPopCounter", "Counts totem pops", ModCategory.Misc);
    }

    public final HashMap<String, Integer> popMap = new HashMap<>();

    @SubscribeEvent
    public void onPacket(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketEntityStatus) {
            final SPacketEntityStatus entityStatusPacket = (SPacketEntityStatus) event.getPacket();
            if (entityStatusPacket.getOpCode() == 35) {
                if (entityStatusPacket.getEntity(mc.world) instanceof EntityPlayer) {
                    processPop((EntityPlayer) entityStatusPacket.getEntity(mc.world));
                }
            }
        }
    }

    @SubscribeEvent
    public void onTick(PlayerUpdateEvent event) {
        for (EntityPlayer player : mc.world.playerEntities) {
            if (popMap.containsKey(player.getName())) {
                if (player.getHealth() <= 0 || player.isDead) {
                    final String s = popMap.get(player.getName()) == 1 ? "" : "s";
                    Util.sendMessage(player.getName() + " has died after popping " + ChatFormatting.GREEN + popMap.get(player.getName()) + ChatFormatting.RESET + " totem" + s, player.getEntityId() * -1);
                    popMap.remove(player.getName());
                }
            }
        }
    }

    private void processPop(EntityPlayer player) {
        if (player.equals(mc.player))
            return;

        if (!popMap.containsKey(player.getName())) {
            popMap.put(player.getName(),1);
        } else {
            final int prevPops = popMap.get(player.getName());
            popMap.put(player.getName(), prevPops + 1);
        }

        final String s = popMap.get(player.getName()) == 1 ? "" : "s";
        Util.sendMessage(player.getName() + " has popped " + ChatFormatting.GREEN + popMap.get(player.getName()) + ChatFormatting.RESET + " totem" + s, player.getEntityId() * -1);
    }
}
