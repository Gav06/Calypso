package me.gavin.calypso.command.cmds;

import me.gavin.calypso.command.CommandExecutor;
import me.gavin.calypso.misc.ConfigManager;

public class ConfigCommand implements CommandExecutor {

    private final ConfigManager configManager = calypso.getConfigManager();

    @Override
    public String getName() {
        return "config";
    }

    @Override
    public String[] getAliases() {
        return new String[] {"conf", "cfg"};
    }

    @Override
    public String getSyntax() {
        return getPrefix() + getName() + " [save|load|delete] [config name]";
    }

    @Override
    public boolean execute(String[] args) {
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("save")) {
                try {
                    configManager.save(args[1]);
                    sendMessage("Saved config \"" + args[1] + "\" successfully");
                    return true;
                } catch (Exception e) {
                    sendMessage("Failed to load config \"" + args[1] + "\", reason: " + e.getMessage());
                    return false;
                }
            } else if (args[0].equalsIgnoreCase("load")) {
                final boolean doesExist = configManager.doesConfigExist(args[1]);
                if (doesExist) {
                    try {
                        configManager.load(args[1]);
                        sendMessage("Loaded config \"" + args[1] + "\" successfully");
                        return true;
                    } catch (Exception e) {
                        sendMessage("Failed to load config \"" + args[1] + "\", reason: " + e.getMessage());
                        return false;
                    }
                } else {
                    sendMessage("Could not find the config \"" + args[1] + "\" to load");
                    return false;
                }
            } else if (args[0].equalsIgnoreCase("delete")) {
                final boolean result = configManager.tryDeleteConfig(args[1]);
                if (result) {
                    sendMessage("Deleted config \"" + args[1] + "\" successfully");
                    return true;
                } else {
                    sendMessage("Could not find the config \"" + args[1] + "\" to delete");
                    return false;
                }
            } else {
                sendSyntax();
                return false;
            }
        } else {
            sendSyntax();
            return false;
        }
    }
}