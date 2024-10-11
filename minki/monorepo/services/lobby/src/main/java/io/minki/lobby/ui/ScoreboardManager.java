package io.minki.lobby.ui;

import io.minki.gapi.text.Colors;
import io.minki.gapi.text.TextBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.minestom.server.entity.Player;
import net.minestom.server.scoreboard.Sidebar;

public class ScoreboardManager {
    private final Sidebar lobbyScoreboard;

    public ScoreboardManager() {
        this.lobbyScoreboard = new Sidebar(new TextBuilder("Minki").color(Colors.PRIMARY).bold().build());
        buildScoreboard();
    }

    public void addPlayer(Player player) {
        this.lobbyScoreboard.addViewer(player);
    }

    private void buildScoreboard() {
        this.lobbyScoreboard.createLine(createLine(9, TextBuilder.empty()));
        this.lobbyScoreboard.createLine(createLine(8, new TextBuilder("► Rank:").color(Colors.SECONDARY).build()));
        this.lobbyScoreboard.createLine(createLine(7, new TextBuilder("Default").color(Colors.TERTIARY).build()));
        this.lobbyScoreboard.createLine(createLine(6, TextBuilder.empty()));
        this.lobbyScoreboard.createLine(createLine(5, new TextBuilder("► Coins:").color(Colors.SECONDARY).build()));
        this.lobbyScoreboard.createLine(createLine(4, new TextBuilder("0").color(Colors.TERTIARY).build()));
        this.lobbyScoreboard.createLine(createLine(3, TextBuilder.empty()));
        this.lobbyScoreboard.createLine(createLine(2, new TextBuilder("► Level:").color(Colors.SECONDARY).build()));
        this.lobbyScoreboard.createLine(createLine(1, new TextBuilder("0").color(Colors.TERTIARY).build()));
    }

    private Sidebar.ScoreboardLine createLine(int line, TextComponent text) {
        return new Sidebar.ScoreboardLine(
                "line_" + line,
                text,
                line
        );
    }
}
