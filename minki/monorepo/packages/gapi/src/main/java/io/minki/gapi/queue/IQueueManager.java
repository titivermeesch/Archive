package io.minki.gapi.queue;

import java.util.UUID;

// Defines what a queue manager is supposed to handle
public interface IQueueManager {
    void queuePlayer(UUID playerUuid, String game, String map);
    boolean isPlayerInQueue(UUID playerUuid);
    void dequeuePlayer(UUID playerUuid);
}
