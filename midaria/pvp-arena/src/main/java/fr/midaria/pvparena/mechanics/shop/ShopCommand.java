package fr.midaria.pvparena.mechanics.shop;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShopCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("§cYou need to be a player, or specify a player as argument");
                return true;
            }

            Player p = (Player) sender;
            if (!p.hasPermission("midariapvp.shop")) {
                p.sendMessage("§cVous n'avez pas les permissions pour faire cette commande");
                return true;
            }

            new MainShopInventoryProvider().INVENTORY.open(p);
            return true;
        }

        if (args.length == 1) {
            if (!sender.hasPermission("midariapvp.admin")) {
                sender.sendMessage("§cVous n'avez pas les permissions pour faire cette commande");
                return true;
            }

            String playerName = args[0];
            if (Bukkit.getPlayer(playerName) == null) {
                sender.sendMessage("§cCe joueur n'est pas en ligne");
                return true;
            }

            Player shopPlayer = Bukkit.getPlayer(playerName);
            new MainShopInventoryProvider().INVENTORY.open(shopPlayer);
            return true;
        }

        return true;
    }
}
