package me.gavin.calypso.module.mod;

import me.gavin.calypso.events.PlayerDamageBlockEvent;
import me.gavin.calypso.module.ModCategory;
import me.gavin.calypso.module.Module;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public class PacketMine extends Module {
    public PacketMine() {
        super("PacketMine", "Mine with packets :D", ModCategory.Player);
    }

    @SubscribeEvent
    public void onPlayerDamageBlock(PlayerDamageBlockEvent event) {
        event.setCanceled(true);
        if (isBreakable(event.getPos())) {
            mc.player.swingArm(EnumHand.MAIN_HAND);
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.getPos(), event.getFacing()));
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.getPos(), event.getFacing()));
        }
    }

    private boolean isBreakable(BlockPos pos) {
        final IBlockState state = mc.world.getBlockState(pos);
        return state.getBlock().getBlockHardness(state, mc.world, pos) != -1;
    }
}
