package me.gavin.calypso.module.mod;

import me.gavin.calypso.misc.RenderUtil;
import me.gavin.calypso.module.ModCategory;
import me.gavin.calypso.module.Module;
import me.gavin.calypso.settings.ColorSetting;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

public class BlockOutline extends Module {

    private final ColorSetting setting = new ColorSetting("Box Color", 255, 0, 0, 120);

    public BlockOutline() {
        super("BlockOutline", "Outline block", ModCategory.Visual);
        this.getSettings().add(setting);
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        RayTraceResult raytrace = mc.objectMouseOver;
        if (raytrace != null) {
            if (raytrace.typeOfHit == RayTraceResult.Type.BLOCK) {
                AxisAlignedBB highlightBox = mc.world.getBlockState(raytrace.getBlockPos())
                        .getSelectedBoundingBox(mc.world, raytrace.getBlockPos())
                        .offset(-mc.getRenderManager().viewerPosX, -mc.getRenderManager().viewerPosY, -mc.getRenderManager().viewerPosZ);
                final Color color = setting.getColor();
                final float r = color.getRed() / 255f;
                final float g = color.getGreen() / 255f;
                final float b = color.getBlue() / 255f;
                final float a = setting.getAlpha().getValue() / 255f;
                RenderUtil.prepare();
                GlStateManager.glLineWidth(2.0f);
                RenderGlobal.renderFilledBox(highlightBox, r, g, b, a);
                RenderGlobal.drawBoundingBox(
                        highlightBox.minX,
                        highlightBox.minY,
                        highlightBox.minZ,
                        highlightBox.maxX,
                        highlightBox.maxY,
                        highlightBox.maxZ,
                        r, g, b, 1f);
                RenderUtil.release();
            }
        }
    }
}
