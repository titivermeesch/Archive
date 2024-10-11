package fr.midaria.pvparena.mechanics.chest;

import fr.midaria.pvparena.utils.Cuboid;
import fr.midaria.pvparena.Main;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RewardChestManager implements Listener {
    private final Plugin plugin;
    private int nextChest = 1200;
    private final List<Location> chestLocations = new ArrayList<>();
    private final List<ItemStack> rewards = new ArrayList<>();

    public RewardChestManager(Plugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        rewards.add(new ItemStack(Material.ARROW, 5));
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
        rewards.add(new ItemStack(Material.DIAMOND_SWORD));
        rewards.add(new ItemStack(Material.SNOW_BALL, 16));
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            nextChest--;
            if (nextChest == 300) {
                Bukkit.broadcastMessage("§dUn largage de coffre se lance dans 5 minutes");
            }
            if (nextChest == 60) {
                Bukkit.broadcastMessage("§dUn largage de coffre se lance dans 1 minute");
            }
            if (nextChest == 0) {
                nextChest = 1200;
                if (Bukkit.getOnlinePlayers().size() >= 3) {
                    spawnChest();
                } else {
                    Bukkit.broadcastMessage("§cLe largage n'a pas spawn par manque de joueurs");
                }
            }
        }, 0, 20);
    }

    private List<Block> getPossibleLocation() {
        FileConfiguration config = Main.getInstance().getConfig();
        ConfigurationSection pos1Section = config.getConfigurationSection("chestevent.pos1");
        ConfigurationSection pos2Section = config.getConfigurationSection("chestevent.pos2");
        Cuboid cuboid = new Cuboid(Bukkit.getWorld("world"), pos1Section.getInt("x"), pos1Section.getInt("y"), pos1Section.getInt("z"), pos2Section.getInt("x"), pos2Section.getInt("y"), pos2Section.getInt("z"));

        List<Block> possibleBlocks = new ArrayList<>();
        for (Block b : cuboid.getBlocks()) {
            List<Material> spawnableMaterials = new ArrayList<>();
            spawnableMaterials.add(Material.GRASS);
            spawnableMaterials.add(Material.HARD_CLAY);
            spawnableMaterials.add(Material.STAINED_CLAY);
            spawnableMaterials.add(Material.COBBLESTONE);
            spawnableMaterials.add(Material.GRAVEL);

            if (spawnableMaterials.contains(b.getType())) {
                Location bLoc = b.getLocation();
                Block top1Block = bLoc.getWorld().getBlockAt(bLoc.add(0, 1, 0));
                Block top2Block = bLoc.getWorld().getBlockAt(bLoc.add(0, 2, 0));
                if (top1Block.getType() == Material.AIR && top2Block.getType() == Material.AIR) {
                    possibleBlocks.add(b);
                }
            }
        }

        return possibleBlocks;
    }

    public void spawnChest() {
        List<Block> possibleBlocks = getPossibleLocation();

        Random rand = new Random();
        Block selectedBlock = possibleBlocks.get(rand.nextInt(possibleBlocks.size()));
        Location loc = selectedBlock.getLocation().add(0, 1, 0);
        chestLocations.add(loc);

        Bukkit.broadcastMessage("§dUn largage tombe aux cooordonnées suivantes: x:" + (int) loc.getX() + " z:" + (int) loc.getZ());

        Bukkit.getScheduler().runTaskTimer(Main.getInstance(), new Runnable() {
            int height = 180;

            @Override
            public void run() {
                height--;
                if (height > loc.getY()) {
                    Location entityLoc = new Location(Bukkit.getWorld("world"), loc.getX(), height, loc.getZ());
                    Firework fw = (Firework) entityLoc.getWorld().spawnEntity(entityLoc, EntityType.FIREWORK);
                    FireworkMeta fwm = fw.getFireworkMeta();
                    FireworkEffect effect = FireworkEffect.builder().flicker(true).withColor(Color.PURPLE).with(FireworkEffect.Type.BURST).trail(false).build();

                    fwm.addEffect(effect);
                    fwm.setPower(2);
                    fw.setFireworkMeta(fwm);

                    Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), fw::detonate, 2);
                }

                if (height == loc.getY()) {
                    Random rand = new Random();
                    ItemStack reward = rewards.get(rand.nextInt(rewards.size()));
                    Bukkit.getWorld("world").getBlockAt(loc).setType(Material.CHEST);
                    Chest chest = (Chest) Bukkit.getWorld("world").getBlockAt(loc).getState();
                    chest.getBlockInventory().setItem(13, reward);
                    chest.update();

                    for (int i = 0; i < 20; i++) {
                        Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
                        FireworkMeta fwm = fw.getFireworkMeta();
                        FireworkEffect effect = FireworkEffect.builder().flicker(true).withColor(Color.PURPLE).with(FireworkEffect.Type.BALL_LARGE).trail(false).build();

                        fwm.addEffect(effect);
                        fwm.setPower(16);
                        fw.setFireworkMeta(fwm);

                        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), fw::detonate, 2);
                        i++;
                    }

                }
            }
        }, 0, 2);
    }

    public int getNextChest() {
        return nextChest;
    }

    public void setNextChest(int nextChest) {
        this.nextChest = nextChest;
    }

    @EventHandler
    public void onChest(InventoryCloseEvent e) {
        if (!e.getInventory().getType().equals(InventoryType.CHEST)) {
            return;
        }

        if (e.getInventory().getHolder() instanceof Player) {
            return;
        }

        Chest chest = (Chest) e.getInventory().getHolder();
        if (!chestLocations.contains(chest.getLocation())) {
            return;
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            chestLocations.remove(chest.getLocation());
            chest.getLocation().getWorld().getBlockAt(chest.getLocation()).setType(Material.AIR);
        }, 60);
    }
}
