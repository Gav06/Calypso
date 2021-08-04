package me.gavin.calypso.module.mod;

import me.gavin.calypso.events.PacketEvent;
import me.gavin.calypso.events.PlayerUpdateEvent;
import me.gavin.calypso.module.ModCategory;
import me.gavin.calypso.module.Module;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CrystalAura extends Module {

    public CrystalAura() {
        super("CrystalAura", "Break and place crystals automatically", ModCategory.Combat);
    }

    private BlockPos renderPos = null;

//    @SubscribeEvent
//    public void onPacketReceive(PacketEvent.Receive event) {
//        if (event.getPacket() instanceof SPacketSpawnObject) {
//            final SPacketSpawnObject spawnObjectPacket = (SPacketSpawnObject) event.getPacket();
//            if (spawnObjectPacket.getType() == 25) {
//
//            }
//        }
//    }

    @SubscribeEvent
    public void onTick(PlayerUpdateEvent event) {

    }

    @SubscribeEvent
    public void onRender3d(RenderWorldLastEvent event) {

    }

    @SubscribeEvent
    public void onRender2d(RenderGameOverlayEvent.Text event) {

    }
}
