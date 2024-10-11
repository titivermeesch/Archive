package fr.midaria.pvparena.common.utils;

import fr.midaria.pvparena.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class KitsUtility {
    public static void loadKitOnPlayer(Player p) {
        List<ItemStack> items = new ArrayList<>();
        items.add(ItemUtils.createUnbreakableItem(Material.WOOD_SWORD, "§aÉpée en bois"));
        items.add(new ItemStack(Material.BOW));
        items.add(ItemUtils.createItem(Material.COOKED_BEEF, "§aBoeuf cuit", 64));
        items.add(new ItemStack(Material.ARROW, 5));

        List<ItemStack> armor = new ArrayList<>();
        armor.add(ItemUtils.createUnbreakableItem(Material.IRON_BOOTS, "§aBottes en fer"));
        armor.add(ItemUtils.createUnbreakableItem(Material.IRON_LEGGINGS, "§aJambières en fer"));
        armor.add(ItemUtils.createUnbreakableItem(Material.CHAINMAIL_CHESTPLATE, "§aPlastron en chaine"));
        armor.add(ItemUtils.createUnbreakableItem(Material.CHAINMAIL_HELMET, "§aCasque en chaine"));

        p.getInventory().addItem(items.toArray(new ItemStack[]{}));
        p.getInventory().setItem(8, Main.getInstance().getHealStickManager().getStick());
        p.getInventory().setArmorContents(armor.toArray(new ItemStack[]{}));
    }
}
