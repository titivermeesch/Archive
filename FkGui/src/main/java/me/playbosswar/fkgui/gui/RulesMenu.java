package me.playbosswar.fkgui.gui;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.playbosswar.fkgui.config.ConfigManager;
import me.playbosswar.fkgui.Main;
import me.playbosswar.fkgui.utils.Items;
import org.bukkit.entity.Player;

public class RulesMenu implements InventoryProvider {
    public SmartInventory INVENTORY;

    public RulesMenu() {
        INVENTORY = SmartInventory.builder()
                .id("rules-menu")
                .provider(this)
                .manager(Main.getInstance().getInventoryManager())
                .size(5, 9)
                .title("Rules")
                .build();
    }

    @Override
    public void init(Player p, InventoryContents inventoryContents) {
        ConfigManager configManager = Main.getInstance().getConfigManager();
        // Days section
        ClickableItem pvpCapItem = Items.createClickableItem(XMaterial.DIAMOND_SWORD, Main.getInstance().getText("pvpDay"), e -> new DaySelectMenu(v -> {
            configManager.setPvpCapDay(v);
            new RulesMenu().INVENTORY.open(p);
        }).INVENTORY.open(p));
        inventoryContents.set(0, 0, pvpCapItem);

        ClickableItem netherCapItem = Items.createClickableItem(XMaterial.NETHERRACK, Main.getInstance().getText("netherDay"), e -> new DaySelectMenu(v -> {
            configManager.setNetherCapDay(v);
            new RulesMenu().INVENTORY.open(p);
        }).INVENTORY.open(p));
        inventoryContents.set(0, 2, netherCapItem);

        ClickableItem tntCapItem = Items.createClickableItem(XMaterial.TNT, Main.getInstance().getText("assaultDay"), e -> new DaySelectMenu(v -> {
            configManager.setTntCapDay(v);
            new RulesMenu().INVENTORY.open(p);
        }).INVENTORY.open(p));
        inventoryContents.set(0, 4, tntCapItem);

        ClickableItem endCapItem = Items.createClickableItem(XMaterial.END_STONE, Main.getInstance().getText("enderDay"), e -> new DaySelectMenu(v -> {
            configManager.setEndCapDay(v);
            new RulesMenu().INVENTORY.open(p);
        }).INVENTORY.open(p));
        inventoryContents.set(0, 6, endCapItem);

        ClickableItem disablePotionItem = Items.createClickableItem(XMaterial.POTION, Main.getInstance().getText("disablePotions"), e -> p.performCommand("fk rules disabledpotions"));
        inventoryContents.set(0, 8, disablePotionItem);

        // Rules section
        ClickableItem friendlyFireItem = Items.createClickableItem(XMaterial.DIAMOND_SWORD, Main.getInstance().getText("friendlyFire"));
        inventoryContents.set(2, 0, friendlyFireItem);
        ClickableItem friendlyFireToggle = Items.createToggleItem(Main.getInstance().getText("friendlyFire"), configManager.isFriendlyFire(), v -> {
            configManager.setFriendlyFire(v);
            new RulesMenu().INVENTORY.open(p);
        });
        inventoryContents.set(3, 0, friendlyFireToggle);

        ClickableItem enderpearlAssaultItem = Items.createClickableItem(XMaterial.ENDER_PEARL, Main.getInstance().getText("enderAssault"));
        inventoryContents.set(2, 2, enderpearlAssaultItem);
        ClickableItem enderpearlAssaultToggle = Items.createToggleItem(Main.getInstance().getText("enderAssault"), configManager.isEnderpearlAssault(), v -> {
            configManager.setEnderpearlAssault(v);
            new RulesMenu().INVENTORY.open(p);
        });
        inventoryContents.set(3, 2, enderpearlAssaultToggle);

        ClickableItem healthNameItem = Items.createClickableItem(XMaterial.POTION, Main.getInstance().getText("healthBelowName"));
        inventoryContents.set(2, 4, healthNameItem);
        ClickableItem healthNameToggle = Items.createToggleItem(Main.getInstance().getText("healthBelowName"), configManager.isHealthBelowName(), v -> {
            configManager.setHealthBelowName(v);
            new RulesMenu().INVENTORY.open(p);
        });
        inventoryContents.set(3, 4, healthNameToggle);

        ClickableItem respawnHomeItem = Items.createClickableItem(XMaterial.RED_BED, Main.getInstance().getText("respawnAtHome"));
        inventoryContents.set(2, 6, respawnHomeItem);
        ClickableItem respawnHomeToggle = Items.createToggleItem(Main.getInstance().getText("respawnAtHome"), configManager.isRespawnAtHome(), v -> {
            configManager.setRespawnAtHome(v);
            new RulesMenu().INVENTORY.open(p);
        });
        inventoryContents.set(3, 6, respawnHomeToggle);

        ClickableItem tntJumpItem = Items.createClickableItem(XMaterial.TNT, Main.getInstance().getText("tntJump"));
        inventoryContents.set(2, 8, tntJumpItem);
        ClickableItem tntJumpToggle = Items.createToggleItem(Main.getInstance().getText("tntJump"), configManager.isTntJump(), v -> {
            configManager.setTntJump(v);
            new RulesMenu().INVENTORY.open(p);
        });
        inventoryContents.set(3, 8, tntJumpToggle);

        ClickableItem back = Items.createClickableItem(XMaterial.BARRIER, Main.getInstance().getText("back"), e -> new MainMenu().INVENTORY.open(p));
        inventoryContents.set(4, 4, back);
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }
}
