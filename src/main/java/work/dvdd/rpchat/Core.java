package work.dvdd.rpchat;

import org.bukkit.plugin.java.JavaPlugin;
import work.dvdd.rpchat.commands.*;
import org.bukkit.Bukkit;
/**
 By realdivided
 **/
public final class Core extends JavaPlugin {
    private static Core instance;
    private CommandManager commandManager;

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage("§6rpchat §f| §aSuccessfully enabled!");
        Bukkit.getConsoleSender().sendMessage("§6rpchat §f| §aBy: §fgithub.com/realdivided/rpchat!");
        Bukkit.getConsoleSender().sendMessage("");
        instance = this;

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        commandManager = new CommandManager(this);
        commandManager
                .register(new MeCommand())
                .register(new GlobalMeCommand())
                .register(new TryCommand())
                .register(new DoCommand())
                .register(new CoinCommand())
                .register(new WhisperCommand())
                .register(new DiceCommand());

    }

    @Override
    public void onDisable() {
        commandManager.unregisterAll();
    }

    public static Core getInstance() {
        return instance;
    }
}

