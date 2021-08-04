package me.gavin.calypso.module.mod;

import me.gavin.calypso.events.ScreenshotEvent;
import me.gavin.calypso.misc.Screenshot;
import me.gavin.calypso.misc.Util;
import me.gavin.calypso.module.ModCategory;
import me.gavin.calypso.module.Module;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ScreenShotHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.io.File;
import java.nio.IntBuffer;

public class BetterScreenshot extends Module {

    public BetterScreenshot() {
        super("BetterScreenshot", "Multithreads screenshots", ModCategory.Misc);
    }

    private IntBuffer pixelBuffer;
    private int[] pixels;

    @SubscribeEvent
    public void onScreenshot(ScreenshotEvent event) {
        event.setCanceled(true);
        event.setTextComponent(new TextComponentString(Util.PREFIX + "Saving screenshot..."));
        this.makeScreenshot(event.getDirectory(), event.getWidth(), event.getHeight(), event.getFramebuffer());
    }

    public void makeScreenshot(final File file, int framebufferTextureWidth, int framebufferTextureHeight, final Framebuffer framebuffer) {
        final File file2 = new File(file, "screenshots");
        file2.mkdir();
        if (OpenGlHelper.isFramebufferEnabled()) {
            framebufferTextureWidth = framebuffer.framebufferTextureWidth;
            framebufferTextureHeight = framebuffer.framebufferTextureHeight;
        }
        final int n = framebufferTextureWidth * framebufferTextureHeight;
        if (this.pixelBuffer == null || this.pixelBuffer.capacity() < n) {
            this.pixelBuffer = BufferUtils.createIntBuffer(n);
            this.pixels = new int[n];
        }
        GL11.glPixelStorei(3333, 1);
        GL11.glPixelStorei(3317, 1);
        this.pixelBuffer.clear();
        if (OpenGlHelper.isFramebufferEnabled()) {
            GlStateManager.bindTexture(framebuffer.framebufferTexture);
            GL11.glGetTexImage(3553, 0, 32993, 33639, this.pixelBuffer);
        }
        else {
            GL11.glReadPixels(0, 0, framebufferTextureWidth, framebufferTextureHeight, 32993, 33639, this.pixelBuffer);
        }
        this.pixelBuffer.get(this.pixels);
        new Thread(new Screenshot(framebufferTextureWidth, framebufferTextureHeight, this.pixels, mc.getFramebuffer(), file2), "Screenshot creation thread").start();
    }
}
