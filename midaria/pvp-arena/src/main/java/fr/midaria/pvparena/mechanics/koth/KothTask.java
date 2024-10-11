package fr.midaria.pvparena.mechanics.koth;

import fr.midaria.pvparena.Main;
import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import me.filoghost.holographicdisplays.api.hologram.Hologram;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

public class KothTask extends BukkitRunnable implements Runnable {

    private final KothInstance kothInstance;
    private final Hologram hologram;

    public KothTask(KothInstance kothInstance) {
        this.kothInstance = kothInstance;

        Main main = Main.getInstance();
        Location location = new Location(Bukkit.getWorld("world"),
                main.getConfig().getDouble("kothleaderboard.x"),
                main.getConfig().getDouble("kothleaderboard.y") + 3,
                main.getConfig().getDouble("kothleaderboard.z"));

        HolographicDisplaysAPI api = HolographicDisplaysAPI.get(main);
        this.hologram = api.createHologram(location);
        this.hologram.getLines().insertText(0, "§6KOTH Leaderboard");
    }

    @Override
    public void run() {

        if (kothInstance.getLeaderboard().isEmpty()) {
            System.out.println("Leaderboard is empty");
            stop();
            return;
        }

        Map<Player, Integer> leaderboard = kothInstance.getLeaderboard();

        Player[] topPlayers = leaderboard.keySet().stream()
                .sorted((p1, p2) -> leaderboard.get(p2) - leaderboard.get(p1))
                .limit(10)
                .toArray(Player[]::new);

        hologram.getLines().clear();
        hologram.getLines().insertText(0, "§6KOTH Leaderboard");

        for (int i = 1; i <= topPlayers.length; i++) {
            Player p = topPlayers[i-1];
            int score = leaderboard.get(p);
            hologram.getLines().insertText(i, "§6" + i + ". §e" + p.getName() + " §7- §e" + score + " points");
        }
    }

    private void stop() {
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                hologram.delete();
                Bukkit.getScheduler().cancelTask(getTaskId());
            }
        }, 300L);
    }
}
