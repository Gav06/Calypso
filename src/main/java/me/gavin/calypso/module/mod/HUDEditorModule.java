package me.gavin.calypso.module.mod;

import me.gavin.calypso.module.ModCategory;
import me.gavin.calypso.module.Module;
import org.lwjgl.input.Keyboard;

public class HUDEditorModule extends Module {

    public HUDEditorModule() {
        super("HUDEdtior", "Edit your hud", ModCategory.Client);
        this.setKeybind(Keyboard.KEY_GRAVE);
    }

    @Override
    protected void onEnable() {
        mc.displayGuiScreen(calypso.getHudEditor());
        disable();
    }
}
