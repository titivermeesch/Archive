package fr.midaria.pvparena.mechanics.evolutions.gui;

import com.cryptomorin.xseries.XMaterial;
import fr.midaria.pvparena.Main;
import fr.midaria.pvparena.mechanics.evolutions.Evolution;
import fr.midaria.pvparena.mechanics.evolutions.EvolutionType;
import fr.midaria.pvparena.utils.ItemGenerator;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.entity.Player;

import java.util.List;

public class EvolutionInventoryProvider implements InventoryProvider {
    private final Evolution evolution;
    public SmartInventory INVENTORY;

    public EvolutionInventoryProvider(EvolutionType evolutionType) {
        this.evolution = Main.getInstance().getEvolutionManager().getEvolution(evolutionType);
        INVENTORY = SmartInventory
                .builder()
                .size(3, 9)
                .id(evolution.getName())
                .provider(this)
                .manager(Main.getInstance().getInventoryManager())
                .title(evolution.getName())
                .build();
    }

    @Override
    public void init(Player p, InventoryContents contents) {
        contents.fill(ClickableItem.empty(XMaterial.PURPLE_STAINED_GLASS_PANE.parseItem()));
        List<ClickableItem> upgradeItems = GuiUtils.createProgressRow(p, evolution.getType());
        for (int i = 0; i < upgradeItems.size(); i++) {
            contents.set(1, i + 2, upgradeItems.get(i));
        }

        contents.set(2, 8, ClickableItem.of(ItemGenerator.getBackItem(), e -> new EvolutionPickerInventoryProvider().INVENTORY.open(p)));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
