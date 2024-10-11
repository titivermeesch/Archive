package fr.midaria.pvparena.mechanics.shop;

import com.cryptomorin.xseries.XMaterial;
import fr.midaria.pvparena.Main;
import fr.midaria.pvparena.utils.ItemGenerator;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;

public class ShopInventoryProvider implements InventoryProvider {
    public SmartInventory INVENTORY;

    public ShopInventoryProvider() {
        INVENTORY = SmartInventory
                .builder()
                .size(4, 9)
                .provider(this)
                .manager(Main.getInstance().getInventoryManager())
                .title("§5§lShop PVP")
                .build();
    }

    @Override
    public void init(Player p, InventoryContents contents) {
        contents.fill(ClickableItem.empty(XMaterial.PURPLE_STAINED_GLASS_PANE.parseItem()));

        double money = Main.getInstance().getEcon().getBalance(p);
        DecimalFormat decimalFormatter = new DecimalFormat("#.##");
        ItemStack moneyItem = ItemGenerator.createItem(Material.GOLD_INGOT, "§e" + decimalFormatter.format(money) + "$");
        contents.set(0, 0, ClickableItem.empty(moneyItem));

        // Armor
        ClickableItem helmet = ItemGenerator.createBuyableItem(new ItemStack(Material.IRON_HELMET), 30);
        contents.set(0, 4, helmet);

        ClickableItem chestplace = ItemGenerator.createBuyableItem(new ItemStack(Material.IRON_CHESTPLATE), 40);
        contents.set(1, 4, chestplace);

        ItemStack leggingsItem = new ItemStack(Material.IRON_LEGGINGS);
        leggingsItem.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        ClickableItem leggings = ItemGenerator.createBuyableItem(leggingsItem, 40);
        contents.set(2, 4, leggings);

        ItemStack bootsItem = new ItemStack(Material.IRON_BOOTS);
        bootsItem.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        ClickableItem boots = ItemGenerator.createBuyableItem(bootsItem, 30);
        contents.set(3, 4, boots);

        // Range weapons
        ItemStack bowItem = new ItemStack(Material.BOW);
        bowItem.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
        ClickableItem bow = ItemGenerator.createBuyableItem(bowItem, 80);
        contents.set(1, 3, bow);

        ClickableItem snowballs = ItemGenerator.createBuyableItem(new ItemStack(Material.SNOW_BALL, 16), 20);
        contents.set(1, 2, snowballs);

        // Swords
        ClickableItem stoneSword = ItemGenerator.createBuyableItem(new ItemStack(Material.STONE_SWORD), 50);
        contents.set(1, 5, stoneSword);

        ClickableItem ironSword = ItemGenerator.createBuyableItem(new ItemStack(Material.IRON_SWORD), 100);
        contents.set(1, 6, ironSword);

        ClickableItem diamondSword = ItemGenerator.createBuyableItem(new ItemStack(Material.DIAMOND_SWORD), 200);
        contents.set(1, 7, diamondSword);
    }

    @Override
    public void update(Player p, InventoryContents contents) {

    }
}
