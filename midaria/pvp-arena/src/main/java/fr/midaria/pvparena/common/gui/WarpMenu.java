package fr.midaria.pvparena.common.gui;

import com.cryptomorin.xseries.XMaterial;
import fr.midaria.pvparena.Main;
import fr.midaria.pvparena.utils.ItemGenerator;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class WarpMenu implements InventoryProvider {
    public SmartInventory INVENTORY;

    public WarpMenu() {
        INVENTORY = SmartInventory
                .builder()
                .size(3, 9)
                .provider(this)
                .manager(Main.getInstance().getInventoryManager())
                .title("§5§lMenu Warp")
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fill(ClickableItem.empty(XMaterial.PURPLE_STAINED_GLASS_PANE.parseItem()));

        // spawn, koth, grotte, train

        ClickableItem spawnItem = ClickableItem.of(ItemGenerator.createItem(Material.BEACON, "§dSpawn"), e -> {
            Player p = (Player) e.getWhoClicked();
            p.performCommand("spawn");
        });
        contents.set(1, 1, spawnItem);

        ClickableItem kothItem = ClickableItem.of(ItemGenerator.createItem(Material.DIAMOND_SWORD, "§dKOTH"), e -> {
            Player p = (Player) e.getWhoClicked();
            p.performCommand("essentials:warp koth");
        });
        contents.set(1, 3, kothItem);

        ClickableItem caveItem = ClickableItem.of(ItemGenerator.createItem(Material.IRON_FENCE, "§dGrotte"), e -> {
            Player p = (Player) e.getWhoClicked();
            p.performCommand("essentials:warp grotte");
        });
        contents.set(1, 5, caveItem);

        ClickableItem trainItem = ClickableItem.of(ItemGenerator.createItem(Material.MINECART, "§dTrain"), e -> {
            Player p = (Player) e.getWhoClicked();
            p.performCommand("essentials:warp train");
        });
        contents.set(1, 7, trainItem);
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
