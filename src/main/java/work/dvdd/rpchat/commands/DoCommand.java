package work.dvdd.rpchat.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import work.dvdd.rpchat.Core;
import work.dvdd.rpchat.Utils;

public class DoCommand extends Command {

    private final ConfigurationSection config = Core.getInstance().getConfig().getConfigurationSection("commands.do");

    public DoCommand() {
        super("do");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) return true;

        if (args.length < 1) {
            sender.sendMessage(config.getString("usage"));
            return true;
        }

        String message = config.getString("format")
                .replace("%sender%", sender.getName())
                .replace("%action%", Utils.extractMessage(args));

        Utils.getNearbyPlayers((Player) sender, config.getInt("radius"))
                .forEach(p -> p.sendMessage(message));
        return false;
    }

}