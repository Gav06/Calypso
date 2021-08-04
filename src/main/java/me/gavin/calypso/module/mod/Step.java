package me.gavin.calypso.module.mod;

import me.gavin.calypso.events.PacketEvent;
import me.gavin.calypso.events.PlayerUpdateEvent;
import me.gavin.calypso.module.ModCategory;
import me.gavin.calypso.module.Module;
import me.gavin.calypso.settings.NumSetting;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

public class Step extends Module {

    public final NumSetting stepHeight = new NumSetting("Step Height", 1f, 0f, 2f);

    private final float defaultStepHeight = 0.6f;

    public Step() {
        super("Step", "Step up blocks", ModCategory.Movement);
        this.getSettings().add(stepHeight);
    }

    private CPacketPlayer prevPacket = null;

    @Override
    protected void onDisable() {
        mc.player.stepHeight = defaultStepHeight;
    }

    @SubscribeEvent
    public void onTick(PlayerUpdateEvent event) {
        mc.player.stepHeight = mc.player.onGround ? stepHeight.getValue() : defaultStepHeight;
    }

    @SubscribeEvent
    public void onPacket(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketPlayer.Position || event.getPacket() instanceof CPacketPlayer.PositionRotation) {
            CPacketPlayer cPacketPlayer = (CPacketPlayer) event.getPacket();
            if (prevPacket != null) {
                double diffY = cPacketPlayer.getY(0f) - prevPacket.getY(0f);

                if (diffY > defaultStepHeight && diffY > 1.2491870787) {
                    ArrayList<CPacketPlayer> packetSendList = new ArrayList<>();

                    double x = prevPacket.getX(0f);
                    double y = prevPacket.getY(0f);
                    double z = prevPacket.getZ(0f);

                    packetSendList.add(new CPacketPlayer.Position(x, y + 0.4199999869D, z, true));
                    packetSendList.add(new CPacketPlayer.Position(x, y + 0.7531999805D, z, true));
                    packetSendList.add(new CPacketPlayer.Position(cPacketPlayer.getX(0f), cPacketPlayer.getY(0f), cPacketPlayer.getZ(0f), cPacketPlayer.onGround));
                    for (Packet<?> packet : packetSendList) {
                        mc.player.connection.sendPacket(packet);
                    }
                    event.setCanceled(true);
                }
            }
            prevPacket = cPacketPlayer;
        }
    }
}