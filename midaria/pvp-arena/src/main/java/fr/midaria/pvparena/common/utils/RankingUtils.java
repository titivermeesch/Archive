package fr.midaria.pvparena.common.utils;

import org.bukkit.entity.Player;

public class RankingUtils {
    public static int getPlayerMultiplier(Player p) {
        if (p.hasPermission("group.staff") || p.isOp()) {
            return 1;
        }
        if (p.hasPermission("group.dieu")) {
            return 4;
        }
        if (p.hasPermission("group.roi")) {
            return 3;
        }
        if (p.hasPermission("group.noble")) {
            return 2;
        }
        if (p.hasPermission("group.chevalier")) {
            return 2;
        }

        return 1;
    }
}
