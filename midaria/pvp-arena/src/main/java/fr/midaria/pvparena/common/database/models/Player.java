package fr.midaria.pvparena.common.database.models;


import java.util.Date;
import java.util.UUID;

public class Player {
    private int id;
    private UUID uuid;
    private String lastDisplayName;
    private Date firstJoinDate;
    private Date lastJoinDate;
    private int playedMinutes;

    public Player() {
    }

    public int getId() {
        return id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getLastDisplayName() {
        return lastDisplayName;
    }

    public void setLastDisplayName(String lastDisplayName) {
        this.lastDisplayName = lastDisplayName;
    }

    public Date getFirstJoinDate() {
        return firstJoinDate;
    }

    public void setFirstJoinDate(Date firstJoinDate) {
        this.firstJoinDate = firstJoinDate;
    }

    public Date getLastJoinDate() {
        return lastJoinDate;
    }

    public void setLastJoinDate(Date lastJoinDate) {
        this.lastJoinDate = lastJoinDate;
    }

    public int getPlayedMinutes() {
        return playedMinutes;
    }

    public void setPlayedMinutes(int playedMinutes) {
        this.playedMinutes = playedMinutes;
    }
}
