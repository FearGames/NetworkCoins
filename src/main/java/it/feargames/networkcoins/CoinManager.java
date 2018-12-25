package it.feargames.networkcoins;

import ch.jalu.configme.SettingsManager;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@RequiredArgsConstructor
public class CoinManager {
    @NonNull
    private final SettingsManager configuration;
    @NonNull
    private final DataSourceManager dataSource;

    public void reset(@NonNull UUID uniqueId) {
        try (Connection connection = dataSource.getConnection()) {
            final String query = "DELETE FROM NetworkCoins WHERE player_uuid = ?;";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, uniqueId.toString());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Unable to fetch the balance of player " + uniqueId + "!");
        }
    }

    public int getBalance(@NonNull UUID uniqueId) {
        try (Connection connection = dataSource.getConnection()) {
            final String query = "SELECT * FROM NetworkCoins WHERE player_uuid = ?;";
            ResultSet result;
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, uniqueId.toString());
                result = statement.executeQuery();
            }
            if (!result.next()) {
                return configuration.getProperty(ConfigValues.STARTING_BALANCE);
            }
            return result.getInt("balance");
        } catch (SQLException e) {
            throw new RuntimeException("Unable to fetch the balance of player " + uniqueId + "!");
        }
    }

    public void setBalance(@NonNull UUID uniqueId, int balance) {
        if (balance < 0) {
            throw new IllegalArgumentException("New balance can't be negative!");
        }
        try (Connection connection = dataSource.getConnection()) {
            final String query = "INSERT INTO NetworkCoins (player_uuid, balance) VALUES (?, ?) "
                    + "ON DUPLICATE KEY UPDATE balance=VALUES(balance);";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, uniqueId.toString());
                statement.setInt(2, balance);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Unable to fetch the balance of player " + uniqueId + "!");
        }
    }

    public void giveAmount(@NonNull UUID uniqueId, int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("The given amount can't be negative!");
        }
        long newBalance = getBalance(uniqueId) + amount;
        if (newBalance > configuration.getProperty(ConfigValues.MAXIMUM_BALANCE)) {
            return;
        }
        setBalance(uniqueId, (int) newBalance);
    }

    public boolean takeAmount(@NonNull UUID uniqueId, int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("The taken amount can't be negative!");
        }
        long newBalance = getBalance(uniqueId) + amount;
        if (newBalance < configuration.getProperty(ConfigValues.MINIMUM_BALANCE)) {
            return false;
        }
        setBalance(uniqueId, (int) newBalance);
        return true;
    }
}
