package me.gavin.calypso.gui;

import me.gavin.calypso.Calypso;
import me.gavin.calypso.module.mod.ClickGUIModule;

public abstract class SettingComponent extends Component implements Typeable {

    protected final ClickGUIModule clickGUIModule;

    public SettingComponent(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.clickGUIModule = Calypso.INSTANCE.getModuleManager().getModule(ClickGUIModule.class);
    }

    public abstract int getTotalHeight();
}