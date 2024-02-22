package work.dvdd.rpchat;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public final class Hider extends JavaPlugin implements Listener {

    private Scoreboard board;
    private Team team;

    private String message;

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        message = getConfig().getString("message");

        boardSettings();

        Bukkit.getPluginManager().registerEvents(this, this);

        if (!Bukkit.getOnlinePlayers().isEmpty())
            Bukkit.getOnlinePlayers().forEach(p -> hideName(p));

        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage("§6RealEngine §f| §aSuccessfully enabled");
        Bukkit.getConsoleSender().sendMessage("§6RealEngine §f| §aBy: §fhttps://github.com/realdivided");
        Bukkit.getConsoleSender().sendMessage("");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        hideName(e.getPlayer());
    }

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent e) {
        if (!(e.getRightClicked() instanceof Player)) return;

        e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR,
                TextComponent.fromLegacyText(message.replace("$name", ((Player) e.getRightClicked()).getDisplayName())));
    }

    private void hideName(Player p) {
        team.addEntry(p.getName());
        p.setScoreboard(board);
    }

    private void boardSettings() {
        board = Bukkit.getScoreboardManager().getNewScoreboard();
        board.registerNewTeam("rHideTags");

        team = board.getTeam("rHideTags");

        team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        team.setCanSeeFriendlyInvisibles(false);
    }
}
