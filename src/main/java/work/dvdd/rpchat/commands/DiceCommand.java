package work.dvdd.rpchat.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import work.dvdd.rpchat.Core;
import work.dvdd.rpchat.Utils;

import java.util.List;
import java.util.Random;
/**
 By realdivided
 **/
public class DiceCommand extends Command {

    private final ConfigurationSection config = Core.getInstance().getConfig().getConfigurationSection("commands.dice");
    private final List<String> results = config.getStringList("results");
    private final Random random = new Random();

    public DiceCommand() {
        super("dice");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) return true;

        if (args.length != 0) {
            sender.sendMessage(config.getString("usage"));
            return true;
        }


        String result = results.get(Utils.getRandom(results.size()));

        String message = config.getString("format")
                .replace("%sender%", sender.getName())
                .replace("%result%", result);

        Utils.getNearbyPlayers((Player) sender, config.getInt("radius"))
                .forEach(p -> p.sendMessage(message));
        return false;
    }

}