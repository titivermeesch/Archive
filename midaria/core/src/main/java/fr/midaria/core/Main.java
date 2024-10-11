package fr.midaria.core;

import fr.midaria.core.common.database.DatabaseManager;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private static Plugin instance;
    private DatabaseManager databaseManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        this.databaseManager = new DatabaseManager();
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public Plugin getInstance() {
        return instance;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}
