package fr.midaria.pvparena.mechanics.evolutions.gui;

import com.cryptomorin.xseries.XMaterial;
import fr.midaria.pvparena.Main;
import fr.midaria.pvparena.mechanics.evolutions.Evolution;
import fr.midaria.pvparena.mechanics.evolutions.EvolutionLevel;
import fr.midaria.pvparena.mechanics.evolutions.EvolutionType;
import fr.midaria.pvparena.utils.ItemGenerator;
import fr.minuskube.inv.ClickableItem;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GuiUtils {
    public static List<ClickableItem> createProgressRow(Player p, EvolutionType evolutionType) {
        Evolution evolution = Main.getInstance().getEvolutionManager().getEvolution(evolutionType);
        int maxLevel = evolution.getMaxLevel();
        int playerLevel = Main.getInstance().getEvolutionManager().getPlayerEvolutionLevel(p.getUniqueId(), evolutionType);

        List<ClickableItem> items = new ArrayList<>();
        for (int i = 1; i <= maxLevel; i++) {
            EvolutionLevel evolutionLevel = evolution.getLevels().get(i - 1);
            List<String> description = evolutionLevel.getDescription();
            if (playerLevel >= i) {
                items.add(ClickableItem.empty(ItemGenerator.createItem(XMaterial.LIME_STAINED_GLASS_PANE, "§dNiveau " + i, evolutionLevel.getDescription())));
                continue;
            }

            if (i == playerLevel + 1) {
                description.add("");
                description.add("§7Acheter pour §d" + evolutionLevel.getPrice() + "$");
                int finalI = i;
                ClickableItem item = ClickableItem.of(ItemGenerator.createItem(XMaterial.ORANGE_STAINED_GLASS_PANE, "§dNiveau " + i, evolutionLevel.getDescription()), e -> {
                    Main.getInstance().getEvolutionManager().upgradeEvolution(p, evolutionType, finalI);
                });
                items.add(item);
                continue;
            }

            description.add("");
            description.add("§cVous devez débloquer les précédents niveaux");
            description.add("§cpour acheter cette évolution");
            items.add(ClickableItem.of(ItemGenerator.createItem(XMaterial.RED_STAINED_GLASS_PANE, "§dNiveau " + i, evolutionLevel.getDescription()), e -> {
                p.sendMessage("§cVous devez débloquer les précédents niveaux pour acheter cette évolution");
                p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 0);
            }));
        }

        return items;
    }

    public static ClickableItem createEvolutionMenuItem(Player p, EvolutionType evolutionType) {
        Evolution evolution = Main.getInstance().getEvolutionManager().getEvolution(evolutionType);
        ItemStack item = ItemGenerator.createItem(evolution.getIcon(), evolution.getName(), evolution.getDescription());

        return ClickableItem.of(item, e -> new EvolutionInventoryProvider(evolution.getType()).INVENTORY.open(p));
    }
}
