package fr.midaria.pvparena.common.commands;

import fr.midaria.pvparena.Main;
import fr.midaria.pvparena.mechanics.koth.KothInstance;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class MainCommands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("No console allowed");
            return true;
        }

        Player p = (Player) sender;

        if (!p.hasPermission("midariapvp.admin")) {
            return true;
        }

        if (args.length == 1) {
            String arg = args[0];
            if (arg.equalsIgnoreCase("refreshpvpleaderboard")) {
                Main.getInstance().getStatsManager().reloadStatsHologram();
                p.sendMessage("§7PVP Leaderboard reloaded");
                return true;
            }

            if (arg.equalsIgnoreCase("setpvpleaderboard")) {
                Location loc = p.getLocation();
                FileConfiguration config = Main.getInstance().getConfig();
                config.set("pvpleaderboard.x", loc.getX());
                config.set("pvpleaderboard.y", loc.getY());
                config.set("pvpleaderboard.z", loc.getZ());
                Main.getInstance().getStatsManager().moveHologram(loc);
                p.sendMessage("§7PVP Leaderboard moved");
                return true;
            }

            if (arg.equalsIgnoreCase("setmoneyleaderboard")) {
                Location loc = p.getLocation();
                FileConfiguration config = Main.getInstance().getConfig();
                config.set("moneyleaderboard.x", loc.getX());
                config.set("moneyleaderboard.y", loc.getY());
                config.set("moneyleaderboard.z", loc.getZ());
                Main.getInstance().getEconomyManager().moveHologram(loc);
                p.sendMessage("§7Economy Leaderboard moved");
                return true;
            }

            if (arg.equalsIgnoreCase("kothpos1")) {
                Location loc = p.getLocation();
                FileConfiguration config = Main.getInstance().getConfig();
                config.set("koth.pos1.x", loc.getX());
                config.set("koth.pos1.y", loc.getY());
                config.set("koth.pos1.z", loc.getZ());
                p.sendMessage("§7KOTH position 1 set");
                return true;
            }

            if (arg.equalsIgnoreCase("kothpos2")) {
                Location loc = p.getLocation();
                FileConfiguration config = Main.getInstance().getConfig();
                config.set("koth.pos2.x", loc.getX());
                config.set("koth.pos2.y", loc.getY());
                config.set("koth.pos2.z", loc.getZ());
                p.sendMessage("§7KOTH position 2 set");
                return true;
            }

            if (arg.equalsIgnoreCase("setkothleaderboard")) {
                Location loc = p.getLocation();
                FileConfiguration config = Main.getInstance().getConfig();
                config.set("kothleaderboard.x", loc.getX());
                config.set("kothleaderboard.y", loc.getY());
                config.set("kothleaderboard.z", loc.getZ());
                p.sendMessage("§7KOTH Leaderboard moved");
                return true;
            }

            if (arg.equalsIgnoreCase("chestpos1")) {
                Location loc = p.getLocation();
                FileConfiguration config = Main.getInstance().getConfig();
                config.set("chestevent.pos1.x", loc.getX());
                config.set("chestevent.pos1.y", loc.getY());
                config.set("chestevent.pos1.z", loc.getZ());
                p.sendMessage("§7Chest event position 1 set");
                return true;
            }

            if (arg.equalsIgnoreCase("chestpos2")) {
                Location loc = p.getLocation();
                FileConfiguration config = Main.getInstance().getConfig();
                config.set("chestevent.pos2.x", loc.getX());
                config.set("chestevent.pos2.y", loc.getY());
                config.set("chestevent.pos2.z", loc.getZ());
                p.sendMessage("§7Chest event position 2 set");
                return true;
            }

            if (arg.equalsIgnoreCase("spawnchest")) {
                Main.getInstance().getRewardChestManager().spawnChest();
                p.sendMessage("§7Chest spawned");
                return true;
            }

            if (arg.equalsIgnoreCase("startkoth")) {
                KothInstance koth = new KothInstance(Main.getInstance().getKothManager(), p.getItemInHand());
                Main.getInstance().getKothManager().setActiveKoth(koth);
                p.sendMessage("§7Un KOTH a été lancé");
            }
        }

        if (args.length == 2) {
            String arg1 = args[0];
            String arg2 = args[1];

            if (arg1.equalsIgnoreCase("setnextkoth")) {
                int value = Integer.parseInt(arg2);
                Main.getInstance().getKothManager().setNextKoth(value);
                p.sendMessage("§7KOTH time has been updated");
                return true;
            }

            if (arg1.equalsIgnoreCase("setnextchest")) {
                int value = Integer.parseInt(arg2);
                Main.getInstance().getRewardChestManager().setNextChest(value);
                p.sendMessage("§7Chest event time has been updated");
                return true;
            }
        }
        return true;
    }
}
