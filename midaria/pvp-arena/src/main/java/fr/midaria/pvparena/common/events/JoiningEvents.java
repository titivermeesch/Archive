package fr.midaria.pvparena.common.events;

import com.j256.ormlite.dao.Dao;
import fr.midaria.pvparena.Main;
import fr.midaria.pvparena.common.database.models.PlayerPvpStat;
import fr.midaria.pvparena.common.database.models.PvpMigration;
import fr.midaria.pvparena.common.utils.KitsUtility;
import fr.midaria.pvparena.stats.PvpStat;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class JoiningEvents implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) throws SQLException {
        Player p = e.getPlayer();
        p.getInventory().clear();
        p.performCommand("spawn");
        Location loc = Bukkit.getWorld("world").getSpawnLocation();
        loc.setYaw(-90);
        loc.setPitch(0);
        p.teleport(loc);
        p.setHealth(20);
        p.setFoodLevel(20);
        KitsUtility.loadKitOnPlayer(p);

        Dao<PlayerPvpStat, String> pvpStatDao = Main.getInstance().getDb().getPvpStatDao();
        Dao<PvpMigration, String> pvpMigrationDao = Main.getInstance().getDb().getPvpMigrationDao();

        try {
            List<PvpMigration> existingMigration = pvpMigrationDao.queryForEq("playerUuid", p.getUniqueId());
            if (existingMigration.size() > 0) {
                return;
            }
            PvpMigration pvpMigration = new PvpMigration(p.getUniqueId());
            int kills = p.getStatistic(Statistic.PLAYER_KILLS);
            int deaths = p.getStatistic(Statistic.DEATHS);

            List<PlayerPvpStat> stats = new ArrayList<>();
            for (int i = 0; i < kills; i++) {
                stats.add(new PlayerPvpStat(p.getUniqueId(), new UUID(0, 0), new Date(), "pvparena"));
            }

            for (int i = 0; i < deaths; i++) {
                stats.add(new PlayerPvpStat(new UUID(0, 0), p.getUniqueId(), new Date(), "pvparena"));
            }

            Main.getInstance().getStatsManager().getPvpStats().put(p.getUniqueId(), new PvpStat(kills, deaths));

            pvpStatDao.create(stats);
            pvpMigrationDao.create(pvpMigration);

            pvpStatDao.getConnectionSource().close();
            pvpMigrationDao.getConnectionSource().close();
        } catch (SQLException err) {
            err.printStackTrace();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
