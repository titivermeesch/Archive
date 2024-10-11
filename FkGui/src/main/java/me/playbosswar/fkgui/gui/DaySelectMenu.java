package me.playbosswar.fkgui.gui;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.playbosswar.fkgui.Main;
import me.playbosswar.fkgui.utils.Callback;
import me.playbosswar.fkgui.utils.Items;
import org.bukkit.entity.Player;

public class DaySelectMenu implements InventoryProvider {
    public SmartInventory INVENTORY;
    private Callback<Integer> callback;

    public DaySelectMenu(Callback<Integer> callback) {
        this.callback = callback;
        INVENTORY = SmartInventory.builder()
                .id("days-menu")
                .provider(this)
                .manager(Main.getInstance().getInventoryManager())
                .size(3, 9)
                .title(Main.getInstance().getText("daySelection"))
                .build();
    }

    @Override
    public void init(Player player, InventoryContents inventoryContents) {
        ClickableItem one = Items.createClickableItem(XMaterial.GREEN_WOOL, Main.getInstance().getText("day") + "1", e -> callback.execute(1));
        inventoryContents.set(0, 2, one);

        ClickableItem two = Items.createClickableItem(XMaterial.GREEN_WOOL, Main.getInstance().getText("day") + "2", e -> callback.execute(2));
        inventoryContents.set(0, 3, two);

        ClickableItem three = Items.createClickableItem(XMaterial.GREEN_WOOL, Main.getInstance().getText("day") + "3", e -> callback.execute(3));
        inventoryContents.set(0, 4, three);

        ClickableItem four = Items.createClickableItem(XMaterial.GREEN_WOOL, Main.getInstance().getText("day") + "4", e -> callback.execute(4));
        inventoryContents.set(0, 5, four);

        ClickableItem five = Items.createClickableItem(XMaterial.GREEN_WOOL, Main.getInstance().getText("day") + "5", e -> callback.execute(5));
        inventoryContents.set(0, 6, five);

        ClickableItem six = Items.createClickableItem(XMaterial.YELLOW_WOOL, Main.getInstance().getText("day") + "6", e -> callback.execute(6));
        inventoryContents.set(1, 2, six);

        ClickableItem seven = Items.createClickableItem(XMaterial.YELLOW_WOOL, Main.getInstance().getText("day") + "7", e -> callback.execute(7));
        inventoryContents.set(1, 3, seven);

        ClickableItem eight = Items.createClickableItem(XMaterial.YELLOW_WOOL, Main.getInstance().getText("day") + "8", e -> callback.execute(8));
        inventoryContents.set(1, 4, eight);

        ClickableItem nine = Items.createClickableItem(XMaterial.YELLOW_WOOL, Main.getInstance().getText("day") + "9", e -> callback.execute(9));
        inventoryContents.set(1, 5, nine);

        ClickableItem ten = Items.createClickableItem(XMaterial.YELLOW_WOOL, Main.getInstance().getText("day") + "10", e -> callback.execute(10));
        inventoryContents.set(1, 6, ten);

        ClickableItem eleven = Items.createClickableItem(XMaterial.RED_WOOL, Main.getInstance().getText("day") + "11", e -> callback.execute(11));
        inventoryContents.set(2, 2, eleven);

        ClickableItem twelve = Items.createClickableItem(XMaterial.RED_WOOL, Main.getInstance().getText("day") + "12", e -> callback.execute(12));
        inventoryContents.set(2, 3, twelve);

        ClickableItem thirteen = Items.createClickableItem(XMaterial.RED_WOOL, Main.getInstance().getText("day") + "13", e -> callback.execute(13));
        inventoryContents.set(2, 4, thirteen);

        ClickableItem fourteen = Items.createClickableItem(XMaterial.RED_WOOL, Main.getInstance().getText("day") + "14", e -> callback.execute(14));
        inventoryContents.set(2, 5, fourteen);

        ClickableItem sixteen = Items.createClickableItem(XMaterial.RED_WOOL, Main.getInstance().getText("day") + "15", e -> callback.execute(15));
        inventoryContents.set(2, 6, sixteen);
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }
}
