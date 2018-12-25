package it.feargames.networkcoins;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import ch.jalu.configme.migration.PlainMigrationService;
import it.feargames.networkcoins.command.NetworkCoinsCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Objects;
import java.util.logging.Level;

public class NetworkCoins extends JavaPlugin {
    private SettingsManager configuration;
    private DataSourceManager dataSource;
    private CoinManager coinManager;

    public CoinManager getCoinManager() {
        return Objects.requireNonNull(coinManager, "Plugin not yet initialized!");
    }

    @Override
    public void onEnable() {
        // Load configuration
        configuration = SettingsManagerBuilder.withYamlFile(new File(getDataFolder(), "config.yml"))
                .configurationData(ConfigValues.class)
                .migrationService(new PlainMigrationService())
                .create();

        // Database connection
        getLogger().info("Connecting to the database...");
        dataSource = new DataSourceManager(configuration, getName());
        try {
            dataSource.connect();
            try (Connection connection = dataSource.getConnection()) {
                final String query = "CREATE TABLE IF NOT EXISTS NetworkCoins"
                        + " (player_uuid VARCHAR(50) PRIMARY KEY, balance INTEGER NOT NULL)";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.execute();
                }
            }
        } catch (Exception e) {
            getLogger().log(Level.WARNING, "Unable to connect to the database! The plugin will be disabled.", e);
            getServer().getPluginManager().disablePlugin(this);
        }
        getLogger().info("Connected!");

        // Initialize API
        coinManager = new CoinManager(configuration, dataSource);

        // Register commands
        new NetworkCoinsCommand(configuration, coinManager).register(this);
    }

    public void reload() {
        getCoinManager(); // Check if plugin is initialized
        configuration.reload();
        dataSource.connect();
    }

    @Override
    public void onDisable() {
        if (dataSource != null) {
            dataSource.disconnect();
        }
    }
}
