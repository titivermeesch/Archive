package io.minki.proxyfranz.players;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.minki.proxyfranz.ProxyFranz;
import proto.PlayerManagerServiceGrpc;
import proto.Playermanager;

import java.util.UUID;

public class PlayersLevelManager {
    private final ProxyFranz plugin;
    private final PlayerManagerServiceGrpc.PlayerManagerServiceBlockingStub stub;

    public PlayersLevelManager(ProxyFranz plugin) {
        this.plugin = plugin;

        ManagedChannel channel = ManagedChannelBuilder.forAddress("playermanager", 3000).usePlaintext().build();
        PlayerManagerServiceGrpc.PlayerManagerServiceBlockingStub stub = PlayerManagerServiceGrpc.newBlockingStub(channel);

        this.stub = stub;
    }

    public int getPlayerLevel(UUID uuid) {
        String cacheEntry = plugin.getCache().get("level:" + uuid);

        if (cacheEntry != null) {
            System.out.println("Hit cache for level:" + uuid + " " + cacheEntry);
            return Integer.parseInt(cacheEntry);
        }

        try {
            System.out.println("Miss cache for level:" + uuid);
            Playermanager.GetPlayerRequest request = Playermanager.GetPlayerRequest.newBuilder().setUuid(uuid.toString()).build();
            Playermanager.GetPlayerResponse resp = stub.getPlayer(request);

            int level = resp.getLevel();
            plugin.getCache().put("level:" + uuid, String.valueOf(level));
            return level;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
