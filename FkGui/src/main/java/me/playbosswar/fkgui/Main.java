package me.playbosswar.fkgui;

import fr.minuskube.inv.InventoryManager;
import me.playbosswar.fkgui.config.ConfigManager;
import me.playbosswar.fkgui.events.CompassEvents;
import me.playbosswar.fkgui.events.ConnectEvents;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private static Main instance;
    private static InventoryManager inventoryManager;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        inventoryManager = new InventoryManager(this);
        configManager = new ConfigManager();
        getServer().getPluginManager().registerEvents(new ConnectEvents(), this);
        getServer().getPluginManager().registerEvents(new CompassEvents(), this);
        inventoryManager.init();
    }

    @Override
    public void onDisable() {
        instance = null;
        inventoryManager = null;
    }

    public static Main getInstance() {
        return instance;
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public String getText(String key) {
        return ChatColor.translateAlternateColorCodes('&', getConfig().getString(key));
    }
}
