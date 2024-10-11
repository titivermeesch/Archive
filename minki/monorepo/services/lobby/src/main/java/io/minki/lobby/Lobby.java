package io.minki.lobby;

import io.minki.lobby.commands.GameCommand;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.instance.AddEntityToInstanceEvent;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.extras.velocity.VelocityProxy;
import net.minestom.server.instance.*;
import net.minestom.server.coordinate.Pos;

public class Lobby {
    public static void main(String[] args) {
        MinecraftServer minecraftServer = MinecraftServer.init();
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        LobbyManager lobbyManager = new LobbyManager();

        VelocityProxy.enable("SchXkUH5NZS4");

        InstanceContainer instanceContainer = instanceManager.createInstanceContainer();
        instanceContainer.setChunkLoader(new AnvilLoader("worlds/world"));
        instanceContainer.setChunkSupplier(LightingChunk::new);

        registerListeners(instanceContainer, lobbyManager);
        registerCommands(lobbyManager);

        minecraftServer.start("0.0.0.0", 25565);
        System.out.println("Server started on port 25565");
    }

    private static void registerCommands(LobbyManager lobbyManager) {
        MinecraftServer.getCommandManager().register(new GameCommand(lobbyManager));
    }

    private static void registerListeners(InstanceContainer instanceContainer, LobbyManager lobbyManager) {
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addListener(AsyncPlayerConfigurationEvent.class, event -> {
            final Player player = event.getPlayer();
            event.setSpawningInstance(instanceContainer);
            player.setRespawnPoint(new Pos(0, 70, 0));
            player.setGameMode(GameMode.ADVENTURE);
        });
        globalEventHandler.addListener(AddEntityToInstanceEvent.class, event -> {
            if(event.getEntity() instanceof Player p) {
                lobbyManager.getScoreboardManager().addPlayer(p);
            }
        });
        globalEventHandler.addListener(PlayerBlockBreakEvent.class, event -> {
            event.setCancelled(true);
        });
        globalEventHandler.addListener(PlayerMoveEvent.class, event -> {
            final Player player = event.getPlayer();
            if (player.getPosition().y() < 0) {
                player.teleport(new Pos(0, 70, 0));
            }
        });
    }
}
