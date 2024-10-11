package me.playbosswar.fkgui.events;

import me.playbosswar.fkgui.gui.MainMenu;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class CompassEvents implements Listener {
    @EventHandler
    public void onCompass(PlayerInteractEvent e) {
        if(!e.getPlayer().getItemInHand().getType().equals(Material.COMPASS)) {
            return;
        }

        new MainMenu().INVENTORY.open(e.getPlayer());
    }
}
