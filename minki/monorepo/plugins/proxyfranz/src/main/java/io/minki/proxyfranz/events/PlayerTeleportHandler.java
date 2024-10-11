package io.minki.proxyfranz.events;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import io.github.cdimascio.dotenv.Dotenv;
import io.minki.gapi.queue.QueueManager;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.json.JSONObject;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Properties;
import java.util.UUID;

public class PlayerTeleportHandler {
    private final QueueManager queueManager;

    public PlayerTeleportHandler(Properties config, ProxyServer proxy) {
        this.queueManager = new QueueManager();

        new Thread(() -> {
            new BasicConsumeLoop<String, String>(config, List.of("teleport_requests")) {
                @Override
                public void process(ConsumerRecord<String, String> record) {
                    JSONObject data = new JSONObject(record.value());
                    String uuid = data.getString("uuid");
                    String server = data.getString("server");

                    if (uuid == null || server == null) {
                        System.out.println("Uuid or server is null " + uuid + " " + server);
                        return;
                    }

                    queueManager.dequeuePlayer(UUID.fromString(uuid));

                    RegisteredServer registeredServer = proxy.matchServer(server).stream().findFirst().orElse(null);
                    if (registeredServer == null) {
                        System.out.println("Could not find registered server " + server);
                        return;
                    }

                    Player player = proxy.getPlayer(UUID.fromString(uuid)).orElse(null);
                    if (player == null) {
                        System.out.println("Could not find player " + uuid);
                        return;
                    }

                    // If player is not in lobby anymore, ignore request
                    if (!player.getCurrentServer().get().getServer().getServerInfo().getName().contains("lobby-")) {
                        System.out.println("Player is not in lobby anymore, ignoring");
                        return;
                    }

                    System.out.println("Requesting player teleport to " + server);
                    player.createConnectionRequest(registeredServer).fireAndForget();
                }
            }.run();
        }).start();
    }
}
