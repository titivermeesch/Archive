package fr.midaria.pvparena.common.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;
import fr.midaria.pvparena.common.database.models.EvolutionProgress;
import fr.midaria.pvparena.common.database.models.PlayerPvpStat;
import fr.midaria.pvparena.common.database.models.PvpMigration;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DatabaseManager {
    public DatabaseManager() {
        createTables();
    }

    private final ArrayList<Dao<Class<?>, String>> daos = new ArrayList<>();

    private void createTables() {
        try {
            JdbcConnectionSource connectionSource = getConnection();
            TableUtils.createTableIfNotExists(connectionSource, PlayerPvpStat.class);
            TableUtils.createTableIfNotExists(connectionSource, PvpMigration.class);
            TableUtils.createTableIfNotExists(connectionSource, EvolutionProgress.class);
            connectionSource.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private JdbcConnectionSource getConnection() {
        try {
            return new JdbcConnectionSource("jdbc:mysql://62.4.17.35:3306/main", "admin", "medionakoya");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public <D extends Dao<T, ?>, T> D getDao(Class<T> clazz) throws SQLException {
        JdbcConnectionSource connectionSource = getConnection();
        Dao<T, ?> dao = DaoManager.lookupDao(connectionSource, clazz);
        if (dao == null) {
            dao = DaoManager.createDao(connectionSource, clazz);
        }

        @SuppressWarnings("unchecked")
        D castDao = (D) dao;
        return castDao;
    }

    public Dao<PlayerPvpStat, String> getPvpStatDao() throws SQLException {
        return getDao(PlayerPvpStat.class);
    }

    public Dao<PvpMigration, String> getPvpMigrationDao() throws SQLException {
        return getDao(PvpMigration.class);
    }

    public Dao<EvolutionProgress, String> getEvolutionProgressDao() throws SQLException {
        return getDao(EvolutionProgress.class);
    }
}
