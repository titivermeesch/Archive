package fr.midaria.pvparena.common.database.models;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import fr.midaria.pvparena.mechanics.evolutions.EvolutionType;

import java.util.UUID;

@DatabaseTable(tableName = "evolution_progress")
public class EvolutionProgress {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private UUID playerUuid;
    @DatabaseField(dataType = DataType.ENUM_TO_STRING)
    private EvolutionType evolutionType;
    @DatabaseField
    private int level;

    public EvolutionProgress() {
    }

    public EvolutionProgress(UUID playerUuid, EvolutionType evolutionType, int level) {
        this.playerUuid = playerUuid;
        this.evolutionType = evolutionType;
        this.level = level;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public EvolutionType getEvolutionType() {
        return evolutionType;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
