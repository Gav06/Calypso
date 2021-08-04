package me.gavin.calypso.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.fml.common.FMLLog;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

// may or may not be from backdoored leak
// :flushed:
public class Screenshot implements Runnable
{
    private static final SimpleDateFormat dateFormat;
    private int width;
    private int height;
    private int[] pixels;
    private Framebuffer framebuffer;
    private File file;

    public Screenshot(final int width, final int height, final int[] pixels, final Framebuffer framebuffer, final File file) {
        super();
        this.width = width;
        this.height = height;
        this.pixels = pixels;
        this.framebuffer = framebuffer;
        this.file = file;
    }

    @Override
    public void run() {
        setupArray(this.pixels, this.width, this.height);
        try {
            BufferedImage bufferedImage;
            if (OpenGlHelper.isFramebufferEnabled()) {
                bufferedImage = new BufferedImage(this.framebuffer.framebufferWidth, this.framebuffer.framebufferHeight, 1);
                int n;
                for (int i = n = this.framebuffer.framebufferTextureHeight - this.framebuffer.framebufferHeight; i < this.framebuffer.framebufferTextureHeight; ++i) {
                    for (int j = 0; j < this.framebuffer.framebufferWidth; ++j) {
                        bufferedImage.setRGB(j, i - n, this.pixels[i * this.framebuffer.framebufferTextureWidth + j]);
                    }
                }
            }
            else {
                bufferedImage = new BufferedImage(this.width, this.height, 1);
                bufferedImage.setRGB(0, 0, this.width, this.height, this.pixels, 0, this.width);
            }
            final File a = saveFile(this.file);
            ImageIO.write(bufferedImage, "png", a);
            final TextComponentString textComponentString = new TextComponentString(Util.PREFIX + "Saved screenshot at: " + a.getName());
            ((ITextComponent)textComponentString).getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, a.getAbsolutePath()));
            ((ITextComponent)textComponentString).getStyle().setUnderlined(Boolean.TRUE);
            Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(textComponentString);
        }
        catch (Exception ex) {
            FMLLog.log.info("Failed to save screenshot");
            ex.printStackTrace();
            Util.sendMessage("Unable to save screenshot");
        }
    }

    private static void setupArray(final int[] array, final int n, final int n2) {
        final int[] array2 = new int[n];
        for (int n3 = n2 / 2, i = 0; i < n3; ++i) {
            System.arraycopy(array, i * n, array2, 0, n);
            System.arraycopy(array, (n2 - 1 - i) * n, array, i * n, n);
            System.arraycopy(array2, 0, array, (n2 - 1 - i) * n, n);
        }
    }

    private static File saveFile(final File file) {
        final String format = Screenshot.dateFormat.format(new Date());
        int n = 1;
        File file2;
        while (true) {
            file2 = new File(file, format + ((n == 1) ? "" : ("_" + n)) + ".png");
            if (!file2.exists()) {
                break;
            }
            ++n;
        }
        return file2;
    }

    static {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
    }
}