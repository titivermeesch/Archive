package io.minki.skywars;

import io.github.togar2.pvp.PvpExtension;
import io.minki.gapi.agones.AgonesClient;
import io.minki.gapi.maps.MapsManager;
import io.minki.skywars.events.ConnectionEvents;
import io.minki.skywars.game.GameManager;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.extras.velocity.VelocityProxy;
import net.minestom.server.instance.*;

import io.minki.gapi.Gapi;
import net.minestom.server.instance.block.Block;
import proto.Mappy;

import java.util.concurrent.TimeUnit;

public class Skywars {
    public static void main(String[] args) {

        String portEnv = System.getenv("PORT");

        Gapi gapi = new Gapi();
        AgonesClient agones = gapi.getAgonesClient();
        agones.startHealthTask(3, TimeUnit.SECONDS);

        MinecraftServer minecraftServer = MinecraftServer.init();
        PvpExtension.init();

        VelocityProxy.enable("SchXkUH5NZS4");

        MapsManager mapsManager = gapi.getMapsManager();
        Mappy.Map map = mapsManager.getRandomMap("skywars");
        InstanceContainer instanceContainer = mapsManager.loadMap(map);
        System.out.println("Loaded map: " + map.getMap());

        agones.addLabel("map", map.getMap());
        agones.addLabel("minPlayers", Integer.toString(map.getMinPlayers()));
        agones.addLabel("maxPlayers", Integer.toString(map.getMaxPlayers()));
        agones.addLabel("gameStatus", "waiting");

        System.out.println("Updated Agones labels");

        GameManager gameManager = new GameManager(map, gapi, instanceContainer);

        instanceContainer.setGenerator(unit ->
                unit.modifier().fillHeight(0, 40, Block.GRASS_BLOCK));
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        // Add an event callback to specify the spawning instance (and the spawn position)
        globalEventHandler.addListener(AsyncPlayerConfigurationEvent.class, event -> {
            final Player player = event.getPlayer();
            event.setSpawningInstance(instanceContainer);
            player.setRespawnPoint(new Pos(0, 42, 0));
        });
        new ConnectionEvents(gapi, globalEventHandler, instanceContainer, gameManager).register();
        globalEventHandler.addChild(PvpExtension.events());


        minecraftServer.start("0.0.0.0", Integer.parseInt(portEnv));
        agones.ready();
        System.out.println("Server started on port:" + portEnv);
    }
}
