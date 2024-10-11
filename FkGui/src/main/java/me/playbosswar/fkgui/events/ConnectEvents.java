package me.playbosswar.fkgui.events;

import me.playbosswar.fkgui.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectEvents implements Listener {
    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Main.getInstance().getConfigManager().removePlayer(e.getPlayer());
    }
}
