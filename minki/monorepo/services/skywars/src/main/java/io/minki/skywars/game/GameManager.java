package io.minki.skywars.game;

import io.minki.gapi.Gapi;
import io.minki.gapi.game.CountdownScheduler;
import io.minki.gapi.text.TextBuilder;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.timer.TaskSchedule;
import proto.Mappy;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameManager {
    private final Mappy.Map map;
    private final Gapi gapi;
    private final InstanceContainer instanceContainer;
    private final List<GamePlayer> players = new ArrayList<>();
    private GameStatus status = GameStatus.WAITING;
    private final int minPlayers;
    private final int maxPlayers;
    private CountdownScheduler countdown;

    public GameManager(Mappy.Map map, Gapi gapi, InstanceContainer instanceContainer) {
        this.map = map;
        this.gapi = gapi;
        this.instanceContainer = instanceContainer;
        this.minPlayers = map.getMinPlayers();
        this.maxPlayers = map.getMaxPlayers();
    }

    public void addPlayer(Player player) {
        players.add(new GamePlayer(player));

        if (status == GameStatus.WAITING && instanceContainer.getPlayers().size() >= minPlayers) {
            countdown = new CountdownScheduler(5, (v) -> instanceContainer.sendMessage(new TextBuilder("Game will start in " + v).build()), () -> {
                status = GameStatus.IN_GAME;
                new CombatListeners(this, MinecraftServer.getGlobalEventHandler()).register();
            });
        }
    }

    public void removePlayer(UUID uuid) {
        players.removeIf(gamePlayer -> gamePlayer.getUuid().equals(uuid));
        if (status == GameStatus.STARTING && instanceContainer.getPlayers().size() < minPlayers) {
            status = GameStatus.WAITING;
            countdown.stop();
        }
    }

    public void endGame(GamePlayer winner) {
        status = GameStatus.END;
        instanceContainer.sendMessage(new TextBuilder("Game has ended! Winner: " + winner.getName()).build());

        MinecraftServer.getSchedulerManager().buildTask(() -> {
            instanceContainer.sendMessage(new TextBuilder("Shutting down server...").build());
            gapi.getAgonesClient().shutdown();
        }).delay(TaskSchedule.seconds(5)).schedule();
    }

    public GamePlayer getPlayer(UUID uuid) {
        return players.stream().filter(gamePlayer -> gamePlayer.getUuid().equals(uuid)).findFirst().orElse(null);
    }

    public List<GamePlayer> getPlayers() {
        return players;
    }

    public GameStatus getStatus() {
        return status;
    }
}
