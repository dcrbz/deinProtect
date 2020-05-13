package bz.dcr.deinprotect.persistence;

import bz.dcr.deinprotect.block.BlockLocation;
import bz.dcr.deinprotect.protection.entity.Protection;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLPersistence implements Persistence {

    private HikariDataSource dataSource;

    public MySQLPersistence(String uri, String username, String password) {
        final HikariConfig config = new HikariConfig();
        config.setJdbcUrl(uri);
        config.setUsername(username);
        config.setPassword(password);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        dataSource = new HikariDataSource(config);
    }

    private void prepareDatabase() {
        // TODO Create tables
        try (Connection con = dataSource.getConnection();
             Statement stmt = con.createStatement()) {
            stmt.addBatch("CREATE TABLE IF NOT EXISTS protection (id INT NOT NULL AUTO_INCREMENT);");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws IOException {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    @Override
    public void saveProtection(Protection protection) {
        if (protection.getId() == null) {
            insertProtection(protection);
        } else {
            updateProtection(protection);
        }
    }

    private void insertProtection(Protection protection) {

    }

    private void updateProtection(Protection protection) {

    }

    @Override
    public void deleteProtection(Protection protection) {

    }

    @Override
    public Protection getProtection(BlockLocation location) {
        return null;
    }
}
