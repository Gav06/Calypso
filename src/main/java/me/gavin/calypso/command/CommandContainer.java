package me.gavin.calypso.command;

import scala.actors.threadpool.Arrays;

import java.util.List;

public class CommandContainer {

    private final String name;
    private final List<String> aliases;
    private final CommandExecutor executor;

    public CommandContainer(CommandExecutor executor) {
        this.name = executor.getName();
        this.aliases = Arrays.asList(executor.getAliases());
        this.executor = executor;
    }

    public String getName() {
        return name;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public void execute(String[] args) {
        executor.execute(args);
    }
}
