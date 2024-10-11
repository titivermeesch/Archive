package io.minki.skywars.game;

import net.minestom.server.entity.Player;

public class GamePlayer extends Player {
    private boolean isAlive = true;

    public GamePlayer(Player player) {
        super(player.getUuid(), player.getUsername(), player.getPlayerConnection());
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }
}
