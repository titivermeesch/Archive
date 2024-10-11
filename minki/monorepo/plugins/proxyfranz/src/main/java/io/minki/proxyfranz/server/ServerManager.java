package io.minki.proxyfranz.server;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;

public class ServerManager {
    private final ProxyServer proxy;

    public ServerManager(ProxyServer proxy) {
        this.proxy = proxy;
    }

    public RegisteredServer getAnyLobby() {
        return proxy.matchServer("lobby-").stream().findFirst().orElse(null);
    }
}
