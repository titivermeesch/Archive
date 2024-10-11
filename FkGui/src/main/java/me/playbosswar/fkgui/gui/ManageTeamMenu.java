package me.playbosswar.fkgui.gui;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.playbosswar.fkgui.Main;
import me.playbosswar.fkgui.config.Team;
import me.playbosswar.fkgui.utils.Items;
import org.bukkit.entity.Player;

public class ManageTeamMenu implements InventoryProvider {
    public SmartInventory INVENTORY;
    private final Team team;

    public ManageTeamMenu(Team team) {
        this.team = team;
        INVENTORY = SmartInventory.builder()
                .id("manage-team-menu")
                .provider(this)
                .manager(Main.getInstance().getInventoryManager())
                .size(5, 9)
                .title(Main.getInstance().getText("team") + " " + team.getName())
                .build();
    }

    @Override
    public void init(Player player, InventoryContents inventoryContents) {
        for (Player p : team.getPlayers()) {
            ClickableItem item = ClickableItem.of(Items.getPlayerHead(p), e -> {
                team.removePlayer(p);
                new ManageTeamMenu(team).INVENTORY.open(player);
            });
            inventoryContents.add(item);
        }

        ClickableItem add = Items.createClickableItem(XMaterial.PAPER, Main.getInstance().getText("addPlayer"), e -> new AllPlayersMenu(team, newPlayer -> {
            Main.getInstance().getConfigManager().removePlayer(newPlayer);
            team.addPlayer(newPlayer);
            new ManageTeamMenu(team).INVENTORY.open(player);
        }).INVENTORY.open(player));

        inventoryContents.set(4, 0, add);

        ClickableItem back = Items.createClickableItem(XMaterial.BARRIER, Main.getInstance().getText("back"), e -> new TeamsMenu().INVENTORY.open(player));
        inventoryContents.set(4, 8, back);
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }
}
