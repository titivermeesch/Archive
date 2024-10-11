package fr.midaria.pvparena.mechanics.koth;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Random;

public class KothManager {
    private KothInstance activeKoth;
    private int nextKoth = 3300; // 55 minutes
    private final ArrayList<ItemStack> rewards = new ArrayList<>();

    public KothManager(Plugin plugin) {
        rewards.add(new ItemStack(Material.ARROW, 15));
        rewards.add(new ItemStack(Material.IRON_HELMET));
        rewards.add(new ItemStack(Material.IRON_CHESTPLATE));
        ItemStack ironLegging = new ItemStack(Material.IRON_LEGGINGS);
        ironLegging.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        rewards.add(ironLegging);
        ItemStack ironBoots = new ItemStack(Material.IRON_BOOTS);
        ironBoots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        rewards.add(ironBoots);
        ItemStack bow = new ItemStack(Material.BOW);
        bow.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
        rewards.add(bow);
        rewards.add(new ItemStack(Material.STONE_SWORD));
        rewards.add(new ItemStack(Material.IRON_SWORD));

        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            nextKoth--;
            if (nextKoth == 300) {
                Bukkit.broadcastMessage("§dUn KOTH se lance dans 5 minutes");
            }
            if (nextKoth == 60) {
                Bukkit.broadcastMessage("§dUn KOTH se lance dans 1 minute");
            }
            if (nextKoth == 0) {
                nextKoth = 3600;
                if (activeKoth != null) {
                    Bukkit.broadcastMessage("§cLe KOTH n'a pas pu être lancé car le précédent KOTH n'est pas encore termineé");
                    return;
                }

                if (Bukkit.getOnlinePlayers().size() >= 5) {
                    activeKoth = new KothInstance(this, getRandomReward());
                    activeKoth.prepareStart();
                    return;
                }
                
                Bukkit.broadcastMessage("§cLe KOTH n'a pas pu être lancé à cause d'un manque de joueurs");
            }
        }, 0, 20);
    }

    private ItemStack getRandomReward() {
        Random rand = new Random();
        return rewards.get(rand.nextInt(rewards.size()));
    }

    public KothInstance getActiveKoth() {
        return activeKoth;
    }

    public void setActiveKoth(KothInstance activeKoth) {
        this.activeKoth = activeKoth;
    }

    public int getNextKoth() {
        return nextKoth;
    }

    public void setNextKoth(int nextKoth) {
        this.nextKoth = nextKoth;
    }
}
