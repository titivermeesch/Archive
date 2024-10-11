package me.playbosswar.enderhoppers;

import me.playbosswar.enderhoppers.hoppers.HopperManager;
import org.bukkit.plugin.java.JavaPlugin;

public class EnderHoppers extends JavaPlugin {
    public static HopperManager manager;

    @Override
    public void onEnable() {
        manager = new HopperManager();
    }

    @Override
    public void onDisable() {

    }
}
