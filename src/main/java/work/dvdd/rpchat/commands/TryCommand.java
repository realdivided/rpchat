package work.dvdd.rpchat.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import work.dvdd.rpchat.Core;
import work.dvdd.rpchat.Utils;

import java.util.List;
import java.util.Random;

public class TryCommand extends Command {

    private final ConfigurationSection config = Core.getInstance().getConfig().getConfigurationSection("commands.try");
    private final List<String> results = config.getStringList("results");
    private final Random random = new Random();

    public TryCommand() {
        super("try");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) return true;

        if (args.length < 1) {
            sender.sendMessage(config.getString("usage"));
            return true;
        }

        String result = results.get(Utils.getRandom(results.size()));

        String message = config.getString("format")
                .replace("%sender%", sender.getName())
                .replace("%action%", Utils.extractMessage(args))
                .replace("%result%", result);

        Utils.getNearbyPlayers((Player) sender, config.getInt("radius"))
                .forEach(p -> p.sendMessage(message));
        return false;
    }

}