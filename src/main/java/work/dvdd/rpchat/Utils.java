package work.dvdd.rpchat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
/**
By realdivided
**/
public final class Utils {
    private static final Random RANDOM = new Random();

    public static String extractMessage(String... message) {
        return String.join(" ", message);
    }

    public static List<Player> getNearbyPlayers(Player player, int range) {
        return Bukkit.getOnlinePlayers().parallelStream()
                .filter(p -> p.getWorld().equals(player.getWorld()) && p.getLocation().distanceSquared(player.getLocation()) <= range * range)
                .collect(Collectors.toList());
    }

    public static int getRandom(int max) {
        return RANDOM.nextInt(max);
    }

}