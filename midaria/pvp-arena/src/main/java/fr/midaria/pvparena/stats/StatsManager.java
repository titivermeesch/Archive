package fr.midaria.pvparena.stats;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.QueryBuilder;
import fr.midaria.pvparena.Main;
import fr.midaria.pvparena.common.database.models.PlayerPvpStat;
import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import me.filoghost.holographicdisplays.api.hologram.Hologram;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.*;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

public class StatsManager implements Listener {
    private final Map<UUID, PvpStat> pvpStats = new HashMap<>();
    private Dao<PlayerPvpStat, String> pvpStatsDao = null;
    private Hologram statsHologram;

    public StatsManager(Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        try {
            this.pvpStatsDao = Main.getInstance().getDb().getPvpStatDao();
            loadStats();
            HolographicDisplaysAPI api = HolographicDisplaysAPI.get(plugin);
            FileConfiguration config = plugin.getConfig();
            int x = config.getInt("pvpleaderboard.x");
            int y = config.getInt("pvpleaderboard.y");
            int z = config.getInt("pvpleaderboard.z");
            this.statsHologram = api.createHologram(new Location(Bukkit.getWorld("world"), x, y, z));
            reloadStatsHologram();
            Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new PvpHologramUpdater(this), 0, 20);
        } catch (SQLException e) {
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(plugin);
        }
    }

    public void moveHologram(Location loc) {
        this.statsHologram.setPosition(loc);
    }

    private void loadStats() {
        final Map<UUID, Integer> kills = new HashMap<>();
        final Map<UUID, Integer> deaths = new HashMap<>();

        try {
            // Load all kills
            QueryBuilder<PlayerPvpStat, String> qb = pvpStatsDao.queryBuilder();
            qb.selectRaw("killerUuid", "count(*)");
            qb.where().eq("game", "pvparena");
            qb.where().not().eq("killerUuid", new UUID(0, 0));
            qb.groupBy("killerUuid");
            GenericRawResults<String[]> rawResults = pvpStatsDao.queryRaw(qb.prepareStatementString());
            for (String[] resultArray : rawResults) {
                kills.put(UUID.fromString(resultArray[0]), Integer.parseInt(resultArray[1]));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            // Load all deaths
            QueryBuilder<PlayerPvpStat, String> qb = pvpStatsDao.queryBuilder();
            qb.selectRaw("victimUuid", "count(*)");
            qb.where().eq("game", "pvparena");
            qb.where().not().eq("victimUuid", new UUID(0, 0));
            qb.groupBy("victimUuid");
            GenericRawResults<String[]> rawResults = pvpStatsDao.queryRaw(qb.prepareStatementString());
            for (String[] resultArray : rawResults) {
                deaths.put(UUID.fromString(resultArray[0]), Integer.parseInt(resultArray[1]));
            }
            pvpStatsDao.getConnectionSource().close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<UUID> uniquePlayers = new ArrayList<>();
        kills.keySet().forEach(killerUuid -> {
            if (!uniquePlayers.contains(killerUuid)) {
                uniquePlayers.add(killerUuid);
            }
        });
        deaths.keySet().forEach(victimUuid -> {
            if (!uniquePlayers.contains(victimUuid)) {
                uniquePlayers.add(victimUuid);
            }
        });

        uniquePlayers.forEach(p -> {
            int pKills = 0;
            int pDeaths = 0;

            if (kills.containsKey(p)) {
                pKills = kills.get(p);
            }
            if (deaths.containsKey(p)) {
                pDeaths = deaths.get(p);
            }

            pvpStats.put(p, new PvpStat(pKills, pDeaths));
        });
    }

    public void reloadStatsHologram() {
        statsHologram.getLines().clear();
        statsHologram.getLines().appendText("§5§lTOP KILLS PVP");
        statsHologram.getLines().appendItem(new ItemStack(Material.DIAMOND_SWORD));
        statsHologram.getLines().appendText("");

        final int limit = 10;
        List<Map.Entry<UUID, PvpStat>> topKills = getTopKills(limit);
        for (int i = 0; i < limit; i++) {
            if (topKills.size() <= i) {
                statsHologram.getLines().appendText("§7???");
            } else {
                Map.Entry<UUID, PvpStat> el = topKills.get(i);
                statsHologram.getLines().appendText("§7" + (i + 1) + ". §d" + Bukkit.getOfflinePlayer(el.getKey()).getName() + ": " + pvpStats.get(el.getKey()).getKills() + " kills");
            }
        }
    }

    private static <T> Comparator<T> compareIntReversed(ToIntFunction<? super T> keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return (Comparator<T> & Serializable)
                (c1, c2) -> Integer.compare(keyExtractor.applyAsInt(c2), keyExtractor.applyAsInt(c1));
    }

    private List<Map.Entry<UUID, PvpStat>> getTopKills(int limit) {
        List<Map.Entry<UUID, PvpStat>> sorted = pvpStats
                .entrySet()
                .stream()
                .sorted(compareIntReversed(e -> e.getValue().getKills()))
                .collect(Collectors.toList());
        return sorted.stream().limit(limit).collect(Collectors.toList());
    }

    @EventHandler
    public void onKill(PlayerDeathEvent e) {
        Player victim = e.getEntity().getPlayer();
        Player killer = e.getEntity().getKiller();

        if (killer == null) {
            return;
        }

        if (pvpStats.containsKey(killer.getUniqueId())) {
            pvpStats.get(killer.getUniqueId()).addKill();
        } else {
            pvpStats.put(killer.getUniqueId(), new PvpStat(1, 0));
        }

        if (pvpStats.containsKey(victim.getUniqueId())) {
            pvpStats.get(victim.getUniqueId()).addDeath();
        } else {
            pvpStats.put(victim.getUniqueId(), new PvpStat(0, 1));
        }

        try {
            PlayerPvpStat pvpStat = new PlayerPvpStat(killer.getUniqueId(), victim.getUniqueId(), new Date(), "pvparena");
            pvpStatsDao.create(pvpStat);
            pvpStatsDao.getConnectionSource().close();
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public Map<UUID, PvpStat> getPvpStats() {
        return pvpStats;
    }
}
