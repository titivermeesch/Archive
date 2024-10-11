package fr.midaria.pvparena.mechanics.evolutions.types;

import fr.midaria.pvparena.mechanics.evolutions.Evolution;
import fr.midaria.pvparena.mechanics.evolutions.EvolutionLevel;
import fr.midaria.pvparena.mechanics.evolutions.EvolutionManager;
import fr.midaria.pvparena.mechanics.evolutions.EvolutionType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
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

public class BowUpgradeEvolution implements Evolution, Listener {
    private final EvolutionManager manager;

    public BowUpgradeEvolution(Plugin plugin, EvolutionManager manager) {
        this.manager = manager;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public EvolutionType getType() {
        return EvolutionType.BOW_UPGRADE;
    }

    @Override
    public Material getIcon() {
        return Material.BOW;
    }

    @Override
    public String getName() {
        return "§dPew Pew Pew";
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList(
                "",

                "§7A chaque kill, vous avez une chance d'améliorer",
                "§7votre arc au niveau supérieur",
                "",
                "§7L'évolution se fait de manière incrémentielle,",
                "§7votre arc passera donc d'abord par",
                "§7protection 1, protection 2,... avant d'atteindre",
                "§7des niveaux supérieurs");
    }

    @Override
    public int getMaxLevel() {
        return 4;
    }

    @Override
    public List<EvolutionLevel> getLevels() {
        EvolutionLevel level1 = new EvolutionLevel(1, new ArrayList<>(Arrays.asList("", "§d70% §7de chance d'améliorer votre arc", "§7vers un §darc power 1 §7suite à un kill")), 650);
        EvolutionLevel level2 = new EvolutionLevel(2, new ArrayList<>(Arrays.asList("", "§d40% §7de chance d'améliorer votre arc", "§7vers une §darc power 2 §7suite à un kill")), 1200);
        EvolutionLevel level3 = new EvolutionLevel(3, new ArrayList<>(Arrays.asList("", "§d25% §7de chance d'améliorer votre arc", "§7vers une §darc power 3 §7suite à un kill")), 2300);
        EvolutionLevel level4 = new EvolutionLevel(4, new ArrayList<>(Arrays.asList("", "§d10% §7de chance d'améliorer votre arc", "§7vers une §darc power 3 flame 1 §7suite à un kill")), 4000);

        return Arrays.asList(level1, level2, level3, level4);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onKill(PlayerDeathEvent e) {
        Player p = e.getEntity().getKiller();
        if (p == null) {
            return;
        }

        int level = manager.getPlayerEvolutionLevel(p.getUniqueId(), this.getType());
        int highestBowLevel = getHighestBowLevel(Arrays.asList(p.getInventory().getContents()));

        if (highestBowLevel >= level) {
            return;
        }

        Random rand = new Random();
        int randomValue = rand.nextInt(99);
        int upgradeChance = getLevelUpgradeChance(highestBowLevel + 1);
        if (upgradeChance == 0 || randomValue >= upgradeChance) {
            return;
        }

        // Do the actual upgrade
        ItemStack highestBow = getHighestBow(Arrays.asList(p.getInventory().getContents()));

        switch (highestBowLevel) {
            case 0:
                highestBow.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
                break;
            case 1:
                highestBow.addEnchantment(Enchantment.ARROW_DAMAGE, 2);
                break;
            case 2:
                highestBow.addEnchantment(Enchantment.ARROW_DAMAGE, 3);
                break;
            case 3:
                highestBow.addEnchantment(Enchantment.ARROW_DAMAGE, 3);
                highestBow.addEnchantment(Enchantment.ARROW_FIRE, 3);
                break;
        }

        // Highest level is 4, don't show upgrade message because there is nothing to upgrade to
        if (highestBowLevel == 4) {
            return;
        }

        p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 1, 1);
        p.sendMessage("§dTon arc a été upgrade au niveau suivant!");
    }

    private int getLevelUpgradeChance(int level) {
        switch (level) {
            case 1:
                return 70;
            case 2:
                return 40;
            case 3:
                return 25;
            case 4:
                return 10;
            default:
                return 0;
        }
    }

    private int getBowLevel(ItemStack bow) {
        if (bow == null) {
            return 0;
        }

        int powerLevel = bow.getEnchantmentLevel(Enchantment.ARROW_DAMAGE);
        int flameLevel = bow.getEnchantmentLevel(Enchantment.ARROW_FIRE);

        switch (powerLevel) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                if (flameLevel == 0) {
                    return 3;
                }
                if (flameLevel == 1) {
                    return 4;
                }
            default:
                return 0;
        }
    }

    private int getHighestBowLevel(List<ItemStack> inventory) {
        ItemStack highestBow = getHighestBow(inventory);

        return getBowLevel(highestBow);
    }

    private ItemStack getHighestBow(List<ItemStack> inventory) {
        List<ItemStack> bows = new ArrayList<>();
        for (ItemStack item : inventory) {
            if (item == null) {
                continue;
            }
            if (item.getType().equals(Material.BOW)) {
                bows.add(item);
            }
        }

        ItemStack currentBestBow = null;
        int currentMaxLevel = 0;

        for (ItemStack bow : bows) {
            int level = getBowLevel(bow);
            if (level >= currentMaxLevel) {
                currentMaxLevel = level;
                currentBestBow = bow;
            }
        }

        return currentBestBow;
    }
}
