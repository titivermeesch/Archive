package me.playbosswar.fireball.game;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Game {
    private Arena arena;
    private GameStatus status = GameStatus.WAITING;
    private List<UUID> players = new ArrayList<>();
    private List<Team> teams = new ArrayList<>();

    public Game(Arena arena) {
        this.arena = arena;
    }

    public void addPlayer(UUID player) {
        players.add(player);
    }

    public void removePlayer(UUID player) {
        players.remove(player);
    }
}
