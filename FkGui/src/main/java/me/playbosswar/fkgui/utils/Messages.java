package me.playbosswar.fkgui.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Messages {
    public static void sendMessage(Player p, String message) {
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }
}
