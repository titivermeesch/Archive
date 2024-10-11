package io.minki.lobby.game;

import io.minki.gapi.text.Colors;
import io.minki.gapi.text.LoreBuilder;
import io.minki.gapi.text.TextBuilder;
import io.minki.lobby.LobbyManager;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public class GamesInventory {
    private final LobbyManager lobbyManager;
    private final Inventory inventory;

    public GamesInventory(LobbyManager lobbyManager) {
        this.lobbyManager = lobbyManager;
        this.inventory = new Inventory(InventoryType.CHEST_3_ROW, "Games");

        build();
        addListeners();
    }

    private void build() {
        ItemStack skywars = ItemStack.builder(Material.FEATHER)
                .displayName(new TextBuilder("Skywars").color(Colors.PRIMARY).build())
                .lore(new LoreBuilder()
                        .addEmpty()
                        .add("Fast-paced PvP on floating islands", Colors.TERTIARY)
                        .add("Gather resources, eliminate foes, survive", Colors.TERTIARY)
                        .add("Last player standing wins the game", Colors.TERTIARY)
                        .addEmpty()
                        .add("Current players: 0", Colors.TERTIARY)
                        .addEmpty()
                        .add("Left-Click to join Skywars", Colors.SUCCESS)
                        .add("Right-Click for more game information", Colors.SECONDARY)
                        .build()
                )
                .build();
        inventory.addItemStack(skywars);

        ItemStack fireball = ItemStack.builder(Material.FIRE_CHARGE)
                .displayName(new TextBuilder("Fireball").color(Colors.PRIMARY).build())
                .lore(new LoreBuilder()
                        .addEmpty()
                        .add("Dodge, aim, survive", Colors.TERTIARY)
                        .add("Avoid blasts, launch your attacks", Colors.TERTIARY)
                        .add("Last one not exploding wins", Colors.TERTIARY)
                        .addEmpty()
                        .add("Current players: 0", Colors.TERTIARY)
                        .addEmpty()
                        .add("Left-Click to join Fireball", Colors.SUCCESS)
                        .add("Right-Click for more game information", Colors.SECONDARY)
                        .build()
                )
                .build();
        inventory.addItemStack(fireball);

        ItemStack stopQueue = ItemStack.builder(Material.REDSTONE_BLOCK)
                .displayName(new TextBuilder("Stop Queue").color(Colors.ERROR).build())
                .lore(new LoreBuilder()
                        .addEmpty()
                        .add("Stop searching for a game", Colors.TERTIARY)
                        .addEmpty()
                        .add("Left-Click to stop queue", Colors.ERROR)
                        .build()
                )
                .build();
        inventory.setItemStack(22, stopQueue);
    }

    private void addListeners() {
        inventory.addInventoryCondition((player, slot, clickType, conditionResult) -> {
            conditionResult.setCancel(true);

            if (conditionResult.getClickedItem() == null) {
                return;
            }

            Material material = conditionResult.getClickedItem().material();
            if (material.equals(Material.FEATHER)) {
                if(lobbyManager.getQueueManager().isPlayerInQueue(player.getUuid())) {
                    player.sendMessage("You are already in a queue!");
                    return;
                }

                lobbyManager.getQueueManager().queuePlayer(player.getUuid(), "skywars", "*");
                lobbyManager.showQueueBossBar(player);

                player.closeInventory();
                player.playSound(Sound.sound(Key.key("block.note_block.bell"), Sound.Source.MUSIC, 1, 1));
            } else if (material.equals(Material.FIRE_CHARGE)) {
                if(lobbyManager.getQueueManager().isPlayerInQueue(player.getUuid())) {
                    player.sendMessage("You are already in a queue!");
                    return;
                }
                lobbyManager.getQueueManager().queuePlayer(player.getUuid(), "fireball", "*");
                lobbyManager.showQueueBossBar(player);

                player.closeInventory();
                player.playSound(Sound.sound(Key.key("block.note_block.bell"), Sound.Source.MUSIC, 1, 1));
            } else if (material.equals(Material.REDSTONE_BLOCK)) {
                lobbyManager.getQueueManager().dequeuePlayer(player.getUuid());
                lobbyManager.hideQueueBossBar(player);
                player.sendMessage("Removed from queue");
                player.closeInventory();
            }
        });
    }

    public void open(Player p) {
        p.openInventory(inventory);
    }
}
