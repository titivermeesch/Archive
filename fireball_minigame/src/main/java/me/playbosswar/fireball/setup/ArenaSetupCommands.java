package me.playbosswar.fireball.setup;

import me.playbosswar.fireball.game.Arena;
import me.playbosswar.fireball.game.GameManager;
import me.playbosswar.fireball.worlds.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ArenaSetupCommands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to use this command.");
            return true;
        }

        if (!sender.hasPermission("fireball.setup")) {
            sender.sendMessage("You do not have permission to use this command.");
            return true;
        }

        Player p = (Player) sender;

        if (args.length == 0) {
            p.sendMessage("Usage: /fba create <name>");
            p.sendMessage("Usage: /fba setminplayers <name> <number>");
            p.sendMessage("Usage: /fba setmaxplayers <name> <number>");
            p.sendMessage("Usage: /fba setteams <name> <number>");
            p.sendMessage("Usage: /fba setupperbound <name>");
            p.sendMessage("Usage: /fba setlowerbound <name>");
            p.sendMessage("Usage: /fba addspawnpoint <name>");
            p.sendMessage("Usage: /fba removespawnpoint <name> <number>");
            p.sendMessage("Usage: /fba enable <name>");
            p.sendMessage("Usage: /fba disable <name>");
            p.sendMessage("Usage: /fba list");
            return true;
        }

        if (args[0].equalsIgnoreCase("create")) {
            if (args.length != 2) {
                p.sendMessage("Usage: /arena create <name>");
                return true;
            }

            Arena existingArena = GameManager.getInstance().getArenaByName(args[1]);
            if (existingArena != null) {
                p.sendMessage("An arena with that name already exists.");
                return true;
            }

            boolean loaded = WorldManager.createWorld(args[1]);
            if (!loaded) {
                p.sendMessage("Failed to create arena");
                return true;
            }

            GameManager.getInstance().createArena(args[1]);
            p.teleport(new Location(Bukkit.getWorld(args[1]), 0, 64, 0));
            p.sendMessage("Arena created");
            return true;
        }

        if (args[0].equalsIgnoreCase("setminplayers")) {
            if (args.length != 3) {
                p.sendMessage("Usage: /arena setminplayers <name> <number>");
                return true;
            }

            Arena arena = GameManager.getInstance().getArenaByName(args[1]);
            if (arena == null) {
                p.sendMessage("That arena does not exist.");
                return true;
            }

            arena.setMinPlayers(Integer.parseInt(args[2]));
            p.sendMessage("Min players set to " + args[2]);
            return true;
        }

        if (args[0].equalsIgnoreCase("setmaxplayers")) {
            if (args.length != 3) {
                p.sendMessage("Usage: /arena setmaxplayers <name> <number>");
                return true;
            }

            Arena arena = GameManager.getInstance().getArenaByName(args[1]);
            if (arena == null) {
                p.sendMessage("That arena does not exist.");
                return true;
            }
            arena.setMaxPlayers(Integer.parseInt(args[2]));
            p.sendMessage("Max players set to " + args[2]);
            return true;
        }

        if (args[0].equalsIgnoreCase("setteams")) {
            if (args.length != 3) {
                p.sendMessage("Usage: /arena setteams <name> <number>");
                return true;
            }

            Arena arena = GameManager.getInstance().getArenaByName(args[1]);
            if (arena == null) {
                p.sendMessage("That arena does not exist.");
                return true;
            }

            arena.setTeams(Integer.parseInt(args[2]));
            p.sendMessage("Teams set to " + args[2]);
            return true;
        }

        if (args[0].equalsIgnoreCase("setupperbound")) {
            if (args.length != 2) {
                p.sendMessage("Usage: /arena setupperbound <name>");
                return true;
            }

            Arena arena = GameManager.getInstance().getArenaByName(args[1]);
            if (arena == null) {
                p.sendMessage("That arena does not exist.");
                return true;
            }

            arena.setUpperBound(p.getLocation());
            p.sendMessage("Upper bound set");
            return true;
        }

        if (args[0].equalsIgnoreCase("setlowerbound")) {
            if (args.length != 2) {
                p.sendMessage("Usage: /arena setlowerbound <name>");
                return true;
            }

            Arena arena = GameManager.getInstance().getArenaByName(args[1]);
            if (arena == null) {
                p.sendMessage("That arena does not exist.");
                return true;
            }

            arena.setLowerBound(p.getLocation());
            p.sendMessage("Lower bound set");
            return true;
        }

        if (args[0].equalsIgnoreCase("addspawnpoint")) {
            if (args.length != 2) {
                p.sendMessage("Usage: /arena addspawnpoint <name>");
                return true;
            }

            Arena arena = GameManager.getInstance().getArenaByName(args[1]);
            if (arena == null) {
                p.sendMessage("That arena does not exist.");
                return true;
            }

            arena.addSpawnPoint(p.getLocation());
            p.sendMessage("Spawn point added");
            return true;
        }

        if (args[0].equalsIgnoreCase("removespawnpoint")) {
            if (args.length != 3) {
                p.sendMessage("Usage: /arena removespawnpoint <name> <number>");
                return true;
            }

            Arena arena = GameManager.getInstance().getArenaByName(args[1]);
            if (arena == null) {
                p.sendMessage("That arena does not exist.");
                return true;
            }

            int index = Integer.parseInt(args[2]);
            if (index < 0 || index >= arena.getSpawnPoints().size()) {
                p.sendMessage("Invalid spawn point index");
                return true;
            }

            arena.removeSpawnPoint(index);
            p.sendMessage("Spawn point removed");
            return true;
        }

        if (args[0].equalsIgnoreCase("enable")) {
            if (args.length != 2) {
                p.sendMessage("Usage: /arena enable <name>");
                return true;
            }

            Arena arena = GameManager.getInstance().getArenaByName(args[1]);
            if (arena == null) {
                p.sendMessage("That arena does not exist.");
                return true;
            }

            if (!arena.isComplete()) {
                p.sendMessage("Arena is not complete");
                return true;
            }

            arena.setActive(true);
            p.sendMessage("Arena is now enabled");
            return true;
        }

        if (args[0].equalsIgnoreCase("disable")) {
            if (args.length != 2) {
                p.sendMessage("Usage: /arena disable <name>");
                return true;
            }

            Arena arena = GameManager.getInstance().getArenaByName(args[1]);
            if (arena == null) {
                p.sendMessage("That arena does not exist.");
                return true;
            }

            arena.setActive(false);
            p.sendMessage("Arena is now disabled");
            return true;
        }

        if (args[0].equalsIgnoreCase("list")) {
            p.sendMessage("Arenas:");
            for (Arena arena : GameManager.getInstance().getLoadedArenas()) {
                p.sendMessage(arena.getName());
            }
            return true;
        }

        return true;
    }
}
