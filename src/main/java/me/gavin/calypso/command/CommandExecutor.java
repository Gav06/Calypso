package me.gavin.calypso.command;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.calypso.Calypso;
import me.gavin.calypso.misc.Util;

public interface CommandExecutor {

    Calypso calypso = Calypso.INSTANCE;

    String getName();

    String[] getAliases();

    String getSyntax();

    boolean execute(String[] args);

    default void sendMessage(String message) {
        Util.sendMessage(message);
    }

    default void sendSyntax() {
        sendMessage(ChatFormatting.GRAY + "Syntax: " + getSyntax());
    }

    default String getPrefix() {
        return calypso.getCommandManager().prefix;
    }
}
