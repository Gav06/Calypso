package me.gavin.calypso.module;

import me.gavin.calypso.module.mod.*;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;

public class ModuleManager {

    private final ArrayList<Module> modules;

    private final ArrayList<Module> sortedModules;

    private final Minecraft mc;

    public ModuleManager() {
        modules = new ArrayList<>();
        sortedModules = new ArrayList<>();
        mc = Minecraft.getMinecraft();
        initModules();
        sortedModules.addAll(modules);
    }

    private void initModules() {
        modules.add(new MiddleClickXP());
        modules.add(new PacketMine());
        modules.add(new BlockOutline());
        modules.add(new NoWeather());
        modules.add(new FakePlayer());
        modules.add(new ChatSuffix());
        modules.add(new ClickGUIModule());
        modules.add(new HUDEditorModule());
        modules.add(new NoRender());
        modules.add(new NoFog());
        modules.add(new Fullbright());
    }

    public void sortModules() {
        modules.sort(this::sortAlphabetically);
        sortedModules.sort(this::sortLength);
    }

    private int sortAlphabetically(Module mod1, Module mod2) {
        return mod1.getName().compareTo(mod2.getName());
    }

    private int sortLength(Module mod1, Module mod2) {
        return Integer.compare(mc.fontRenderer.getStringWidth(mod2.getName()), mc.fontRenderer.getStringWidth(mod1.getName()));
    }

    public ArrayList<Module> getModules() {
        return modules;
    }

    public ArrayList<Module> getSortedModules() {
        return sortedModules;
    }

    public ArrayList<Module> getModulesFromCategory(ModCategory category) {
        final ArrayList<Module> list = new ArrayList<>();

        for (Module module : modules) {
            if (module.getCategory() == category)
                list.add(module);
        }

        return list;
    }

    public <F extends Module> F getModule(Class<F> clazz) {
        for (Module module : modules) {
            if (module.getClass() == clazz)
                return (F) module;
        }

        return null;
    }

    public <F extends Module> F getModule(String name) {
        for (Module module : modules) {
            if (module.getName().equalsIgnoreCase(name))
                return (F) module;
        }

        return null;
    }

    public ArrayList<Module> getEnabledModules() {
        final ArrayList<Module> list = new ArrayList<>();
        for (Module module : sortedModules) {
            if (module.isEnabled() && module.visibleSetting.getValue())
                list.add(module);
        }

        return list;
    }

    public boolean isModuleEnabled(String name) {
        return getModule(name).isEnabled();
    }

    public boolean isModuleEnabled(Class<? extends Module> clazz) {
        return getModule(clazz).isEnabled();
    }
}
