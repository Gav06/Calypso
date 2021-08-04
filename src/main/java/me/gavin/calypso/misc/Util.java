package me.gavin.calypso.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Util {

    public static final String PREFIX = ChatFormatting.RED + "<Calypso> " + ChatFormatting.RESET;

    public static void sendMessage(String message) {
        if (Minecraft.getMinecraft().player != null) {
            Minecraft.getMinecraft().player.sendMessage(new TextComponentString(PREFIX + message));
        }
    }

    public static void sendMessage(String message, int messageId) {
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new TextComponentString(PREFIX + message), messageId);
    }

    public static float normalize(float value, float min, float max) {
        return 1.0f - ((value - min) / (max - min));
    }

    public static float roundNumber(float value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        final BigDecimal bd = new BigDecimal(value).setScale(places, RoundingMode.UP);
        return bd.floatValue();
    }

    public static double[] calculateLookAt(double x, double y, double z, EntityPlayer me) {
        double dirx = me.posX - x;
        double diry = me.posY + me.getEyeHeight() - y;
        double dirz = me.posZ - z;

        double distance = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);

        dirx /= distance;
        diry /= distance;
        dirz /= distance;

        double pitch = Math.asin(diry);
        double yaw = Math.atan2(dirz, dirx);

        pitch = pitch * 180.0D / Math.PI;
        yaw = yaw * 180.0D / Math.PI;

        yaw += 90.0D;

        return new double[] {yaw, pitch};
    }
}