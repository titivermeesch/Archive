package io.minki.lobby.commands;

import io.minki.lobby.LobbyManager;
import io.minki.lobby.game.GamesInventory;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;

public class GameCommand extends Command {
    public GameCommand(LobbyManager lobbyManager) {
        super("games", "game", "g");

        setDefaultExecutor((sender, context) -> {
            if(!(sender instanceof Player)) {
                return;
            }

            new GamesInventory(lobbyManager).open((Player) sender);
        });
    }
}
