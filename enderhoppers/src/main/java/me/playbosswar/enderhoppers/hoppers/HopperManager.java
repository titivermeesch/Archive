package me.playbosswar.enderhoppers.hoppers;

import org.bukkit.block.EnderChest;

import java.util.ArrayList;
import java.util.List;

public class HopperManager {
    private final List<EnderChest> enderChests = new ArrayList<>();

    public HopperManager() {
    }

    public List<EnderChest> getEnderChests() {
        return enderChests;
    }

    public void addChest(EnderChest chest) {
        enderChests.add(chest);
    }
}
