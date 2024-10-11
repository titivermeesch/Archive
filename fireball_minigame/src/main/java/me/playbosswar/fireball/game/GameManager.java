package me.playbosswar.fireball.game;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameManager {
    public static GameManager instance;
    private final List<Arena> loadedArenas = new ArrayList<>();
    private final List<Game> ongoingGames = new ArrayList<>();

    public GameManager(Plugin plugin) {
        instance = this;
        Bukkit.getPluginManager().registerEvents(new JoinListeners(), plugin);
    }

    public void createArena(String name) {
        Arena existingArena = getArenaByName(name);
        if (existingArena != null) {
            return;
        }

        Arena arena = new Arena(name);
        loadedArenas.add(arena);
    }

    @Nullable
    public Arena getArenaByName(String name) {
        for (Arena arena : loadedArenas) {
            if (arena.getName().equalsIgnoreCase(name)) {
                return arena;
            }
        }
        return null;
    }

    public void removePlayer(UUID player) {
        for (Game game : ongoingGames) {
            game.removePlayer(player);
        }
    }

    public List<Arena> getLoadedArenas() {
        return loadedArenas;
    }

    public static GameManager getInstance() {
        return instance;
    }
}
