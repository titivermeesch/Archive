package me.playbosswar.fireball.game;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinListeners implements Listener {
    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        GameManager.getInstance().removePlayer(e.getPlayer().getUniqueId());
    }
}
