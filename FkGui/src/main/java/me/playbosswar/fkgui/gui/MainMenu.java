package me.playbosswar.fkgui.gui;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.playbosswar.fkgui.Main;
import me.playbosswar.fkgui.utils.Items;
import org.bukkit.entity.Player;

public class MainMenu implements InventoryProvider {
    public SmartInventory INVENTORY;

    public MainMenu() {
        INVENTORY = SmartInventory.builder()
                .id("main-menu")
                .provider(this)
                .manager(Main.getInstance().getInventoryManager())
                .size(1, 9)
                .title("FallenKingdom")
                .build();
    }

    @Override
    public void init(Player p, InventoryContents inventoryContents) {
        ClickableItem gameItem = Items.createClickableItem(XMaterial.IRON_SWORD, Main.getInstance().getText("game"), e -> new GameMenu().INVENTORY.open(p));
        inventoryContents.set(0, 2, gameItem);

        ClickableItem rulesItem = Items.createClickableItem(XMaterial.WRITABLE_BOOK, Main.getInstance().getText("rules"), e -> new RulesMenu().INVENTORY.open(p));
        inventoryContents.set(0, 4, rulesItem);

        ClickableItem teamItem = Items.createClickableItem(XMaterial.GREEN_BANNER, Main.getInstance().getText("teams"), e -> new TeamsMenu().INVENTORY.open(p));
        inventoryContents.set(0, 6, teamItem);
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }
}
