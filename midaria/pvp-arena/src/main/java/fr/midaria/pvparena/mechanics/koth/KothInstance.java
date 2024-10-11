package fr.midaria.pvparena.mechanics.koth;

import fr.midaria.pvparena.utils.Cuboid;
import fr.midaria.pvparena.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.stream.Collectors;

public class KothInstance {
    private final KothManager manager;
    private final ItemStack rewardItem;
    private final Map<Player, Integer> leaderboard;
    private KothStatus status = KothStatus.NOT_STARTED;
    private int duration = 300;
    private int maxPlayerJoinSeconds = 30;
    private final Cuboid arena;

    public KothInstance(KothManager manager, ItemStack rewardItem) {
        this.manager = manager;
        this.rewardItem = rewardItem;
        this.leaderboard = new HashMap<>();
        FileConfiguration config = Main.getInstance().getConfig();
        ConfigurationSection kothPos1 = config.getConfigurationSection("koth.pos1");
        ConfigurationSection kothPos2 = config.getConfigurationSection("koth.pos2");
        this.arena = new Cuboid(
                Bukkit.getWorld("world"),
                kothPos1.getInt("x"),
                kothPos1.getInt("y"),
                kothPos1.getInt("z"),
                kothPos2.getInt("x"),
                kothPos2.getInt("y"),
                kothPos2.getInt("z")
        );
        prepareStart();
    }

    public Map<Player, Integer> getLeaderboard() {
        return leaderboard;
    }

    public Player getTopPlayer() {
        if (leaderboard.isEmpty()) {
            return null;
        }
        Player topPlayer = null;
        int score = 0;

        for (Player p : leaderboard.keySet()) {
            int pScore = leaderboard.get(p);
            if (topPlayer == null) {
                topPlayer = p;
                score = pScore;
            }

            if (pScore > score) {
                topPlayer = p;
                score = pScore;
            }
        }

        return topPlayer;
    }

    public void sendMessageToParticipants(String msg) {
        for (Player p : leaderboard.keySet()) {
            p.sendMessage(msg);
        }
    }

    public void prepareStart() {
        status = KothStatus.WAITING_PLAYERS;
        final int minPlayers = Main.getInstance().getConfig().getInt("minkothplayers");
        Bukkit.broadcastMessage("§dUn KOTH va bientôt commencer en §5-12, 91§d. La partie se lancera si au moins §52 joueurs §dsont dans la zone");
        Bukkit.getScheduler().runTaskTimer(Main.getInstance(), new TimerTask() {
            @Override
            public void run() {
                if (!status.equals(KothStatus.WAITING_PLAYERS)) {
                    return;
                }


                List<Player> players = getPlayersInKothArena();
                if (players.size() >= minPlayers) {
                    for (Player p : players) {
                        leaderboard.put(p, 0);
                    }
                    status = KothStatus.ONGOING;
                    start();
//                    new KothTask(KothInstance.this).runTaskTimer(Main.getInstance(), 0, 20);
                    return;
                }

                if (maxPlayerJoinSeconds == 10) {
                    Bukkit.broadcastMessage("§7Le KOTH sera annulé dans 10 secondes si moins de 5 personnes se trouvent dans la zone");
                }

                if (maxPlayerJoinSeconds == 5) {
                    Bukkit.broadcastMessage("§7Le KOTH sera annulé dans 5 secondes si moins de 5 personnes se trouvent dans la zone");
                }

                if (maxPlayerJoinSeconds == 0) {
                    Bukkit.broadcastMessage("§cLe KOTH a été annulé par manque de joueurs");
                    setStatus(KothStatus.NOT_STARTED);
                    Main.getInstance().getKothManager().setActiveKoth(null);
                }

                maxPlayerJoinSeconds--;
            }
        }, 0, 5);
    }

    public List<Player> getPlayersInKothArena() {
        return Bukkit.getOnlinePlayers()
                .stream()
                .filter(p -> arena.contains(p.getLocation()))
                .collect(Collectors.toList());
    }

    public void start() {
        Bukkit.broadcastMessage("§dLe KOTH vient de commencer en §5-12, 91");
        Bukkit.getScheduler().runTaskTimer(Main.getInstance(), new TimerTask() {
            @Override
            public void run() {
                if (!status.equals(KothStatus.ONGOING)) {
                    return;
                }

                List<Player> playersInKoth = getPlayersInKothArena();
                Player previousTopPlayer = getTopPlayer();
                playersInKoth.forEach(p -> leaderboard.put(p, leaderboard.getOrDefault(p, 0) + 1));
                Player newTopPlayer = getTopPlayer();

                if (!previousTopPlayer.getUniqueId().equals(newTopPlayer.getUniqueId())) {
                    sendMessageToParticipants("§dLe joueur §5" + newTopPlayer.getDisplayName() + " §dvient de dépasser §5" + previousTopPlayer.getDisplayName() + " §dau KOTH");
                }

                if (duration == 45) {
                    Bukkit.broadcastMessage("§dIl reste 45 secondes pour gagner le KOTH. Le top joueur est §5" + newTopPlayer.getDisplayName());
                }

                if (duration == 30) {
                    Bukkit.broadcastMessage("§dIl reste 30 secondes pour gagner le KOTH. Le top joueur est §5" + newTopPlayer.getDisplayName());
                }

                if (duration == 15) {
                    Bukkit.broadcastMessage("§dIl reste 15 secondes pour gagner le KOTH. Le top joueur est §5" + newTopPlayer.getDisplayName());
                }

                duration--;

                if (duration <= 0) {
                    Bukkit.broadcastMessage("§dLe KOTH est terminé. Le gagnant est §5" + newTopPlayer.getDisplayName() + " §d et vient de recevoir sa récompense");
                    newTopPlayer.getInventory().addItem(rewardItem);
                    newTopPlayer.sendMessage("§7Tu viens de recevoir ta récompense KOTH et §d50$");
                    PotionEffect absorptionEffect = new PotionEffect(PotionEffectType.ABSORPTION, 60, 0);
                    PotionEffect regenEffect = new PotionEffect(PotionEffectType.REGENERATION, 100, 2);
                    newTopPlayer.addPotionEffect(absorptionEffect);
                    newTopPlayer.addPotionEffect(regenEffect);
                    Main.getInstance().getEcon().depositPlayer(newTopPlayer, 50);
                    manager.setActiveKoth(null);
                    status = KothStatus.NOT_STARTED;
                    leaderboard.clear();
                }
            }
        }, System.currentTimeMillis() % 20, 20);
    }

    public void setStatus(KothStatus status) {
        this.status = status;
    }
}
