package me.gavin.calypso.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;

public class InventoryUtil {

    public static int getHotbarSlot(Item item) {
        final Minecraft mc = Minecraft.getMinecraft();
        int itemSlot = mc.player.getHeldItemMainhand().getItem() == item ? mc.player.inventory.currentItem : -1;
        if (itemSlot == -1) {
            for (int l = 0; l < 9; ++l) {
                if (mc.player.inventory.getStackInSlot(l).getItem() == item) {
                    itemSlot = l;
                }
            }
        }

        return itemSlot;
    }
}