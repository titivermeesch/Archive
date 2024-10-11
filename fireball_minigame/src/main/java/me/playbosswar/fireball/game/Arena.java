package me.playbosswar.fireball.game;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class Arena {
    private String name;
    private int maxPlayers;
    private int minPlayers;
    private int teams;
    private Location upperBound;
    private Location lowerBound;
    private final List<Location> spawnPoints = new ArrayList<>();
    private boolean enabled;

    public Arena(String name, int maxPlayers, int minPlayers, int teams, Location upperBound, Location lowerBound) {
        this.name = name;
        this.maxPlayers = maxPlayers;
        this.minPlayers = minPlayers;
        this.teams = teams;
        this.upperBound = upperBound;
        this.lowerBound = lowerBound;
    }

    public Arena(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

    public int getTeams() {
        return teams;
    }

    public void setTeams(int teams) {
        this.teams = teams;
    }

    public Location getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(Location upperBound) {
        this.upperBound = upperBound;
    }

    public Location getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(Location lowerBound) {
        this.lowerBound = lowerBound;
    }

    public List<Location> getSpawnPoints() {
        return spawnPoints;
    }

    public void addSpawnPoint(Location location) {
        spawnPoints.add(location);
    }

    public void removeSpawnPoint(int index) {
        spawnPoints.remove(index);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setActive(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isComplete() {
        return name != null && maxPlayers != 0 && minPlayers != 0 && teams != 0 && upperBound != null && lowerBound != null;
    }
}
