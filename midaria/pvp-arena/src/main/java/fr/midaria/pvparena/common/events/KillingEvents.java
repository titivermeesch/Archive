package fr.midaria.pvparena.common.events;

import fr.midaria.pvparena.Main;
import fr.midaria.pvparena.common.utils.KitsUtility;
import fr.midaria.pvparena.common.utils.RankingUtils;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class KillingEvents implements Listener {
    @EventHandler
    public void onKill(PlayerDeathEvent e) {
        Player victim = e.getEntity().getPlayer();
        Player killer = e.getEntity().getKiller();

        if (killer != null) {
            int multiplier = RankingUtils.getPlayerMultiplier(killer);
            EconomyResponse r = Main.getInstance().getEcon().depositPlayer(killer, multiplier);
            if (multiplier > 1) {
                killer.sendMessage("§d+$" + multiplier + " §7(Multiplicateur de gains x" + multiplier + ")");
            } else {
                killer.sendMessage("§d+$" + multiplier);
            }

            killer.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE));
            PotionEffect absorptionEffect = new PotionEffect(PotionEffectType.ABSORPTION, 60, 0);
            PotionEffect regenEffect = new PotionEffect(PotionEffectType.REGENERATION, 60, 2);
            killer.addPotionEffect(absorptionEffect);
            killer.addPotionEffect(regenEffect);
            killer.setStatistic(Statistic.PLAYER_KILLS, killer.getStatistic(Statistic.PLAYER_KILLS) + 1);
        }

        victim.getInventory().clear();
        victim.performCommand("spawn");
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> victim.spigot().respawn(), 4L);
//        victim.spigot().respawn();
        KitsUtility.loadKitOnPlayer(victim);
        Main.getInstance().getHealStickManager().getCooldowns().put(victim, 0);
    }
}
