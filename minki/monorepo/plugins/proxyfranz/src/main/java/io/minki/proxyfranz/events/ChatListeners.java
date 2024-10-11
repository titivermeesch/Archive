package io.minki.proxyfranz.events;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import io.minki.proxyfranz.ProxyFranz;
import io.minki.proxyfranz.players.PlayersLevelManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;

public class ChatListeners {
    private final ProxyServer proxy;
    private final PlayersLevelManager playersLevelManager;

    public ChatListeners(ProxyFranz plugin, ProxyServer proxy) {
        this.proxy = proxy;
        this.playersLevelManager = new PlayersLevelManager(plugin);
    }

    @Subscribe(order = PostOrder.FIRST)
    public void onChatMessage(PlayerChatEvent e) {
        e.setResult(PlayerChatEvent.ChatResult.denied());

        int level = playersLevelManager.getPlayerLevel(e.getPlayer().getUniqueId());

        // TODO: Properly fetch player level
        // TODO: Fetch player rank and display color accordingly

        TextComponent message = Component
                .text("[" + level + "]")
                .color(TextColor.color(0x9dc1c9))
                .append(Component.text(" " + e.getPlayer().getUsername() + ": ").color(TextColor.color(0xa1a1a1)))
                .append(Component.text(e.getMessage()).color(TextColor.color(0xffffff)));

        proxy.sendMessage(message);
    }
}
