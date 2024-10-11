package io.minki.proxyfranz.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.event.player.ServerPostConnectEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import io.minki.gapi.queue.QueueManager;
import io.minki.gapi.text.Colors;
import io.minki.gapi.text.TextBuilder;
import net.kyori.adventure.text.Component;
import org.json.JSONObject;

public class PlayerConnectionListener {
    private final ProxyServer server;
    private final KafkaManager kafkaManager;
    private final QueueManager queueManager;

    public PlayerConnectionListener(ProxyServer server, KafkaManager kafkaManager) {
        this.server = server;
        this.kafkaManager = kafkaManager;
        this.queueManager = new QueueManager();
    }

    @Subscribe
    public void onPreConnect(PlayerChooseInitialServerEvent e) {
        // TODO: Some smart balancing is required here to ensure we fill up lobbies correctly
        RegisteredServer lobby = server.matchServer("lobby-").stream().findFirst().orElse(null);
        if (lobby != null) {
            e.setInitialServer(lobby);
        }
    }

    @Subscribe
    public void onPlayerJoin(LoginEvent e) {
        String jsonString = new JSONObject()
                .put("uuid", e.getPlayer().getUniqueId().toString())
                .put("username", e.getPlayer().getUsername())
                .put("ip", e.getPlayer().getRemoteAddress().getAddress().getHostAddress())
                .put("timestamp", System.currentTimeMillis())
                .toString();
        kafkaManager.produce("player_connection", e.getPlayer().getUniqueId().toString(), jsonString);

    }

    @Subscribe
    public void onPostJoin(ServerPostConnectEvent e) {
        e.getPlayer().sendPlayerListHeaderAndFooter(
                new TextBuilder("You are playing on Minki").color(Colors.PRIMARY).build(),
                new TextBuilder("There are currently " + server.getPlayerCount() + " players online").color(Colors.TERTIARY).build()
        );
    }

    @Subscribe
    public void onPlayerLeave(DisconnectEvent e) {
        queueManager.dequeuePlayer(e.getPlayer().getUniqueId());
    }
}
