package me.playbosswar.fireball;

import me.playbosswar.fireball.game.GameManager;
import me.playbosswar.fireball.setup.ArenaSetupCommands;
import org.bukkit.plugin.java.JavaPlugin;

public class Fireball extends JavaPlugin {
    public static Fireball instance;
    public ServerStatus serverStatus = ServerStatus.ACCEPTING_GAMES;
    public GameManager gameManager;

    @Override
    public void onEnable() {
        instance = this;
        new GameManager(this);
        getCommand("fireballarena").setExecutor(new ArenaSetupCommands());
    }

    @Override
    public void onDisable() {

    }

    public static Fireball getInstance() {
        return instance;
    }

    public GameManager getGameManager() {
        return gameManager;
    }
}
