package me.gavin.calypso.command;

import me.gavin.calypso.command.cmds.BindCommand;
import me.gavin.calypso.command.cmds.ConfigCommand;
import me.gavin.calypso.command.cmds.PrefixCommand;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Arrays;

public class CommandManager {

    public String prefix;
    private final Minecraft mc;
    private final ArrayList<CommandContainer> commands;

    public CommandManager() {
        MinecraftForge.EVENT_BUS.register(this);
        prefix = "$";
        mc = Minecraft.getMinecraft();
        commands = new ArrayList<>();
        registerCommands();
    }

    private void registerCommands() {
        addCommand(new BindCommand());
        addCommand(new ConfigCommand());
        addCommand(new PrefixCommand());
    }

    private void processCommand(String name, String[] arguments) {
        for (CommandContainer command : commands) {
            if (command.getName().equalsIgnoreCase(name) || command.getAliases().contains(name.toLowerCase())) {
                command.execute(arguments);
                return;
            }
        }


    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onChat(ClientChatEvent event) {
        if (event.getMessage().startsWith(prefix)) {
            event.setCanceled(true);
            mc.ingameGUI.getChatGUI().addToSentMessages(event.getMessage());
            processCommand(processName(event.getMessage()), processArguments(event.getMessage()));
        }
    }

    private String processName(String message) {
        return message.substring(prefix.length()).split(" ")[0];
    }

    private String[] processArguments(String message) {
        return Arrays.copyOfRange(message.split(" "), 1, message.split(" ").length);
    }

    private void addCommand(CommandExecutor executor) {
        this.commands.add(new CommandContainer(executor));
    }
}