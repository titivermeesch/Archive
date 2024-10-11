package me.playbosswar.fkgui.gui;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.playbosswar.fkgui.Main;
import me.playbosswar.fkgui.config.ConfigManager;
import me.playbosswar.fkgui.utils.Items;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class TeamsMenu implements InventoryProvider {
    public SmartInventory INVENTORY;

    public TeamsMenu() {
        INVENTORY = SmartInventory.builder()
                .id("teams-menu")
                .provider(this)
                .manager(Main.getInstance().getInventoryManager())
                .size(1, 9)
                .title("Teams")
                .build();
    }

    @Override
    public void init(Player p, InventoryContents inventoryContents) {
        ConfigManager configManager = Main.getInstance().getConfigManager();

        ClickableItem teamBlue = Items.createClickableItem(XMaterial.BLUE_BANNER, Main.getInstance().getText("teamBlue"), e -> new ManageTeamMenu(configManager.getTeamBlue()).INVENTORY.open(p));
        inventoryContents.set(0, 0, teamBlue);

        ClickableItem teamRed = Items.createClickableItem(XMaterial.RED_BANNER, Main.getInstance().getText("teamRed"), e -> new ManageTeamMenu(configManager.getTeamRed()).INVENTORY.open(p));
        inventoryContents.set(0, 2, teamRed);

        ClickableItem teamYellow = Items.createClickableItem(XMaterial.YELLOW_BANNER, Main.getInstance().getText("teamYellow"), e -> new ManageTeamMenu(configManager.getTeamYellow()).INVENTORY.open(p));
        inventoryContents.set(0, 4, teamYellow);

        ClickableItem teamGreen = Items.createClickableItem(XMaterial.GREEN_BANNER, Main.getInstance().getText("teamGreen"), e -> new ManageTeamMenu(configManager.getTeamGreen()).INVENTORY.open(p));
        inventoryContents.set(0, 6, teamGreen);

        ClickableItem back = Items.createClickableItem(XMaterial.BARRIER, Main.getInstance().getText("back"), e -> new MainMenu().INVENTORY.open(p));
        inventoryContents.set(0, 8, back);
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }
}
