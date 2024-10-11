package me.playbosswar.fkgui.config;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Team {
    final String name;
    List<Player> players;

    public Team(String name) {
        this.name = name;
        this.players = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player player) {
        this.players.add(player);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "fk team addPlayer " + player.getName() + " " + getName());
    }

    public void removePlayer(Player player) {
        this.players.remove(player);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "fk team removePlayer " + player.getName() + " " + getName());
    }
}
