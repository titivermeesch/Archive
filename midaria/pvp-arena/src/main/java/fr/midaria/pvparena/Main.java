package fr.midaria.pvparena;

import fr.midaria.pvparena.common.commands.MainCommands;
import fr.midaria.pvparena.common.commands.WarpCommand;
import fr.midaria.pvparena.common.database.DatabaseManager;
import fr.midaria.pvparena.common.events.ChatEvents;
import fr.midaria.pvparena.common.events.JoiningEvents;
import fr.midaria.pvparena.common.events.KillingEvents;
import fr.midaria.pvparena.mechanics.chest.RewardChestManager;
import fr.midaria.pvparena.items.HealStickManager;
import fr.midaria.pvparena.mechanics.evolutions.EvolutionManager;
import fr.midaria.pvparena.mechanics.koth.Events;
import fr.midaria.pvparena.mechanics.koth.KothManager;
import fr.midaria.pvparena.mechanics.shop.ShopCommand;
import fr.midaria.pvparena.papi.PvpPlaceholders;
import fr.midaria.pvparena.stats.EconomyManager;
import fr.midaria.pvparena.stats.StatsManager;
import fr.minuskube.inv.InventoryManager;
import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private static Main instance;
    private static Economy econ = null;
    private HealStickManager healStickManager;
    private DatabaseManager database;
    private StatsManager statsManager;
    private EconomyManager economyManager;
    private KothManager kothManager;
    private RewardChestManager rewardChestManager;
    private InventoryManager inventoryManager;
    private EvolutionManager evolutionManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        this.database = new DatabaseManager();
        if (!setupEconomy()) {
            Bukkit.getConsoleSender().sendMessage("§4ECONOMY IS NOT SET UP FOR MIDARIAPVP");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        statsManager = new StatsManager(this);
        economyManager = new EconomyManager(this);
        kothManager = new KothManager(this);
        rewardChestManager = new RewardChestManager(this);
        evolutionManager = new EvolutionManager(this);
        getCommand("midariapvp").setExecutor(new MainCommands());
        getCommand("shop").setExecutor(new ShopCommand());
        getCommand("warp").setExecutor(new WarpCommand());
        Bukkit.getPluginManager().registerEvents(new Events(), this);
        Bukkit.getPluginManager().registerEvents(new KillingEvents(), this);
        Bukkit.getPluginManager().registerEvents(new JoiningEvents(), this);
        Bukkit.getPluginManager().registerEvents(new ChatEvents(), this);
        this.healStickManager = new HealStickManager();
        this.inventoryManager = new InventoryManager(this);
        inventoryManager.init();
        new PvpPlaceholders(this).register();
    }

    @Override
    public void onDisable() {
        saveConfig();
        instance = null;
        if (healStickManager != null) {
            healStickManager.disable();
        }
        HolographicDisplaysAPI api = HolographicDisplaysAPI.get(this);
        api.deleteHolograms();
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Main getInstance() {
        return instance;
    }

    public Economy getEcon() {
        return econ;
    }

    public HealStickManager getHealStickManager() {
        return healStickManager;
    }

    public DatabaseManager getDb() {
        return database;
    }

    public StatsManager getStatsManager() {
        return statsManager;
    }

    public EconomyManager getEconomyManager() {
        return economyManager;
    }

    public KothManager getKothManager() {
        return kothManager;
    }

    public RewardChestManager getRewardChestManager() {
        return rewardChestManager;
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public EvolutionManager getEvolutionManager() {
        return evolutionManager;
    }
}
