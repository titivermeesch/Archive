package fr.midaria.pvparena.common.events;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatEvents implements Listener {
    @EventHandler
    public void onPlayerPing(AsyncPlayerChatEvent e) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (e.getMessage().contains(p.getName())) {
                p.playSound(p.getLocation(), Sound.NOTE_PLING, 5, 2);
            }
        }
    }
}
