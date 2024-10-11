package net.opsonmc.core.api.kits;

import net.opsonmc.core.api.game.GameType;

import java.util.List;
import java.util.UUID;

public class KitsManager {
    private GameType gameType;
    private List<Kit> kits;

    public KitsManager() {
        this.kits = new KitsList().get();
    }

    // Allows the manager to only cache kits for specific game (used on actual game servers)
    public KitsManager(GameType gameType) {
        this.gameType = gameType;

        List<Kit> allKits = new KitsList().get();
        this.kits = allKits.stream().filter(k -> k.getGame().equals(gameType)).toList();
    }

    // Cache player
    public void preloadPlayer(UUID uuid) {

    }

    public void unloadPlayer(UUID uuid) {

    }
}
