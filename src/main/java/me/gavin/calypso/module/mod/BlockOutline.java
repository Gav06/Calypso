package me.gavin.calypso.module.mod;

import me.gavin.calypso.misc.RenderUtil;
import me.gavin.calypso.module.ModCategory;
import me.gavin.calypso.module.Module;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BlockOutline extends Module {

    public BlockOutline() {
        super("BlockOutline", "Outline block", ModCategory.Visual);
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        RayTraceResult raytrace = mc.objectMouseOver;
        if (raytrace != null) {
            if (raytrace.typeOfHit == RayTraceResult.Type.BLOCK) {
                AxisAlignedBB highlightBox = mc.world.getBlockState(raytrace.getBlockPos())
                        .getSelectedBoundingBox(mc.world, raytrace.getBlockPos())
                        .offset(-mc.getRenderManager().viewerPosX, -mc.getRenderManager().viewerPosY, -mc.getRenderManager().viewerPosZ);

                RenderUtil.prepare();
                GlStateManager.glLineWidth(2.0f);
                RenderGlobal.renderFilledBox(highlightBox, 1f, 1f, 1f, 0.4f);
                RenderGlobal.drawBoundingBox(
                        highlightBox.minX,
                        highlightBox.minY,
                        highlightBox.minZ,
                        highlightBox.maxX,
                        highlightBox.maxY,
                        highlightBox.maxZ,
                        1f, 1f, 1f, 1f);
                RenderUtil.release();
            }
        }
    }
}
