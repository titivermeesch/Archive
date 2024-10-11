package fr.midaria.pvparena.items;

import fr.midaria.pvparena.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

public class HealStickManager implements Listener {
    private final Map<Player, Integer> cooldowns = new HashMap<>();
    private final BukkitTask runner;

    public HealStickManager() {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        this.runner = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), new TimerTask() {
            @Override
            public void run() {
                for (Player p : cooldowns.keySet()) {
                    int c = cooldowns.get(p);
                    if (c < 0) {
                        continue;
                    }

                    cooldowns.put(p, c - 1);
                    p.setLevel(c);
                    p.setExp(c / 60f);
                }
            }
        }, 0, 20);
    }

    public void disable() {
        runner.cancel();
    }

    public ItemStack getStick() {
        ItemStack item = new ItemStack(Material.BLAZE_ROD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§dBâton de soin");
        meta.setLore(Arrays.asList("", "§7Clique-droit"));
        item.setItemMeta(meta);
        return item;
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        cooldowns.remove(e.getPlayer());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        cooldowns.put(e.getPlayer(), 0);
    }

    @EventHandler
    public void onItemClick(PlayerInteractEvent e) {
        if (!e.getAction().equals(Action.RIGHT_CLICK_AIR) && !e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        Player p = e.getPlayer();
        ItemStack item = e.getItem();
        if (item == null) {
            return;
        }

        if (!item.getType().equals(Material.BLAZE_ROD)) {
            return;
        }

        int playerCooldown = cooldowns.getOrDefault(p, 0);
        if (playerCooldown > 0) {
            p.sendMessage("§7Il faut attendre §d" + playerCooldown + "s §7avant de pouvoir réutiliser le bâton");
            return;
        }

        cooldowns.put(p, 60);
        p.setHealth(20);
        p.setFoodLevel(20);
        p.sendMessage("§dVous avez été soigné");
        new ParticleBuilder(ParticleEffect.HEART, p.getLocation())
                .setOffsetY(1f)
                .setOffsetX(1f)
                .setOffsetZ(1f)
                .setAmount(10)
                .setSpeed(0.1f)
                .display();

        Bukkit.getScheduler().runTaskTimer(Main.getInstance(), new Runnable() {
            float i = 1F;

            @Override
            public void run() {
                if (i > 2) {
                    return;
                }
                p.playSound(p.getLocation(), Sound.ORB_PICKUP, 5, i);
                i += 0.5;
            }
        }, 0, 3);
    }

    public Map<Player, Integer> getCooldowns() {
        return cooldowns;
    }
}
