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

public class SwordUpgradeEvolution implements Evolution, Listener {
    private final EvolutionManager manager;

    public SwordUpgradeEvolution(Plugin plugin, EvolutionManager manager) {
        this.manager = manager;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public EvolutionType getType() {
        return EvolutionType.SWORD_UPGRADE;
    }

    @Override
    public Material getIcon() {
        return Material.DIAMOND_SWORD;
    }

    @Override
    public String getName() {
        return "§dL'évolution de la vie";
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList(
                "",

                "§7A chaque kill, vous avez une chance d'améliorer",
                "§7votre épée au niveau supérieur",
                "",
                "§7L'évolution se fait de manière incrémentielle,",
                "§7votre épée passera donc d'abord par",
                "§7la pierre, le fer,... avant d'atteindre",
                "§7des niveaux supérieurs");
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public List<EvolutionLevel> getLevels() {
        EvolutionLevel level1 = new EvolutionLevel(1, new ArrayList<>(Arrays.asList("", "§d80% §7de chance d'améliorer votre épée", "§7vers une §dépée en pierre §7suite à un kill", "", "§7+5 dégâts")), 400);
        EvolutionLevel level2 = new EvolutionLevel(2, new ArrayList<>(Arrays.asList("", "§d60% §7de chance d'améliorer votre épée", "§7vers une §dépée en fer §7suite à un kill", "", "§7+6 dégâts")), 800);
        EvolutionLevel level3 = new EvolutionLevel(3, new ArrayList<>(Arrays.asList("", "§d40% §7de chance d'améliorer votre épée", "§7vers une §dépée en diamand §7suite à un kill", "", "§7+7 dégâts")), 2500);
        EvolutionLevel level4 = new EvolutionLevel(4, new ArrayList<>(Arrays.asList("", "§d10% §7de chance d'améliorer votre épée", "§7vers une §dépée en diamand tranchant 1 §7suite à un kill", "", "§7+8.25 dégâts")), 5000);
        EvolutionLevel level5 = new EvolutionLevel(5, new ArrayList<>(Arrays.asList("", "§d5% §7de chance d'améliorer votre épée", "§7vers une §dépée en diamand tranchant 2 §7suite à un kill", "", "§7+9.5 dégâts")), 10000);

        return Arrays.asList(level1, level2, level3, level4, level5);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onKill(PlayerDeathEvent e) {
        Player p = e.getEntity().getKiller();
        if (p == null) {
            return;
        }

        int level = manager.getPlayerEvolutionLevel(p.getUniqueId(), this.getType());
        int highestSwordLevel = getHighestSwordLevel(Arrays.asList(p.getInventory().getContents()));

        if (highestSwordLevel >= level) {
            return;
        }

        Random rand = new Random();
        int randomValue = rand.nextInt(99);
        int upgradeChance = getLevelUpgradeChance(highestSwordLevel + 1);
        if (upgradeChance == 0 || randomValue >= upgradeChance) {
            return;
        }

        // Do the actual upgrade
        ItemStack highestSword = getHighestSword(Arrays.asList(p.getInventory().getContents()));

        switch (highestSwordLevel) {
            case 0:
                highestSword.setType(Material.STONE_SWORD);
                break;
            case 1:
                highestSword.setType(Material.IRON_SWORD);
                break;
            case 2:
                highestSword.setType(Material.DIAMOND_SWORD);
                break;
            case 3:
                highestSword.setType(Material.DIAMOND_SWORD);
                highestSword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
                break;
            case 4:
                highestSword.setType(Material.DIAMOND_SWORD);
                highestSword.addEnchantment(Enchantment.DAMAGE_ALL, 2);
                break;
        }

        // Highest level is 5, don't show upgrade message because there is nothing to upgrade to
        if (highestSwordLevel == 5) {
            return;
        }

        p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 1, 1);
        p.sendMessage("§dTon épée a été upgrade au niveau suivant!");
    }

    private int getLevelUpgradeChance(int level) {
        switch (level) {
            case 1:
                return 80;
            case 2:
                return 60;
            case 3:
                return 40;
            case 4:
                return 10;
            case 5:
                return 5;
            default:
                return 0;
        }
    }

    private int getSwordLevel(ItemStack sword) {
        if (sword == null) {
            return 0;
        }
        Material material = sword.getType();

        switch (material) {
            case WOOD_SWORD:
                return 0;
            case STONE_SWORD:
                return 1;
            case IRON_SWORD:
                return 2;
            case DIAMOND_SWORD:
                int sharpnessEnchantLevel = sword.getEnchantmentLevel(Enchantment.DAMAGE_ALL);
                if (sharpnessEnchantLevel == 0) {
                    return 3;
                }
                if (sharpnessEnchantLevel == 1) {
                    return 4;
                }
                if (sharpnessEnchantLevel == 2) {
                    return 5;
                }
                return 0;
            default:
                return 0;
        }
    }

    private int getHighestSwordLevel(List<ItemStack> inventory) {
        ItemStack highestSword = getHighestSword(inventory);

        return getSwordLevel(highestSword);
    }

    private ItemStack getHighestSword(List<ItemStack> inventory) {
        List<Material> swordMaterials = Arrays.asList(Material.WOOD_SWORD, Material.STONE_SWORD, Material.IRON_SWORD, Material.DIAMOND_SWORD);
        List<ItemStack> swords = new ArrayList<>();
        for (ItemStack item : inventory) {
            if (item == null) {
                continue;
            }
            if (swordMaterials.contains(item.getType())) {
                swords.add(item);
            }
        }

        ItemStack currentBestSword = null;
        int currentMaxLevel = 0;

        for (ItemStack sword : swords) {
            int level = getSwordLevel(sword);
            if (level >= currentMaxLevel) {
                currentMaxLevel = level;
                currentBestSword = sword;
            }
        }

        return currentBestSword;
    }
}
