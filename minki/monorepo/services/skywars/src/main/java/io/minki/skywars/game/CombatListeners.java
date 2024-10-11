package io.minki.skywars.game;

import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerDeathEvent;

public class CombatListeners {
    private final GlobalEventHandler eventHandler;
    private final GameManager gameManager;

    public CombatListeners(GameManager gameManager, GlobalEventHandler eventHandler) {
        this.eventHandler = eventHandler;
        this.gameManager = gameManager;
    }

    public void register() {
        eventHandler.addListener(PlayerDeathEvent.class, e -> {
            gameManager.getPlayer(e.getPlayer().getUuid()).setAlive(false);
            if (gameManager.getPlayers().stream().filter(GamePlayer::isAlive).count() == 1) {
                GamePlayer winner = gameManager.getPlayers().stream().filter(GamePlayer::isAlive).findFirst().get();
                gameManager.endGame(winner);
            }
        });
    }
}
