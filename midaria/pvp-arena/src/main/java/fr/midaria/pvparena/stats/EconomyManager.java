package fr.midaria.pvparena.stats;

import fr.midaria.pvparena.Main;
import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import me.filoghost.holographicdisplays.api.hologram.Hologram;
import me.filoghost.holographicdisplays.api.hologram.line.HologramLine;
import me.filoghost.holographicdisplays.api.hologram.line.ItemHologramLine;
import me.filoghost.holographicdisplays.api.hologram.line.TextHologramLine;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.*;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

public class EconomyManager {
    private Hologram moneyHologram;

    public EconomyManager(Plugin plugin) {
        HolographicDisplaysAPI api = HolographicDisplaysAPI.get(plugin);
        FileConfiguration config = plugin.getConfig();
        int x = config.getInt("moneyleaderboard.x");
        int y = config.getInt("moneyleaderboard.y");
        int z = config.getInt("moneyleaderboard.z");
        this.moneyHologram = api.createHologram(new Location(Bukkit.getWorld("world"), x, y, z));
        reloadHologram();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new EconomyHologramUpdater(this), 0, 20);
    }

    public void moveHologram(Location loc) {
        this.moneyHologram.setPosition(loc);
    }

    private static <T> Comparator<T> compareIntReversed(ToDoubleFunction<? super T> keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return (Comparator<T> & Serializable)
                (c1, c2) -> Double.compare(keyExtractor.applyAsDouble(c2), keyExtractor.applyAsDouble(c1));
    }

    private List<Map.Entry<UUID, Double>> getTopBalances(int limit) {
        Map<UUID, Double> balances = new HashMap<>();
        for (OfflinePlayer p : Bukkit.getOfflinePlayers()) {
            double money = Main.getInstance().getEcon().getBalance(p);
            balances.put(p.getUniqueId(), money);
        }

        List<Map.Entry<UUID, Double>> sorted = balances
                .entrySet()
                .stream()
                .sorted(compareIntReversed(Map.Entry::getValue))
                .collect(Collectors.toList());
        return sorted.stream().limit(limit).collect(Collectors.toList());
    }

    private void setOrUpdateLine(int index, String text) {
        if (moneyHologram.getLines().size() <= index) {
            moneyHologram.getLines().insertText(index, text);
            return;
        }

        TextHologramLine textLine = (TextHologramLine) moneyHologram.getLines().get(index);
        textLine.setText(text);
    }

    private void setOrUpdateLine(int index, ItemStack item) {
        if (moneyHologram.getLines().size() <= index) {
            moneyHologram.getLines().insertItem(index, item);
            return;
        }

        ItemHologramLine itemLine = (ItemHologramLine) moneyHologram.getLines().get(index);
        itemLine.setItemStack(item);
    }

    public void reloadHologram() {
        setOrUpdateLine(0, "§5§lLES GROS RICHES");
        setOrUpdateLine(1, new ItemStack(Material.GOLD_INGOT));
        setOrUpdateLine(2, "");

        final int limit = 10;
        List<Map.Entry<UUID, Double>> topBalances = getTopBalances(limit);
        for (int i = 0; i < limit; i++) {
            if (topBalances.size() <= i) {
                setOrUpdateLine(i + 3, "§7???");
            } else {
                Map.Entry<UUID, Double> el = topBalances.get(i);
                DecimalFormat decimalFormatter = new DecimalFormat("#.##");
                setOrUpdateLine(i + 3, "§7" + (i + 1) + ". §d" + Bukkit.getOfflinePlayer(el.getKey()).getName() + ": $" + decimalFormatter.format(el.getValue()));
            }
        }
    }
}
