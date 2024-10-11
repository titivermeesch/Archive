package me.playbosswar.fireball.game;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Team {
    private final String name;
    private final List<UUID> players = new ArrayList<>();

    public Team(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<UUID> getPlayers() {
        return players;
    }
}
