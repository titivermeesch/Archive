package me.playbosswar.fkgui.gui;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.playbosswar.fkgui.Main;
import me.playbosswar.fkgui.utils.Items;
import org.bukkit.entity.Player;

public class GameMenu implements InventoryProvider {
    public SmartInventory INVENTORY;

    public GameMenu() {
        INVENTORY = SmartInventory.builder()
                .id("game-menu")
                .provider(this)
                .manager(Main.getInstance().getInventoryManager())
                .size(1, 9)
                .title(Main.getInstance().getText("game"))
                .build();
    }

    @Override
    public void init(Player player, InventoryContents inventoryContents) {
        ClickableItem startGame = Items.createClickableItem(XMaterial.LIME_STAINED_GLASS_PANE, Main.getInstance().getText("startGame"), e -> player.performCommand("fk game start"));
        inventoryContents.set(0, 0, startGame);

        ClickableItem stopGame = Items.createClickableItem(XMaterial.RED_STAINED_GLASS_PANE, Main.getInstance().getText("stopGame"), e -> {
            player.performCommand("fk game stop");
            player.performCommand("restart");
        });
        inventoryContents.set(0, 1, stopGame);

        ClickableItem pauseGame = Items.createClickableItem(XMaterial.ORANGE_STAINED_GLASS_PANE, Main.getInstance().getText("pauseGame"), e -> player.performCommand("fk game pause"));
        inventoryContents.set(0, 2, pauseGame);

        ClickableItem resumeGame = Items.createClickableItem(XMaterial.LIGHT_BLUE_STAINED_GLASS_PANE, Main.getInstance().getText("resumeGame"), e -> player.performCommand("fk game resume"));
        inventoryContents.set(0, 3, resumeGame);

        ClickableItem back = Items.createClickableItem(XMaterial.BARRIER, Main.getInstance().getText("back"), e -> new MainMenu().INVENTORY.open(player));
        inventoryContents.set(0, 8, back);
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }
}
