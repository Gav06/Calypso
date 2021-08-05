package me.gavin.calypso.module.mod;

import me.gavin.calypso.events.PlayerUpdateEvent;
import me.gavin.calypso.misc.InventoryUtil;
import me.gavin.calypso.module.ModCategory;
import me.gavin.calypso.module.Module;
import me.gavin.calypso.settings.BoolSetting;
import me.gavin.calypso.settings.EnumSetting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CrystalAura extends Module {

    private final BoolSetting offhand = new BoolSetting("Offhand", false);
    private final EnumSetting switchStyle = new EnumSetting("Switch", SwitchMode.Normal);
    private final BoolSetting updateController = new BoolSetting("UpdateController", true);

    public CrystalAura() {
        super("CrystalAura", "Break and place crystals automatically", ModCategory.Combat);
        this.getSettings().add(offhand);
        this.getSettings().add(switchStyle);
    }

    private AxisAlignedBB renderPos = null;

    @SubscribeEvent
    public void onTick(PlayerUpdateEvent event) {
        if (switchStyle.getValue() != SwitchMode.Off) {
            if (switchStyle.getValue() == SwitchMode.Normal) {
                if (!offhand.getValue()) {
                    if (mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL) {
                        final int itemSlot = InventoryUtil.getHotbarSlot(Items.END_CRYSTAL);
                        if (itemSlot != -1) {
                            mc.player.inventory.currentItem = itemSlot;
                            mc.player.connection.sendPacket(new CPacketHeldItemChange(itemSlot));
                            if (updateController.getValue())
                                mc.playerController.updateController();
                        }
                    }
                }
            } else {
                // TODO: silent implementation
            }
        }
        placeLogic();
        breakLogic();
    }

    private void placeLogic() {

    }

    private void breakLogic() {

    }

    private EntityPlayer targetPlayer;

    @SubscribeEvent
    public void onRender3d(RenderWorldLastEvent event) {

    }

    @SubscribeEvent
    public void onRender2d(RenderGameOverlayEvent.Text event) {

    }

    public enum SwitchMode {
        Normal,
        Silent,
        Off
    }
}
