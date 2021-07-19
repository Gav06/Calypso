package me.gavin.calypso.module.mod;

import me.gavin.calypso.events.PlayerUpdateEvent;
import me.gavin.calypso.misc.InventoryUtil;
import me.gavin.calypso.module.ModCategory;
import me.gavin.calypso.module.Module;
import me.gavin.calypso.settings.BoolSetting;
import me.gavin.calypso.settings.EnumSetting;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Mouse;

public class MiddleClickXP extends Module {

    public MiddleClickXP() {
        super("MiddleClickXP", "Throws xp silently when you middle click", ModCategory.Combat);
        //this.getSettings().add(throwModeSetting);
        this.getSettings().add(cancelSwitchSetting);
    }

    public final EnumSetting throwModeSetting = new EnumSetting("Behavior", ThrowBehavior.WHILE_LOOKING_AT_BLOCK);

    public final BoolSetting cancelSwitchSetting = new BoolSetting("Cancel Switch", true);

    private boolean mouseHolding = false;

    @SubscribeEvent
    public void onMouse(InputEvent.MouseInputEvent event) {
        if (Mouse.getEventButton() == 2) {
            mouseHolding = Mouse.getEventButtonState();
        }
    }

    @SubscribeEvent
    public void onTick(PlayerUpdateEvent event) {
        if (mouseHolding) {

            int chosenSlot = getXPSlot();

            if (isLookingAtBlock()) {
                if (chosenSlot != -1) {
                    switchToSlot(chosenSlot);
                    throwXP();
                    switchBack();
                }
            }
        }
    }

    private void switchToSlot(int slot) {
        mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
    }

    private void switchBack() {
        mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
    }

    private void throwXP() {
        mc.rightClickDelayTimer = 0;
        mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
    }

    private int getXPSlot() {
        return InventoryUtil.getHotbarSlot(Items.EXPERIENCE_BOTTLE);
    }

    private boolean isLookingAtBlock() {
        return mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK && mc.objectMouseOver != null;
    }

    public enum ThrowBehavior {
        ALWAYS_DOWN,
        WHILE_LOOKING_AT_BLOCK
    }
}