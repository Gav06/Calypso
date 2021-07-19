package me.gavin.calypso.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Util {

    public static void sendMessage(String message) {
        if (Minecraft.getMinecraft().player != null) {
            Minecraft.getMinecraft().player.sendMessage(new TextComponentString(ChatFormatting.RED + "<Calypso> " + ChatFormatting.RESET + message));
        }
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
}
