package io.minki.lobby;

import io.minki.gapi.queue.QueueManager;
import io.minki.lobby.ui.ScoreboardManager;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.timer.TaskSchedule;

public class LobbyManager {
    private final QueueManager queueManager;
    private final BossBar queueBossBar;
    private final ScoreboardManager scoreboardManager;

    public LobbyManager() {
        this.queueManager = new QueueManager();
        this.queueBossBar = BossBar.bossBar(Component.text("Looking for a game..."), 0, BossBar.Color.BLUE, BossBar.Overlay.PROGRESS);
        this.scoreboardManager = new ScoreboardManager();

        MinecraftServer.getSchedulerManager().submitTask(() -> {
            float newValue = queueBossBar.progress() + 0.1f;
            if (newValue > 1) {
                newValue = 1;
            }

            queueBossBar.progress(newValue);
            if (queueBossBar.progress() >= 1) {
                queueBossBar.progress(0);
            }
            return TaskSchedule.millis(100);
        });
    }

    public QueueManager getQueueManager() {
        return queueManager;
    }

    public void showQueueBossBar(Player p) {
        Audience.audience(p).showBossBar(queueBossBar);
    }

    public void hideQueueBossBar(Player p) {
        Audience.audience(p).hideBossBar(queueBossBar);
    }

    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }
}
