package io.minki.skywars.events;

import io.minki.gapi.Gapi;
import io.minki.skywars.game.GameManager;
import io.minki.skywars.game.GameStatus;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.instance.InstanceContainer;

public class ConnectionEvents {
    private final Gapi gapi;
    private final GlobalEventHandler eventHandler;
    private final InstanceContainer instanceContainer;
    private final GameManager gameManager;

    public ConnectionEvents(Gapi gapi, GlobalEventHandler eventHandler, InstanceContainer instanceContainer, GameManager gameManager) {
        this.gapi = gapi;
        this.eventHandler = eventHandler;
        this.instanceContainer = instanceContainer;
        this.gameManager = gameManager;
    }

    public void register() {
        registerPlayerJoinEvent();
        registerPlayerLeaveEvent();
    }

    private void registerPlayerJoinEvent() {
        eventHandler.addListener(AsyncPlayerConfigurationEvent.class, e -> {
            e.setSpawningInstance(instanceContainer);
            e.getPlayer().setRespawnPoint(new Pos(0, 92, 0));
            gapi.getAgonesClient().allocate();
            gapi.getAgonesClient().playerConnect(e.getPlayer().getUuid());
            gameManager.addPlayer(e.getPlayer());
        });
    }

    private void registerPlayerLeaveEvent() {
        eventHandler.addListener(PlayerDisconnectEvent.class, e -> {
            // Don't remove player if game started, list of players in gamemanager is used for statistics
            if (gameManager.getStatus() == GameStatus.WAITING) {
                gameManager.removePlayer(e.getPlayer().getUuid());
            }
            gapi.getAgonesClient().playerDisconnect(e.getPlayer().getUuid());
            if (instanceContainer.getPlayers().isEmpty()) {
                gapi.getAgonesClient().ready();
            }
        });
    }
}
