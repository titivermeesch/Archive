package me.playbosswar.enderhoppers.events;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import me.playbosswar.enderhoppers.EnderHoppers;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class HopperTransferListeners implements Listener {
    @EventHandler
    public void onEnderPlace(BlockPlaceEvent e) {
        Block block = e.getBlock();

        if (!block.getType().equals(Material.ENDER_CHEST)) {
            return;
        }
    }

    @EventHandler
    public void onEnderBreak(BlockBreakEvent e) {
        Block block = e.getBlock();

        if (!block.getType().equals(Material.ENDER_CHEST)) {
            return;
        }
    }

    @EventHandler
    public void onItemTransfer(ServerTickEndEvent e) {
        EnderHoppers.manager.getEnderChests().forEach(enderChest -> {
            Chest chest = (Chest) enderChest;
        });
    }
}
