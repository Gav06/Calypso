package me.gavin.calypso.module.mod;

import me.gavin.calypso.module.ModCategory;
import me.gavin.calypso.module.Module;
import me.gavin.calypso.settings.BoolSetting;
import me.gavin.calypso.settings.NumSetting;
import org.lwjgl.input.Keyboard;

public class ClickGUIModule extends Module {

    public ClickGUIModule() {
        super("ClickGUI", ":)", ModCategory.Client);
        this.setKeybind(Keyboard.KEY_RSHIFT);
        this.getSettings().add(buttonBackground);
        this.getSettings().add(descriptions);
        this.getSettings().add(blur);
    }

    public final BoolSetting buttonBackground = new BoolSetting("Button Background", true);
    public final BoolSetting descriptions = new BoolSetting("Descriptions", true);
    public final BoolSetting blur = new BoolSetting("Blur", true);

    @Override
    public void onEnable() {
        mc.displayGuiScreen(calypso.getClickGUI());
        disable();
    }
}
