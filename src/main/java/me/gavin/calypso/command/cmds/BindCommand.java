package me.gavin.calypso.command.cmds;

import me.gavin.calypso.command.CommandExecutor;
import me.gavin.calypso.module.Module;
import org.lwjgl.input.Keyboard;

public class BindCommand implements CommandExecutor {

    @Override
    public String getName() {
        return "bind";
    }

    @Override
    public String[] getAliases() {
        return new String[] {"b"};
    }

    @Override
    public String getSyntax() {
        return getPrefix() + getName() + " [module] [keybind]";
    }


    @Override
    public boolean execute(String[] args) {
        if (args.length == 2) {
            final Module module = calypso.getModuleManager().getModule(args[0]);
            final int keycode = Keyboard.getKeyIndex(args[1].toUpperCase());

            if (module == null) {
                sendMessage("Could not find the specified module");
                return false;
            } else {
                module.setKeybind(keycode);
                sendMessage("Bound " + module.getName() + " to " + Keyboard.getKeyName(keycode));
                return true;
            }
        } else {
            sendSyntax();
            return false;
        }
    }
}