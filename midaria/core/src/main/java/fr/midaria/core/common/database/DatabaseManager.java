package fr.midaria.core.common.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;

import java.sql.SQLException;

public class DatabaseManager {
    public DatabaseManager() {
        createTables();
    }

    private void createTables() {
        try {
            JdbcConnectionSource connectionSource = getConnection();
            connectionSource.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private JdbcConnectionSource getConnection() {
        try {
            return new JdbcConnectionSource("jdbc:mysql://45.154.96.86:3306/s6_Main", "u6_hgw4ufc2IJ", "gcP1nZFrm.JuPO!WSwr1BFDd");
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
}
