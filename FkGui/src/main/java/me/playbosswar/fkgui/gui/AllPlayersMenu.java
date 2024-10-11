package me.playbosswar.fkgui.gui;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.playbosswar.fkgui.Main;
import me.playbosswar.fkgui.config.Team;
import me.playbosswar.fkgui.utils.Callback;
import me.playbosswar.fkgui.utils.Items;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class AllPlayersMenu implements InventoryProvider {
    public SmartInventory INVENTORY;
    private Callback<Player> callback;
    private Team team;

    public AllPlayersMenu(Team team, Callback<Player> callback) {
        this.callback = callback;
        this.team = team;
        INVENTORY = SmartInventory.builder()
                .id("all-players-menu")
                .provider(this)
                .manager(Main.getInstance().getInventoryManager())
                .size(5, 9)
                .title(Main.getInstance().getText("addTeamPlayer") + team.getName())
                .build();
    }

    @Override
    public void init(Player player, InventoryContents inventoryContents) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            ClickableItem item = ClickableItem.of(Items.getPlayerHead(p), e -> callback.execute(p));
            inventoryContents.add(item);
        }

        ClickableItem back = Items.createClickableItem(XMaterial.BARRIER, Main.getInstance().getText("back"), e -> new ManageTeamMenu(team).INVENTORY.open(player));
        inventoryContents.set(4, 8, back);
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }
}
