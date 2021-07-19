package me.gavin.calypso.command.cmds;

import me.gavin.calypso.command.CommandExecutor;

public class PrefixCommand implements CommandExecutor {
    @Override
    public String getName() {
        return "prefix";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public String getSyntax() {
        return getPrefix() + getName() + " [desired prefix]";
    }

    @Override
    public boolean execute(String[] args) {
        if (args.length == 1) {
            calypso.getCommandManager().prefix = args[0];
            sendMessage("Set the command prefix to \"" + args[0] + "\"");
            return true;
        } else {
            sendSyntax();
            return false;
        }
    }
}