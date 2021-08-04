package me.gavin.calypso.mixin.common;

import me.gavin.calypso.events.ScreenshotEvent;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ScreenShotHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.File;

@Mixin(ScreenShotHelper.class)
public class MixinScreenShotHelper {

    @Redirect(method = "saveScreenshot(Ljava/io/File;IILnet/minecraft/client/shader/Framebuffer;)Lnet/minecraft/util/text/ITextComponent;", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ScreenShotHelper;saveScreenshot(Ljava/io/File;Ljava/lang/String;IILnet/minecraft/client/shader/Framebuffer;)Lnet/minecraft/util/text/ITextComponent;"))
    private static ITextComponent saveScreenshotInject(File gameDirectory, String screenshotName, int width, int height, Framebuffer buffer) {
        final ScreenshotEvent event = new ScreenshotEvent(gameDirectory, screenshotName, width, height, buffer);
        MinecraftForge.EVENT_BUS.post(event);
        if (!event.isCanceled()) {
            return ScreenShotHelper.saveScreenshot(gameDirectory, null, width, height, buffer);
        }
        return event.getTextComponent();
    }
}
