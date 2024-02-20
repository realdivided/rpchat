package work.dvdd.rpchat.commands;

import com.google.common.collect.Sets;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

public class CommandManager {

    private static CommandMap commandMap;

    private final Set<Command> commands = Sets.newHashSet();
    private final JavaPlugin plugin;

    public CommandManager(JavaPlugin plugin) {
        this.plugin = plugin;

        SimplePluginManager simpleCommandManager = (SimplePluginManager) Bukkit.getServer().getPluginManager();

        try {
            Field commandMapField = SimplePluginManager.class.getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            commandMap = (SimpleCommandMap) commandMapField.get(simpleCommandManager);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public CommandManager register(Command command) {
        if (!plugin.getConfig().getBoolean(String.format("commands.%s.enabled", command.getName()))) return this;

        command.register(commandMap);
        commandMap.register(plugin.getName().toLowerCase(), command);
        commands.add(command);

        return this;
    }

    public void unregisterAll() {
        commands.forEach(this::unregister);
    }

    private void unregister(Command command) {
        command.unregister(commandMap);

        try {
            Field knownCommands = SimpleCommandMap.class.getDeclaredField("knownCommands");
            knownCommands.setAccessible(true);

            Map<String, Command> commands = (Map<String, Command>) knownCommands.get(commandMap);
            removeCommand(commands, command.getLabel());
            command.getAliases().forEach(alias -> removeCommand(commands, alias));

            knownCommands.set(commandMap, commands);
            knownCommands.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void removeCommand(Map<String, Command> commands, String name) {
        commands.remove(name);
        commands.remove(plugin.getName().toLowerCase() + ":" + name);
    }

}