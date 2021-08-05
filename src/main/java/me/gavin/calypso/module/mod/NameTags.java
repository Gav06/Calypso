package me.gavin.calypso.module.mod;

import com.google.common.collect.Lists;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.calypso.events.RenderPlayerNameEvent;
import me.gavin.calypso.misc.ProjectionUtil;
import me.gavin.calypso.misc.RenderUtil;
import me.gavin.calypso.module.ModCategory;
import me.gavin.calypso.module.Module;
import me.gavin.calypso.settings.BoolSetting;
import me.gavin.calypso.settings.EnumSetting;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NameTags extends Module {

    private final ICamera camera = new Frustum();

    private final BoolSetting health = new BoolSetting("Health", true);
    private final BoolSetting ping = new BoolSetting("Ping", true);
    private final EnumSetting armorStyle = new EnumSetting("Armor", ArmorEnum.Shown);

    public NameTags() {
        super("NameTags", "Better player NameTags", ModCategory.Visual);
        this.getSettings().add(health);
        this.getSettings().add(ping);
        this.getSettings().add(armorStyle);
    }

    // dn

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Text event) {
        camera.setPosition(mc.getRenderViewEntity().posX, mc.getRenderViewEntity().posY, mc.getRenderViewEntity().posZ);
        for (EntityPlayer player : mc.world.playerEntities) {
            if (player.equals(mc.player))
                continue;

            if (!camera.isBoundingBoxInFrustum(player.getEntityBoundingBox()))
                continue;

            final double lerpX = MathHelper.clampedLerp(player.lastTickPosX, player.posX, event.getPartialTicks());
            final double lerpY = MathHelper.clampedLerp(player.lastTickPosY, player.posY, event.getPartialTicks());
            final double lerpZ = MathHelper.clampedLerp(player.lastTickPosZ, player.posZ, event.getPartialTicks());

            final Vec3d projection = ProjectionUtil.toScaledScreenPos(new Vec3d(lerpX, lerpY + player.height + 0.25, lerpZ));

            final ChatFormatting crouch = player.isSneaking() ? ChatFormatting.DARK_RED : ChatFormatting.WHITE;
            final double health = player.getHealth() + player.getAbsorptionAmount();
            GlStateManager.pushMatrix();
            GlStateManager.translate(projection.x, projection.y, 0);
            final String s = getNametagText(player);
            drawArmor(player);
            Gui.drawRect(-(mc.fontRenderer.getStringWidth(s) / 2) - 1, -mc.fontRenderer.FONT_HEIGHT - 2, (mc.fontRenderer.getStringWidth(s) / 2) + 1, 1, 0x90000000);
            mc.fontRenderer.drawStringWithShadow(s, -(mc.fontRenderer.getStringWidth(s) / 2f), -mc.fontRenderer.FONT_HEIGHT, -1);
            GlStateManager.popMatrix();
        }
    }

    private ChatFormatting getHealthColor(double health) {
        if (health >= 14) {
            return ChatFormatting.GREEN;
        } else if (health >= 8) {
            return ChatFormatting.YELLOW;
        } else if (health >= 4) {
            return ChatFormatting.RED;
        } else {
            return ChatFormatting.DARK_RED;
        }
    }

    private String getNametagText(EntityPlayer player) {
        final ChatFormatting crouch = player.isSneaking() ? ChatFormatting.DARK_RED : ChatFormatting.WHITE;
        String text = crouch + player.getName();
        if (ping.getValue()) {
            final int ping = mc.player.connection.getPlayerInfo(player.getName()) != null ? mc.player.connection.getPlayerInfo(player.getName()).getResponseTime() : -1;
            text += " " + ChatFormatting.WHITE + ping + "ms";
        }
        if (health.getValue()) {
            final double health = player.getHealth() + player.getAbsorptionAmount();
            text += " " + getHealthColor(health) + (int)(health);
        }

        return text;
    }

    private void drawArmor(EntityPlayer player) {
        int y = -(mc.fontRenderer.FONT_HEIGHT * 3);
        if (this.armorStyle.getValue() == ArmorEnum.Shown) {
            drawShownArmor(player, y);
        } else if (this.armorStyle.getValue() == ArmorEnum.Both) {
            drawDurabilityArmor(player, y - mc.fontRenderer.FONT_HEIGHT);
        }
    }

    private void drawShownArmor(EntityPlayer player, int y) {
        int x = -30;
        for (ItemStack armorPiece : Lists.reverse(player.inventory.armorInventory)) {
            if (armorPiece.isEmpty()) {
                x += 15;
                continue;
            }
            RenderUtil.renderItem(armorPiece, x, y);
            x += 15;
        }
    }

    private void drawDurabilityArmor(EntityPlayer player, int y) {

        int x = -40;
        for (ItemStack armorPiece : Lists.reverse(player.inventory.armorInventory)) {
            if (armorPiece.isEmpty()) {
                x += 20;
                continue;
            }
            RenderUtil.renderItem(armorPiece, x, y);
            mc.fontRenderer.drawStringWithShadow(String.valueOf(armorPiece.getMaxDamage() - armorPiece.getItemDamage()), x, y + mc.fontRenderer.FONT_HEIGHT * 2, -1);
            x += 20;
        }


    }

    @SubscribeEvent
    public void onRender(RenderPlayerNameEvent event) {
        event.setCanceled(true);
    }

    public enum ArmorEnum {
        Hidden,
        Shown,
        Durability,
        Both
    }
}