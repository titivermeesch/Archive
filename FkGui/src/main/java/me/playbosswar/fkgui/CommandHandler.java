package me.playbosswar.fkgui;

import me.playbosswar.fkgui.gui.MainMenu;
import me.playbosswar.fkgui.utils.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Command can only be executed by a player");
            return true;
        }

        Player p = (Player) sender;

        if (!p.hasPermission("fkgui.use")) {
            Messages.sendMessage(p, "&cYou don't have enough permissions to do this");
            return true;
        }

        new MainMenu().INVENTORY.open(p);
        return true;
    }
}
