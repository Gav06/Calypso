package me.gavin.calypso.misc;

import com.google.gson.*;
import me.gavin.calypso.Calypso;
import me.gavin.calypso.gui.HUDComponent;
import me.gavin.calypso.gui.Panel;
import me.gavin.calypso.module.HUDModule;
import me.gavin.calypso.module.Module;
import me.gavin.calypso.settings.*;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;

public class ConfigManager {

    private final Minecraft mc;
    private final Calypso calypso;
    private final File saveDir;
    private final Gson gson;

    public boolean shouldSaveByDefault;
    private String defaultConfig;

    public ConfigManager() {
        this.mc = Minecraft.getMinecraft();
        this.calypso = Calypso.INSTANCE;
        this.saveDir = new File(mc.gameDir, Calypso.MOD_ID);
        if (!saveDir.exists())
            saveDir.mkdir();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.defaultConfig = "Config";
        shouldSaveByDefault = true;
    }

    public void load(String name) throws Exception {
        if (doesConfigExist(name)) {

            final JsonParser parser = new JsonParser();
            final File file = new File(saveDir, name + ".json");
            final Reader reader = new FileReader(file);

            final JsonObject parsedObject = parser.parse(reader).getAsJsonObject();

            final JsonArray modules = parsedObject.get("modules").getAsJsonArray();
            final JsonArray hudModules = parsedObject.get("hudmodules").getAsJsonArray();
            final JsonArray hudComponents = parsedObject.get("hudcomponents").getAsJsonArray();
            final JsonArray panels = parsedObject.get("panels").getAsJsonArray();
            final JsonObject miscObj = parsedObject.get("misc").getAsJsonObject();

            for (JsonElement ele : modules) {
                final JsonObject moduleObj = ele.getAsJsonObject();
                final Module module = calypso.getModuleManager().getModule(moduleObj.get("name").getAsString());
                jsonToModule(moduleObj, module);
            }

            for (JsonElement ele : hudModules) {
                final JsonObject hudModuleObj = ele.getAsJsonObject();
                final Module hudModule = calypso.getHudEditor().getHudModuleByName(hudModuleObj.get("name").getAsString());
                jsonToModule(hudModuleObj, hudModule);
            }

            for (JsonElement ele : hudComponents) {
                final JsonObject hudCompObj = ele.getAsJsonObject();
                final HUDComponent hudComponent = calypso.getHudEditor().getHudComponent(hudCompObj.get("name").getAsString());
                if (hudComponent != null) {
                    hudComponent.setX(hudCompObj.get("x").getAsInt());
                    hudComponent.setY(hudCompObj.get("y").getAsInt());
                }
            }

            for (JsonElement ele : panels) {
                final JsonObject panelObj = ele.getAsJsonObject();
                Panel panel;
                final String panelName = panelObj.get("name").getAsString();
                if (panelName.equals("HUD")) {
                    panel = calypso.getHudEditor().getPanel();
                } else {
                    panel = calypso.getClickGUI().getPanelByName(panelName);
                }

                panel.setX(panelObj.get("x").getAsInt());
                panel.setY(panelObj.get("y").getAsInt());
                panel.setOpen(panelObj.get("open").getAsBoolean());
            }

            {
                calypso.getCommandManager().prefix = miscObj.get("commandPrefix").getAsString();
            }
        }
    }

