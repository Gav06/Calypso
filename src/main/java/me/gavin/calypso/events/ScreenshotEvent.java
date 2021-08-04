package me.gavin.calypso.events;

import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.io.File;

@Cancelable
public class ScreenshotEvent extends Event {

    private final File directory;
    private final String name;
    private final int width;
    private final int height;
    private final Framebuffer framebuffer;

    private ITextComponent textComponent;

    public ScreenshotEvent(File directory, String name, int width, int height, Framebuffer framebuffer) {
        this.directory = directory;
        this.name = name;
        this.width = width;
        this.height = height;
        this.framebuffer = framebuffer;
    }

    public File getDirectory() {
        return directory;
    }

    public String getName() {
        return name;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Framebuffer getFramebuffer() {
        return framebuffer;
    }

    public ITextComponent getTextComponent() {
        return textComponent;
    }

    public void setTextComponent(ITextComponent component) {
        this.textComponent = component;
    }
}
