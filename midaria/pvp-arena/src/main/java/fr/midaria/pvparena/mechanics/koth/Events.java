package fr.midaria.pvparena.mechanics.koth;

import fr.midaria.pvparena.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class Events implements Listener {
    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        KothInstance koth = Main.getInstance().getKothManager().getActiveKoth();
        if (koth == null) {
            return;
        }
        koth.getLeaderboard().remove(e.getPlayer());
    }
}
