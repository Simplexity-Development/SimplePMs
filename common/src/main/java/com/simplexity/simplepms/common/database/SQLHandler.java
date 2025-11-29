package com.simplexity.simplepms.common.database;

import com.simplexity.simplepms.common.database.objects.PlayerBlock;
import com.simplexity.simplepms.common.database.objects.PlayerSettings;
import com.simplexity.simplepms.common.logger.Logger;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SQLHandler {

    private SQLHandler() { }

    private static SQLHandler instance;
    private static boolean usingMySQL;

    public static SQLHandler getInstance() {
        if (instance == null) {
            instance = new SQLHandler();
        }
        return instance;
    }

    private static HikariDataSource dataSource;
    private static final HikariConfig hikariConfig = new HikariConfig();

    public void init() {
        if (dataSource == null) {
            // TODO: Logger error, need to call setupConfig first
            return;
        }
        try (Connection connection = getConnection()) {
            PreparedStatement blocklistInitStatement = connection.prepareStatement("""
                    CREATE TABLE IF NOT EXISTS blocklist (
                       player_uuid VARCHAR (36) NOT NULL,
                       blocked_player_uuid VARCHAR(36) NOT NULL,
                       blocked_player_name VARCHAR(256),
                       block_reason VARCHAR(256),
                       PRIMARY KEY (player_uuid, blocked_player_uuid)
                    );""");
            blocklistInitStatement.execute();
            PreparedStatement playerSettingsInitStatement = connection.prepareStatement("""
                    CREATE TABLE IF NOT EXISTS settings (
                       player_uuid VARCHAR (36) NOT NULL PRIMARY KEY,
                       socialspy_enabled BOOLEAN NOT NULL,
                       messages_disabled BOOLEAN NOT NULL
                    );""");
            playerSettingsInitStatement.execute();
            updateDatabaseColumns();
        } catch (SQLException e) {
            Logger.getLogger().warn("Failed to connect to database: {}", e.getMessage(), e);
        }
    }

    public void reloadDatabase(String jdbcUrl, String username, String password) {
        Logger.getLogger().info("Reconnecting to SimplePMs database...");
        shutdownConnection();
        setupConfig(jdbcUrl, username, password);
        Logger.getLogger().info("Database reloaded successfully");
    }


    public PlayerSettings getSettings(UUID playerUUID) {
        String queryString = "SELECT socialspy_enabled, messages_disabled FROM settings WHERE player_uuid = ?;";
        PlayerSettings settings = null;
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(queryString);
            statement.setString(1, String.valueOf(playerUUID));
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    settings = new PlayerSettings(playerUUID);
                    updateSettings(playerUUID, settings.isSocialSpyEnabled(), settings.areMessagesDisabled());
                } else {
                    boolean socialSpy = resultSet.getBoolean("socialspy_enabled");
                    boolean messagesDisabled = resultSet.getBoolean("messages_disabled");
                    settings = new PlayerSettings(playerUUID, socialSpy, messagesDisabled);
                }
            }
        } catch (SQLException e) {
            Logger.getLogger().warn("Failed to retrieve settings from database: {}", e.getMessage(), e);
        }
        return settings;
    }

    public List<PlayerBlock> getBlockedPlayers(UUID playerUUID) {
        String queryString = "SELECT blocked_player_uuid, block_reason, blocked_player_name from blocklist WHERE player_uuid = ?;";
        List<PlayerBlock> blockedPlayers = new ArrayList<>();
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(queryString);
            statement.setString(1, String.valueOf(playerUUID));
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    UUID blockedPlayerUUID = UUID.fromString(resultSet.getString("blocked_player_uuid"));
                    String blockedPlayerName = resultSet.getString("blocked_player_name");
                    String reason = resultSet.getString("block_reason");
                    PlayerBlock block = new PlayerBlock(playerUUID, blockedPlayerName, blockedPlayerUUID, reason);
                    blockedPlayers.add(block);
                }
            }
        } catch (SQLException e) {
            Logger.getLogger().warn("Failed to get blocked players: {}", e.getMessage(), e);
        }
        return blockedPlayers;
    }

    public void addBlockedPlayer(UUID playerUUID, UUID blockedPlayerUUID, String blockedPlayerName, String reason) {
        String queryString = "REPLACE INTO blocklist (player_uuid, blocked_player_uuid, blocked_player_name, block_reason) VALUES (?, ?, ?, ?);";
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(queryString);
            statement.setString(1, String.valueOf(playerUUID));
            statement.setString(2, String.valueOf(blockedPlayerUUID));
            statement.setString(3, blockedPlayerName);
            statement.setString(4, reason);
            statement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger().warn("Failed to add blocked player: {}", e.getMessage(), e);
            e.printStackTrace();
        }
    }

    public void removeBlockedPlayer(UUID playerUUID, UUID blockedPlayerUUID) {
        String queryString = "DELETE FROM blocklist WHERE player_uuid = ? and blocked_player_uuid = ?;";
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(queryString);
            statement.setString(1, String.valueOf(playerUUID));
            statement.setString(2, String.valueOf(blockedPlayerUUID));
            statement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger().warn("Failed to remove blocked player: {}", e.getMessage(), e);
        }
    }

    public void updateSettings(UUID playerUUID, boolean socialSpyEnabled, boolean messagesDisabled) {
        String queryString = "REPLACE INTO settings (player_uuid, socialspy_enabled, messages_disabled) VALUES (?, ?, ?);";
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(queryString);
            statement.setString(1, String.valueOf(playerUUID));
            statement.setBoolean(2, socialSpyEnabled);
            statement.setBoolean(3, messagesDisabled);
            statement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger().warn("Failed to update settings to database: {}", e.getMessage(), e);
        }

    }

    public void updateDatabaseColumns() {
        String tableName = "blocklist";
        String columnName = "blocked_player_name";
        boolean columnExists;
        try {
            if (usingMySQL) {
                columnExists = doesMysqlColumnExist(tableName, columnName);
            } else {
                columnExists = doesSqliteColumnExist(tableName, columnName);
            }
            if (!columnExists) {
                addColumn(tableName, columnName, "VARCHAR(256)", "");
            }
        } catch (Exception e) {
            Logger.getLogger().warn("Unable to update database table: {} column: {}, error: {}", tableName, columnName, e.getMessage(), e);
        }
    }

    private Boolean doesSqliteColumnExist(String tableName, String columnName) {
        String query = "PRAGMA table_info(" + tableName + ")";
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                if (columnName.equalsIgnoreCase(resultSet.getString("name"))) {
                    return true;
                }
            }
        } catch (SQLException e) {
            Logger.getLogger().warn("Failed to to check for column {} in table {}: {}", columnName, tableName, e.getMessage(), e);
        }
        return false;
    }

    private Boolean doesMysqlColumnExist(String tableName, String columnName) {
        try (Connection connection = getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getColumns(null, null, tableName, columnName);
            return resultSet.next();
        } catch (SQLException e) {
            Logger.getLogger().warn("Failed to check for column {} in table {}: {}", columnName, tableName, e.getMessage(), e);
        }
        return false;
    }

    // Possibly extremely cursed way to do this :cackle:

    public void addColumn(String tableName, String columnName, String dataType, String constraints) {
        String query = String.format("ALTER TABLE %s ADD COLUMN %s %s %s;", tableName, columnName, dataType, constraints);
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.executeUpdate();
            Logger.getLogger().info("Added new column '{}' to table '{}'", columnName, tableName);
        } catch (SQLException e) {
            Logger.getLogger().warn("Failed to add new column {} to table {}: {}", columnName, tableName, e.getMessage(), e);
        }
    }

    private static void loadHikariDataSource() {
        if (hikariConfig == null) return; // TODO: Logger for missing hikariConfig
        dataSource = new HikariDataSource(hikariConfig);
    }

    public void setupConfig(String jdbcUrl, String username, String password) {
        if (!usingMySQL) {
            hikariConfig.setJdbcUrl(jdbcUrl);
            hikariConfig.setConnectionTestQuery("PRAGMA journal_mode = WAL;");
            loadHikariDataSource();
            return;
        }
        hikariConfig.setJdbcUrl(jdbcUrl);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        loadHikariDataSource();
    }

    public static void setUsingMySQL(boolean usingMySQL) {
        SQLHandler.usingMySQL = usingMySQL;
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void shutdownConnection() {
        if (dataSource == null || dataSource.isClosed()) return;
        dataSource.close();
        dataSource = null;
        Logger.getLogger().info("Closed existing database connection");
    }
}