package me.gavin.calypso;

import me.gavin.calypso.command.CommandManager;
import me.gavin.calypso.events.processing.EventProcessor;
import me.gavin.calypso.gui.ClickGUI;
import me.gavin.calypso.gui.HUDEditor;
import me.gavin.calypso.misc.ConfigManager;
import me.gavin.calypso.misc.DiscordManager;
import me.gavin.calypso.module.ModuleManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

@Mod(
        modid = Calypso.MOD_ID,
        name = Calypso.MOD_NAME,
        version = Calypso.VERSION,
        clientSideOnly = true
)
public class Calypso {

    public static final String MOD_ID = "calypso";
    public static final String MOD_NAME = "Calypso";
    public static final String VERSION = "1.0";

    @Mod.Instance(MOD_ID)
    public static Calypso INSTANCE;

    private Logger logger;

    private ModuleManager moduleManager;

    private EventProcessor eventProcessor;

    private DiscordManager discordManager;

    private ClickGUI clickGUI;

    private HUDEditor hudEditor;

    private CommandManager commandManager;

    private ConfigManager configManager;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        logger = LogManager.getLogger(MOD_ID);

        logger.info("   _____      _                       ");
        logger.info("  / ____|    | |                      ");
        logger.info(" | |     __ _| |_   _ _ __  ___  ___  ");
        logger.info(" | |    / _` | | | | | '_ \\/ __|/ _ \\ ");
        logger.info(" | |___| (_| | | |_| | |_) \\__ \\ (_) |");
        logger.info("  \\_____\\__,_|_|\\__, | .__/|___/\\___/");
        logger.info("                 __/ | |");
        logger.info("                |___/|_|");

        logger.info("Starting " + MOD_NAME + " v" + VERSION);

        moduleManager = new ModuleManager();
        logger.info("Modules initialized");

        eventProcessor = new EventProcessor();
        logger.info("Events initialized");

        discordManager = new DiscordManager();
        logger.info("Discord RPC initialized");

        clickGUI = new ClickGUI();
        logger.info("Click GUI initialized");

        hudEditor = new HUDEditor();
        logger.info("HUD editor initialized");

        configManager = new ConfigManager();
        logger.info("Configs initialized");

        commandManager = new CommandManager();
        logger.info("Commands initialized");

        logger.info(MOD_NAME + " completed initialization");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        Display.setTitle(MOD_NAME + " " + VERSION + " - 1.12.2");
        moduleManager.sortModules();

        try {
            configManager.load(configManager.getDefaultConfig());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                configManager.save(configManager.getDefaultConfig());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public DiscordManager getDiscordManager() {
        return discordManager;
    }

    public ClickGUI getClickGUI() {
        return clickGUI;
    }

    public HUDEditor getHudEditor() {
        return hudEditor;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}