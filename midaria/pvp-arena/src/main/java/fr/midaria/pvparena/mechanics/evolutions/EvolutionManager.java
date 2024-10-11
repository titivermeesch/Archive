package fr.midaria.pvparena.mechanics.evolutions;

import com.j256.ormlite.dao.Dao;
import fr.midaria.pvparena.Main;
import fr.midaria.pvparena.common.database.models.EvolutionProgress;
import fr.midaria.pvparena.mechanics.evolutions.types.BonusGappleEvolution;
import fr.midaria.pvparena.mechanics.evolutions.types.BowUpgradeEvolution;
import fr.midaria.pvparena.mechanics.evolutions.types.SwordUpgradeEvolution;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class EvolutionManager implements Listener {
    private final List<EvolutionProgress> progress = new ArrayList<>();
    private final List<Evolution> evolutions = new ArrayList<>();

    public EvolutionManager(Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.evolutions.add(new SwordUpgradeEvolution(plugin, this));
        this.evolutions.add(new BonusGappleEvolution(plugin, this));
        this.evolutions.add(new BowUpgradeEvolution(plugin, this));
        // Make sure data is persisted when server reloads
        for (Player p : Bukkit.getOnlinePlayers()) {
            loadPlayerData(p);
        }
    }

    public int getPlayerEvolutionLevel(UUID playerUuid, EvolutionType evolutionType) {
        Optional<EvolutionProgress> evolutionProgress = progress
                .stream()
                .filter(p -> p.getPlayerUuid().equals(playerUuid) && p.getEvolutionType().equals(evolutionType))
                .findFirst();

        return evolutionProgress.map(EvolutionProgress::getLevel).orElse(0);
    }

    public Evolution getEvolution(EvolutionType evolutionType) {
        return evolutions.stream().filter(e -> e.getType().equals(evolutionType)).findFirst().orElse(null);
    }

    public EvolutionProgress getPlayerEvolution(UUID playerUuid, EvolutionType evolutionType) {
        return progress.stream().filter(e -> e.getPlayerUuid().equals(playerUuid) && e.getEvolutionType().equals(evolutionType)).findFirst().orElse(null);
    }

    public void upgradeEvolution(Player p, EvolutionType evolutionType, int level) {
        Evolution evolution = getEvolution(evolutionType);
        EvolutionLevel evolutionLevel = evolution.getLevels().get(level - 1);
        double playerBalance = Main.getInstance().getEcon().getBalance(p);

        if (evolutionLevel.getPrice() > playerBalance) {
            p.sendMessage("§cVous n'avez pas assez d'argent pour acheter cet article");
            p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 0);
            return;
        }

        try {
            EvolutionProgress playerProgress = getPlayerEvolution(p.getUniqueId(), evolutionType);
            Dao<EvolutionProgress, String> evolutionProgressDao = Main.getInstance().getDb().getEvolutionProgressDao();
            if (playerProgress == null) {
                EvolutionProgress newProgress = new EvolutionProgress(p.getUniqueId(), evolutionType, 0);
                evolutionProgressDao.create(newProgress);
                progress.add(newProgress);
                playerProgress = newProgress;
            }

            playerProgress.setLevel(level);
            evolutionProgressDao.update(playerProgress);
            Main.getInstance().getEcon().withdrawPlayer(p, evolutionLevel.getPrice());
            p.sendMessage("§7Nous avons débité §d" + evolutionLevel.getPrice() + "$ §7de votre compte");
            p.playSound(p.getLocation(), Sound.NOTE_PLING, 2, 2);
            evolutionProgressDao.getConnectionSource().close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            p.closeInventory();
        }
    }

    private void loadPlayerData(Player p) {
        try {
            Dao<EvolutionProgress, String> evolutionProgressDao = Main.getInstance().getDb().getEvolutionProgressDao();
            List<EvolutionProgress> progresses = evolutionProgressDao.queryForEq("playerUuid", p.getUniqueId());
            progress.addAll(progresses);
            evolutionProgressDao.getConnectionSource().close();
        } catch (SQLException err) {
            err.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        loadPlayerData(e.getPlayer());
    }
}
