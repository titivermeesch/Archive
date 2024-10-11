package fr.midaria.pvparena.stats;

public class EconomyHologramUpdater implements Runnable {
    private final EconomyManager manager;
    private int countdown = 15;

    public EconomyHologramUpdater(EconomyManager manager) {
        this.manager = manager;
    }

    @Override
    public void run() {
        countdown--;
        if (countdown == 0) {
            manager.reloadHologram();
            countdown = 15;
        }
    }
}