    public void save(String name) throws Exception {
        final JsonObject saveObject = new JsonObject();

        // modules
        final JsonArray modulesArray = new JsonArray();

        for (Module module : calypso.getModuleManager().getModules()) {
            final JsonObject obj = new JsonObject();
            moduleToJson(obj, module);
            modulesArray.add(obj);
        }

        saveObject.add("modules", modulesArray);

        // hud modules
        final JsonArray hudModulesArray = new JsonArray();

        for (Module module : calypso.getHudEditor().getHudModules()) {
            final JsonObject obj = new JsonObject();
            moduleToJson(obj, module);
            hudModulesArray.add(obj);
        }

        saveObject.add("hudmodules", hudModulesArray);

        // hud components
        final JsonArray hudComponentsArray = new JsonArray();

        for (Module module : calypso.getHudEditor().getHudModules()) {
            final HUDModule hudModule = (HUDModule) module;
            final HUDComponent component = hudModule.getComponent();
            final JsonObject componentObj = new JsonObject();

            componentObj.addProperty("x", component.getX());
            componentObj.addProperty("y", component.getY());
            componentObj.addProperty("name", hudModule.getName());

            hudComponentsArray.add(componentObj);
        }

        saveObject.add("hudcomponents", hudComponentsArray);

        // panels
        final JsonArray panelsArray = new JsonArray();

        {
            for (Panel panel : calypso.getClickGUI().getPanels()) {
                final JsonObject obj = new JsonObject();
                panelToJson(obj, panel);
                panelsArray.add(obj);
            }

            final Panel hudPanel = calypso.getHudEditor().getPanel();
            final JsonObject obj = new JsonObject();
            panelToJson(obj, hudPanel);
            panelsArray.add(obj);
        }

        saveObject.add("panels", panelsArray);

        // misc
        final JsonObject miscObject = new JsonObject();

        miscObject.addProperty("commandPrefix", calypso.getCommandManager().prefix);

        saveObject.add("misc", miscObject);

        final File configFile = new File(saveDir, name + ".json");
        if (!configFile.exists())
            configFile.createNewFile();
        final FileWriter writer = new FileWriter(configFile);
        gson.toJson(saveObject, writer);
        writer.close();
    }

    public boolean doesConfigExist(String name) {
        return new File(saveDir, name + ".json").exists();
    }

    public String getDefaultConfig() {
        return defaultConfig;
    }

    public void setDefaultConfig(String name) {
        this.defaultConfig = name;
    }

    private void moduleToJson(JsonObject moduleObj, Module module) {
        moduleObj.addProperty("name", module.getName());
        moduleObj.addProperty("keybind", module.getKeybind().getBind());
        moduleObj.addProperty("visible", module.visibleSetting.getValue());
        moduleObj.addProperty("enabled", module.isEnabled());

        for (AbstractSetting setting : module.getSettings()) {
            if (setting instanceof NumSetting) {
                moduleObj.addProperty(setting.getName(), ((NumSetting)setting).getValue());
            } else if (setting instanceof EnumSetting) {
                moduleObj.addProperty(setting.getName(), EnumIndexer.getEnumIndex(((EnumSetting)setting).getValue()));
            } else if (setting instanceof BoolSetting) {
                moduleObj.addProperty(setting.getName(), ((BoolSetting)setting).getValue());
            }
        }

    }

    private void panelToJson(JsonObject panelObj, Panel panel) {
        panelObj.addProperty("name", panel.getCategory().name());
        panelObj.addProperty("x", panel.getX());
        panelObj.addProperty("y", panel.getY());
        panelObj.addProperty("open", panel.isOpen());
    }

    private void jsonToModule(JsonObject moduleObj, Module module) {
        final JsonElement bind = moduleObj.get("keybind");
        if (bind != null && bind.isJsonPrimitive())
            module
                    .keybind.setBind(bind.getAsInt());

        module.visibleSetting.setValue(moduleObj.get("visible").getAsBoolean());
        if (moduleObj.get("enabled").getAsBoolean())
            module.enable();

        for (AbstractSetting setting : module.getSettings()) {
            if (setting instanceof NumSetting) {
                ((NumSetting)setting).setValue(moduleObj.get(setting.getName()).getAsFloat());
            } else if (setting instanceof BoolSetting) {
                ((BoolSetting)setting).setValue(moduleObj.get(setting.getName()).getAsBoolean());
            } else if (setting instanceof EnumSetting) {
                final EnumSetting enumSetting = (EnumSetting)setting;
                enumSetting.setValue(EnumIndexer.getEnumValue(moduleObj.get(setting.getName()).getAsInt(), enumSetting.getValue()));
            }
        }
    }

    public File getSaveDir() {
        return saveDir;
    }

    public boolean tryDeleteConfig(String name) {
        final File configFile = new File(saveDir, name + ".json");
        if (configFile.exists()) {
            configFile.delete();
            return true;
        } else {
            return false;
        }
    }
}