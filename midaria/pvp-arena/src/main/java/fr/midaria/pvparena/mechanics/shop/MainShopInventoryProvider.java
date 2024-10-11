package fr.midaria.pvparena.mechanics.shop;

import com.cryptomorin.xseries.XMaterial;
import fr.midaria.pvparena.Main;
import fr.midaria.pvparena.mechanics.evolutions.gui.EvolutionPickerInventoryProvider;
import fr.midaria.pvparena.utils.ItemGenerator;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class MainShopInventoryProvider implements InventoryProvider {
    public SmartInventory INVENTORY;

    public MainShopInventoryProvider() {
        INVENTORY = SmartInventory
                .builder()
                .size(3, 9)
                .provider(this)
                .manager(Main.getInstance().getInventoryManager())
                .title("§5§lShop")
                .build();
    }

    @Override
    public void init(Player p, InventoryContents contents) {
        contents.fill(ClickableItem.empty(XMaterial.PURPLE_STAINED_GLASS_PANE.parseItem()));

        List<String> shopLore = Arrays.asList("", "§7Achetez des items à usage unique");
        ItemStack shopItem = ItemGenerator.createItem(Material.DIAMOND_SWORD, "§dItems", shopLore);
        ClickableItem clickableShop = ClickableItem.of(shopItem, e -> new ShopInventoryProvider().INVENTORY.open(p));
        contents.set(1, 3, clickableShop);

        List<String> evolutionLore = Arrays.asList("", "§7Investisez de l'argent dans plusieurs compétences différentes", "§7afin de vous donner certains avantages");
        ItemStack evolutionItem = ItemGenerator.createItem(Material.BEACON, "§dEvolutions", evolutionLore);
        ClickableItem clickableEvolution = ClickableItem.of(evolutionItem, e -> new EvolutionPickerInventoryProvider().INVENTORY.open(p));
        contents.set(1, 5, clickableEvolution);
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
