package work.dvdd.rpchat.commands;

import com.google.common.collect.Sets;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.ReflectionUtils;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 By realdivided
 **/
public class CommandManager {

    private static CommandMap commandMap;
    private final JavaPlugin plugin;
    private final Set<Command> commands = Sets.newHashSet();

    public CommandManager(JavaPlugin plugin) {
        this.plugin = plugin;
        initializeCommandMap();
    }

    private void initializeCommandMap() {
        try {
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public CommandManager register(Command command) {
        if (!plugin.getConfig().getBoolean(String.format("commands.%s.enabled", command.getName()))) return this;

        commandMap.register(plugin.getName().toLowerCase(), command);
        commands.add(command);

        return this;
    }

    public void unregisterAll() {
        commands.forEach(this::unregister);
    }

    @SuppressWarnings("unchecked")
    private void unregister(Command command) {
        try {
            Field knownCommandsField = commandMap.getClass().getDeclaredField("knownCommands");
            knownCommandsField.setAccessible(true);

            Map<String, Command> knownCommands = (Map<String, Command>) knownCommandsField.get(commandMap);
            removeCommand(knownCommands, command.getLabel());
            command.getAliases().forEach(alias -> removeCommand(knownCommands, alias));

            knownCommandsField.set(commandMap, knownCommands);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void removeCommand(Map<String, Command> commands, String name) {
        commands.remove(name);
    }
}