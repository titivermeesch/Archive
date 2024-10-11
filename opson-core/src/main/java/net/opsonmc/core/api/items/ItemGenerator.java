package net.opsonmc.core.api.items;

import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemGenerator {
    public static ItemStack generateItem(Material material, String title) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        String coloredTitle = ChatColor.translateAlternateColorCodes('&', title);
        meta.displayName(Component.text(coloredTitle));
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack generateItem(Material material, String title, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        String coloredTitle = ChatColor.translateAlternateColorCodes('&', title);
        meta.displayName(Component.text(coloredTitle));
        List<Component> loreComponents = lore.stream().map(l -> Component.text(l).asComponent()).toList();
        item.lore(loreComponents);
        item.setItemMeta(meta);

        return item;
    }
}
