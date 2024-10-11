package me.playbosswar.fkgui.utils;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.function.Consumer;

public class Items {
    public static ItemStack createItem(XMaterial material, String title) {
        ItemStack item = material.parseItem();
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(title);
        item.setItemMeta(meta);

        return item;
    }

    public static ClickableItem createClickableItem(XMaterial material, String title, Consumer<InventoryClickEvent> consumer) {
        ItemStack item = createItem(material, title);
        return ClickableItem.of(item, consumer);
    }

    public static ClickableItem createClickableItem(XMaterial material, String title) {
        ItemStack item = createItem(material, title);
        return ClickableItem.empty(item);
    }

    public static ClickableItem createToggleItem(String title, boolean active, Callback<Boolean> newValue) {
        if (active) {
            return createClickableItem(XMaterial.LIME_STAINED_GLASS_PANE, title, e -> newValue.execute(false));
        }

        return createClickableItem(XMaterial.RED_STAINED_GLASS_PANE, title, e -> newValue.execute(true));
    }

    public static ItemStack getPlayerHead(Player p) {
        ItemStack stack = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
        SkullMeta meta = (SkullMeta) stack.getItemMeta();
        meta.setDisplayName(p.getDisplayName());
        meta.setOwner(p.getName());
        stack.setItemMeta(meta);

        return stack;


    }
}
