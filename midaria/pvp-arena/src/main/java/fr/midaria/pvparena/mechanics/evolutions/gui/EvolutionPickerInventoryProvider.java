package fr.midaria.pvparena.mechanics.evolutions.gui;

import com.cryptomorin.xseries.XMaterial;
import fr.midaria.pvparena.Main;
import fr.midaria.pvparena.mechanics.evolutions.EvolutionType;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.entity.Player;

public class EvolutionPickerInventoryProvider implements InventoryProvider {
    public SmartInventory INVENTORY;

    public EvolutionPickerInventoryProvider() {
        INVENTORY = SmartInventory
                .builder()
                .size(3, 9)
                .id("main-evolution")
                .provider(this)
                .manager(Main.getInstance().getInventoryManager())
                .title("§5§lEvolutions")
                .build();
    }

    @Override
    public void init(Player p, InventoryContents contents) {
        contents.fill(ClickableItem.empty(XMaterial.PURPLE_STAINED_GLASS_PANE.parseItem()));

        ClickableItem swordItem = GuiUtils.createEvolutionMenuItem(p, EvolutionType.SWORD_UPGRADE);
        contents.set(1, 3, swordItem);

        ClickableItem gappleItem = GuiUtils.createEvolutionMenuItem(p, EvolutionType.BONUS_GOLDEN_APPLE);
        contents.set(1, 4, gappleItem);

        ClickableItem bowItem = GuiUtils.createEvolutionMenuItem(p, EvolutionType.BOW_UPGRADE);
        contents.set(1, 5, bowItem);

    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
