package io.minki.proxyfranz.commands;

import com.velocitypowered.api.command.RawCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import io.minki.proxyfranz.server.ServerManager;

public class LobbyCommand implements RawCommand {
    private final ServerManager serverManager;

    public LobbyCommand(ServerManager serverManager) {
        this.serverManager = serverManager;
    }

    @Override
    public void execute(Invocation invocation) {
        if(!(invocation.source() instanceof Player p)) {
            return;
        }

        RegisteredServer lobby = serverManager.getAnyLobby();
        p.createConnectionRequest(lobby).fireAndForget();
    }
}
