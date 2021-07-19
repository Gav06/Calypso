package me.gavin.calypso.module;

import me.gavin.calypso.Calypso;
import me.gavin.calypso.settings.AbstractSetting;
import me.gavin.calypso.settings.BoolSetting;
import me.gavin.calypso.settings.KeybindSetting;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public abstract class Module {

    protected final Minecraft mc;
    protected final Calypso calypso;

    private boolean enabled;

    public final KeybindSetting keybind;

    private final String name;
    private final String description;
    private final ModCategory category;

    protected final ArrayList<AbstractSetting> settings = new ArrayList<>();

    public BoolSetting visibleSetting = new BoolSetting("Visible", true);

    public Module(String name, String description, ModCategory category) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.mc = Minecraft.getMinecraft();
        this.calypso = Calypso.INSTANCE;
        this.keybind = new KeybindSetting("Bind", Keyboard.KEY_NONE);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ModCategory getCategory() {
        return category;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public ArrayList<AbstractSetting> getSettings() {
        return settings;
    }

    public void enable() {
        enabled = true;
        MinecraftForge.EVENT_BUS.register(this);
        onEnable();
    }

    public void disable() {
        enabled = false;
        MinecraftForge.EVENT_BUS.unregister(this);
        onDisable();
    }

    public void toggle() {
        if (enabled) {
            disable();
        } else {
            enable();
        }
    }

    protected void onEnable() { }

    protected void onDisable() { }

    public KeybindSetting getKeybind() {
        return keybind;
    }

    public void setKeybind(int keybind) {
        this.keybind.setBind(keybind);
    }

}
