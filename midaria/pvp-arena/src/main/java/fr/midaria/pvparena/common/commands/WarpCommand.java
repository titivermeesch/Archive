package fr.midaria.pvparena.common.commands;

import fr.midaria.pvparena.common.gui.WarpMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player p = (Player) sender;
        if (args.length == 1) {
            p.performCommand("essentials:warp " + args[0]);
            return true;
        }

        new WarpMenu().INVENTORY.open(p);
        return true;
    }
}
