package fr.midaria.pvparena.papi;

import fr.midaria.pvparena.Main;
import fr.midaria.pvparena.utils.TimeUtils;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

import java.text.DecimalFormat;

public class PvpPlaceholders extends PlaceholderExpansion {
    private final Main plugin;

    public PvpPlaceholders(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getAuthor() {
        return "Midaria";
    }

    @Override
    public String getIdentifier() {
        return "midariapvp";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if (params.equalsIgnoreCase("next_koth")) {
            return TimeUtils.getTimeString(Main.getInstance().getKothManager().getNextKoth(), "mm'm'ss's'");
        }

        if (params.equalsIgnoreCase("next_chest")) {
            return TimeUtils.getTimeString(Main.getInstance().getRewardChestManager().getNextChest(), "mm'm'ss's'");
        }

        if (params.equalsIgnoreCase("kills")) {
            if (Main.getInstance().getStatsManager().getPvpStats().containsKey(player.getUniqueId())) {
                return Main.getInstance().getStatsManager().getPvpStats().get(player.getUniqueId()).getKills() + "";
            }

            return "0";
        }

        if (params.equalsIgnoreCase("deaths")) {
            if (Main.getInstance().getStatsManager().getPvpStats().containsKey(player.getUniqueId())) {
                return Main.getInstance().getStatsManager().getPvpStats().get(player.getUniqueId()).getDeaths() + "";
            }

            return "0";
        }

        if (params.equalsIgnoreCase("kd")) {
            if (!Main.getInstance().getStatsManager().getPvpStats().containsKey(player.getUniqueId())) {
                return "0";
            }

            float kills = Main.getInstance().getStatsManager().getPvpStats().get(player.getUniqueId()).getKills();
            float deaths = Main.getInstance().getStatsManager().getPvpStats().get(player.getUniqueId()).getDeaths();
            float ratio = kills / deaths;

            String color = "§a";
            if (ratio < 0.5) {
                color = "§c";
            } else if (ratio < 0.85) {
                color = "§6";
            }

            DecimalFormat decimalFormatter = new DecimalFormat("#.##");
            return color + decimalFormatter.format(ratio);
        }

        return null;
    }
}
