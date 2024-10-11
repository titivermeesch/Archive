package fr.midaria.pvparena.mechanics.evolutions.types;

import fr.midaria.pvparena.mechanics.evolutions.Evolution;
import fr.midaria.pvparena.mechanics.evolutions.EvolutionLevel;
import fr.midaria.pvparena.mechanics.evolutions.EvolutionManager;
import fr.midaria.pvparena.mechanics.evolutions.EvolutionType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BonusGappleEvolution implements Evolution, Listener {
    private final EvolutionManager manager;

    public BonusGappleEvolution(Plugin plugin, EvolutionManager manager) {
        this.manager = manager;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public EvolutionType getType() {
        return EvolutionType.BONUS_GOLDEN_APPLE;
    }

    @Override
    public String getName() {
        return "§dIsaac Newton";
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList("", "§7A chaque kill, vous avez une chance de", "§7recevoir une 2e pomme d'or");
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public List<EvolutionLevel> getLevels() {
        EvolutionLevel level1 = new EvolutionLevel(1, new ArrayList<>(Arrays.asList("", "§d10% §7de chance d'avoir une 2e pomme d'or")), 200);
        EvolutionLevel level2 = new EvolutionLevel(2, new ArrayList<>(Arrays.asList("", "§d25% §7de chance d'avoir une 2e pomme d'or")), 500);
        EvolutionLevel level3 = new EvolutionLevel(3, new ArrayList<>(Arrays.asList("", "§d40% §7de chance d'avoir une 2e pomme d'or")), 1000);
        EvolutionLevel level4 = new EvolutionLevel(4, new ArrayList<>(Arrays.asList("", "§d50% §7de chance d'avoir une 2e pomme d'or")), 1500);
        EvolutionLevel level5 = new EvolutionLevel(5, new ArrayList<>(Arrays.asList("", "§d60% §7de chance d'avoir une 2e pomme d'or")), 2500);

        return Arrays.asList(level1, level2, level3, level4, level5);
    }

    @Override
    public Material getIcon() {
        return Material.GOLDEN_APPLE;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onKill(PlayerDeathEvent e) {
        Player p = e.getEntity().getKiller();
        if (p == null) {
            return;
        }

        int level = manager.getPlayerEvolutionLevel(p.getUniqueId(), this.getType());

        Random rand = new Random();
        int randomValue = rand.nextInt(99);
        int upgradeChance = getLevelUpgradeChance(level);
        if (upgradeChance == 0 || randomValue >= upgradeChance) {
            return;
        }

        p.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE));
        p.playSound(p.getLocation(), Sound.EAT, 1, 1);
        p.sendMessage("§dTu as reçu une pomme d'or supplémentaire grâce à ton évolution");
    }

    private int getLevelUpgradeChance(int level) {
        switch (level) {
            case 1:
                return 10;
            case 2:
                return 25;
            case 3:
                return 40;
            case 4:
                return 50;
            case 5:
                return 60;
            default:
                return 0;
        }
    }
}
