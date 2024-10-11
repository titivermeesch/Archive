package io.minki.gapi.queue;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import proto.QueueManagerServiceGrpc;
import proto.Queuemanager;

import java.util.UUID;

public class QueueManager implements IQueueManager {
    private final QueueManagerServiceGrpc.QueueManagerServiceBlockingStub stub;

    public QueueManager() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("queuemanager.default.svc.cluster.local", 3000).usePlaintext().build();
        this.stub = QueueManagerServiceGrpc.newBlockingStub(channel);
    }

    @Override
    public void queuePlayer(UUID playerUuid, String game, String map) {
        Queuemanager.QueuePlayerRequest request = Queuemanager.QueuePlayerRequest.newBuilder()
                .setUuid(playerUuid.toString())
                .setGame(game)
                .setMap(map)
                .build();
        stub.queuePlayer(request);
    }

    @Override
    public boolean isPlayerInQueue(UUID playerUuid) {
        Queuemanager.IsQueuedRequest request = Queuemanager.IsQueuedRequest.newBuilder()
                .setUuid(playerUuid.toString())
                .build();
        return stub.isQueued(request).getQueued();
    }

    @Override
    public void dequeuePlayer(UUID playerUuid) {
        Queuemanager.DequeuePlayerRequest request = Queuemanager.DequeuePlayerRequest.newBuilder()
                .setUuid(playerUuid.toString())
                .build();
        stub.dequeuePlayer(request);
    }
}
