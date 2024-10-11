package me.playbosswar.anyworldcommand;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        getCommand("awc").setExecutor(new ExecuteCommand());
    }

    @Override
    public void onDisable() {
    }
}
