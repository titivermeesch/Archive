package fr.midaria.pvparena.stats;

public class PvpHologramUpdater implements Runnable {
    private final StatsManager manager;
    private int countdown = 15;

    // 14 lines
    public PvpHologramUpdater(StatsManager manager) {
        this.manager = manager;
    }

    @Override
    public void run() {
        countdown--;
        if (countdown == 0) {
            manager.reloadStatsHologram();
            countdown = 15;
        }
    }
}
