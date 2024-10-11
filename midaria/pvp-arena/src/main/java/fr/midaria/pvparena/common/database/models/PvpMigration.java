package fr.midaria.pvparena.common.database.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.UUID;

@DatabaseTable(tableName = "pvp_migration")
public class PvpMigration {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private UUID playerUuid;

    public PvpMigration() {
    }

    public PvpMigration(UUID playerUuid) {
        this.playerUuid = playerUuid;
    }
}
