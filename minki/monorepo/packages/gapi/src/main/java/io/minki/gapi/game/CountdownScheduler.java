package io.minki.gapi.game;

import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import org.jetbrains.annotations.Nullable;

/**
 * Minestom scheduler for countdowns
 */
public class CountdownScheduler {
    private final Scheduler scheduler = MinecraftServer.getSchedulerManager();
    private @Nullable Task task;
    private final CountdownCallback callback;
    private final CountdownFinishedCallback finishedCallback;
    private int countdown;

    /**
     * Constructor
     *
     * @param countdown Countdown time in seconds
     * @param callback  Callback called every second
     */
    public CountdownScheduler(int countdown, CountdownCallback callback, CountdownFinishedCallback finishedCallback) {
        this.countdown = countdown;
        this.callback = callback;
        this.finishedCallback = finishedCallback;
    }

    /**
     * Start the countdown
     */
    public void start() {
        task = scheduler.submitTask(() -> {
            if (countdown == 0 && task != null) {
                task.cancel();
                finishedCallback.call();
            } else {
                callback.call(countdown);
                countdown--;
            }

            return TaskSchedule.seconds(1);
        });
    }

    /**
     * Stop the countdown
     */
    public void stop() {
        if (task != null) {
            task.cancel();
        }
    }
}
