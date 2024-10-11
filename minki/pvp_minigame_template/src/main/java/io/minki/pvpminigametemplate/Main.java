package io.minki.pvpminigametemplate;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.instance.AddEntityToInstanceEvent;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerDeathEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.block.Block;

public class Main {
    public static void main(String[] args) {
        MinecraftServer server = MinecraftServer.init();
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();

        InstanceContainer instanceContainer = instanceManager.createInstanceContainer();
        instanceContainer.setGenerator(unit -> unit.modifier().fillHeight(0, 60, Block.GRASS_BLOCK));
        instanceContainer.setChunkSupplier(LightingChunk::new);

        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();

        globalEventHandler.addListener(AsyncPlayerConfigurationEvent.class, event -> {
            final Player player = event.getPlayer();
            event.setSpawningInstance(instanceContainer);
            player.setRespawnPoint(new Pos(0, 60, 0));
            if (instanceContainer.getPlayers().size() == 2) {
                instanceContainer.sendMessage(Component.text("Game started, last man standing wins!"));
            }
        });
        globalEventHandler.addListener(AddEntityToInstanceEvent.class, event -> {
            final Entity entity = event.getEntity();
            if (entity instanceof Player) {
                final Player player = (Player) entity;
            }
        });
        globalEventHandler.addListener(PlayerDeathEvent.class, event -> {
            final Player player = event.getPlayer();
            instanceContainer.sendMessage(Component.text(player.getUsername() + " died!"));
            player.setGameMode(GameMode.SPECTATOR);
            if (instanceContainer.getPlayers().size() == 1) {
                instanceContainer.sendMessage(Component.text("Game over!"));
            }
        });

        server.start("0.0.0.0", 25565);
        System.out.println("server listening");
    }
}

