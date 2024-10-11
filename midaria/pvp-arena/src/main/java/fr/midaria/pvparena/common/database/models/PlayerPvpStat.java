package fr.midaria.pvparena.common.database.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;
import java.util.UUID;

@DatabaseTable(tableName = "player_pvp_stats")
public class PlayerPvpStat {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private UUID killerUuid;
    @DatabaseField
    private UUID victimUuid;
    @DatabaseField
    private Date timestamp;
    @DatabaseField
    private String game;

    public PlayerPvpStat() {}

    public PlayerPvpStat(UUID killerUuid, UUID victimUuid, Date timestamp, String game) {
        this.killerUuid = killerUuid;
        this.victimUuid = victimUuid;
        this.timestamp = timestamp;
        this.game = game;
    }

    public int getId() {
        return id;
    }

    public UUID getKillerUuid() {
        return killerUuid;
    }

    public void setKillerUuid(UUID killerUuid) {
        this.killerUuid = killerUuid;
    }

    public UUID getVictimUuid() {
        return victimUuid;
    }

    public void setVictimUuid(UUID victimUuid) {
        this.victimUuid = victimUuid;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }
}
