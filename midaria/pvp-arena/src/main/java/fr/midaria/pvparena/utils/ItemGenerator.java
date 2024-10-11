package fr.midaria.pvparena.utils;

import com.cryptomorin.xseries.XMaterial;
import fr.midaria.pvparena.Main;
import fr.minuskube.inv.ClickableItem;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemGenerator {
    public static ItemStack createItem(Material material, String title) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(title);
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack createItem(Material material, String title, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(title);
        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack createItem(XMaterial xMaterial, String title, List<String> lore) {
        ItemStack item = xMaterial.parseItem();
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(title);
        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    public static ClickableItem createBuyableItem(ItemStack item, double price) {
        ItemStack shopItem = item.clone();
        ItemMeta meta = shopItem.getItemMeta();
        List<String> lore;
        if (meta.getLore() == null) {
            lore = new ArrayList<>();
        } else {
            lore = meta.getLore();
        }
        lore.add("");
        lore.add("§7Acheter pour §d" + price + "$");
        meta.setLore(lore);
        shopItem.setItemMeta(meta);

        ClickableItem clickableItem = ClickableItem.of(shopItem, (e) -> {
            Player p = (Player) e.getWhoClicked();
            boolean canAfford = Main.getInstance().getEcon().has(p, price);

            if (!canAfford) {
                p.sendMessage("§cVous n'avez pas assez d'argent pour acheter cet article");
                p.playSound(p.getLocation(), Sound.NOTE_BASS, 1, 0);
                return;
            }

            Main.getInstance().getEcon().withdrawPlayer(p, price);
            p.getInventory().addItem(item);
            p.sendMessage("§7Nous avons débité §d" + price + " §7de votre compte");
            p.playSound(p.getLocation(), Sound.NOTE_PLING, 2, 2);
        });

        return clickableItem;
    }

    public static ItemStack getBackItem() {
        return createItem(Material.REDSTONE, "§cRetour");
    }
}
