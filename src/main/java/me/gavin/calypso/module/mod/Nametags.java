package me.gavin.calypso.module.mod;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.calypso.events.RenderPlayerNameEvent;
import me.gavin.calypso.misc.ProjectionUtil;
import me.gavin.calypso.module.ModCategory;
import me.gavin.calypso.module.Module;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Nametags extends Module {

    private final ICamera camera = new Frustum();

    public Nametags() {
        super("NameTags", "Better player nametags", ModCategory.Visual);
    }

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

            final ChatFormatting crouch = player.isSneaking() ? ChatFormatting.GOLD : ChatFormatting.WHITE;
            final double health = player.getHealth() + player.getAbsorptionAmount();
            GlStateManager.pushMatrix();
            GlStateManager.translate(projection.x, projection.y, 0);
            final String s = crouch + player.getName() + " " + getHealthColor(health) + (int)(health);
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

    @SubscribeEvent
    public void onRender(RenderPlayerNameEvent event) {
        event.setCanceled(true);
    }
}
