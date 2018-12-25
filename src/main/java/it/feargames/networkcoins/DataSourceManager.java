package it.feargames.networkcoins;

import ch.jalu.configme.SettingsManager;
import com.zaxxer.hikari.HikariDataSource;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;

@RequiredArgsConstructor
public class DataSourceManager {
    @NonNull
    private final SettingsManager configuration;
    @NonNull
    private final String poolName;

    private HikariDataSource dataSource;

    public boolean isConnected() {
        return dataSource != null && !dataSource.isClosed();
    }

    public void connect() {
        disconnect();
        dataSource = new HikariDataSource();
        dataSource.setPoolName(poolName);
        dataSource.setDriverClassName(configuration.getProperty(ConfigValues.STORAGE_DRIVER));
        dataSource.setJdbcUrl(configuration.getProperty(ConfigValues.STORAGE_JDBC_URL));
        dataSource.setUsername(configuration.getProperty(ConfigValues.STORAGE_USER));
        dataSource.setPassword(configuration.getProperty(ConfigValues.STORAGE_PASSWORD));
        dataSource.setMaximumPoolSize(configuration.getProperty(ConfigValues.STORAGE_MAXIMUM_POOL_SIZE));
        dataSource.setMaxLifetime(configuration.getProperty(ConfigValues.STORAGE_MAXIMUM_CONNECTION_LIFETIME));
    }

    public void disconnect() {
        if (!isConnected()) {
            return;
        }
        dataSource.close();
    }

    public Connection getConnection() throws SQLException {
        if (!isConnected()) {
            throw new IllegalStateException("DataSource is closed!");
        }
        return dataSource.getConnection();
    }
}
