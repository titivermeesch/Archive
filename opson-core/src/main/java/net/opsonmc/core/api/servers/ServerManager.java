package net.opsonmc.core.api.servers;

import redis.clients.jedis.JedisPooled;

import java.util.UUID;

// Responsible for sending players to different servers
public class ServerManager {
    private final JedisPooled jedis;

    public ServerManager() {
        this.jedis = new JedisPooled("localhost", 6379);
    }

    public void sendPlayerToServer(UUID uuid, String serverName) {
        jedis.publish("player_transfers", uuid.toString() + ":" + serverName);
    }
}
