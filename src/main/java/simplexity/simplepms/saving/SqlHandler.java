package simplexity.simplepms.saving;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.slf4j.Logger;
import simplexity.simplepms.SimplePMs;
import simplexity.simplepms.config.ConfigHandler;
import simplexity.simplepms.saving.objects.PlayerBlock;
import simplexity.simplepms.saving.objects.PlayerSettings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

@SuppressWarnings({"CallToPrintStackTrace", "SqlResolve", "StringTemplateMigration"})
public class SqlHandler {

    private SqlHandler() {
    }

    private static SqlHandler instance;

    public static SqlHandler getInstance() {
        if (instance == null) {
            instance = new SqlHandler();
        }
        return instance;
    }


    private static final HikariConfig hikariConfig = new HikariConfig();
    private static HikariDataSource dataSource;
    private static final Plugin plugin = SimplePMs.getInstance();
    private final Logger logger = SimplePMs.getInstance().getSLF4JLogger();


    public void init() {
        setupConfig();
        try (Connection connection = getConnection()) {
            PreparedStatement blocklistInitStatement = connection.prepareStatement("""
                    CREATE TABLE IF NOT EXISTS blocklist (
                       player_uuid VARCHAR (36) NOT NULL,
                       blocked_player_uuid VARCHAR(36) NOT NULL,
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
        } catch (SQLException e) {
            logger.warn("Failed to connect to database: {}", e.getMessage(), e);
        }
    }

    public void getSettings(UUID playerUUID, Consumer<PlayerSettings> callback) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
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
                logger.warn("Failed to retrieve settings from database: {}", e.getMessage(), e);
            }
            PlayerSettings finalSettings = settings;
            Bukkit.getScheduler().runTask(plugin, () -> callback.accept(finalSettings));
        });
    }

    public void addBlockedPlayer(UUID playerUUID, UUID blockedPlayerUUID, String reason) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            String queryString = "REPLACE INTO blocklist (player_uuid, blocked_player_uuid, block_reason) VALUES (?, ?, ?);";
            try (Connection connection = getConnection()) {
                PreparedStatement statement = connection.prepareStatement(queryString);
                statement.setString(1, String.valueOf(playerUUID));
                statement.setString(2, String.valueOf(blockedPlayerUUID));
                statement.setString(3, reason);
                statement.executeUpdate();
            } catch (SQLException e) {
                logger.warn("Failed to add blocked player: {}", e.getMessage(), e);
                e.printStackTrace();
            }
        });
    }

    public void removeBlockedPlayer(UUID playerUUID, UUID blockedPlayerUUID) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            String queryString = "DELETE FROM blocklist WHERE player_uuid = ? and blocked_player_uuid = ?;";
            try (Connection connection = getConnection()) {
                PreparedStatement statement = connection.prepareStatement(queryString);
                statement.setString(1, String.valueOf(playerUUID));
                statement.setString(2, String.valueOf(blockedPlayerUUID));
                statement.executeUpdate();
            } catch (SQLException e) {
                logger.warn("Failed to remove blocked player: {}", e.getMessage(), e);
            }
        });
    }

    public void getBlockedPlayers(UUID playerUUID, Consumer<List<PlayerBlock>> callback) {
        String queryString = "SELECT blocked_player_uuid, block_reason from blocklist WHERE player_uuid = ?;";
        List<PlayerBlock> blockedPlayers = new ArrayList<>();
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(queryString);
            statement.setString(1, String.valueOf(playerUUID));
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    UUID blockedPlayerUUID = UUID.fromString(resultSet.getString("blocked_player_uuid"));
                    String blockedPlayerName = Bukkit.getOfflinePlayer(blockedPlayerUUID).getName();
                    String reason = resultSet.getString("block_reason");
                    PlayerBlock block = new PlayerBlock(playerUUID, blockedPlayerName, blockedPlayerUUID, reason);
                    blockedPlayers.add(block);
                }
            }
            Bukkit.getScheduler().runTask(plugin, () -> callback.accept(blockedPlayers));
        } catch (SQLException e) {
            logger.warn("Failed to get blocked players: {}", e.getMessage(), e);
        }
    }

    public void updateSettings(UUID playerUUID, boolean socialSpyEnabled, boolean messagesDisabled) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            String queryString = "REPLACE INTO settings (player_uuid, socialspy_enabled, messages_disabled) VALUES (?, ?, ?);";
            try (Connection connection = getConnection()) {
                PreparedStatement statement = connection.prepareStatement(queryString);
                statement.setString(1, String.valueOf(playerUUID));
                statement.setBoolean(2, socialSpyEnabled);
                statement.setBoolean(3, messagesDisabled);
                statement.executeUpdate();
            } catch (SQLException e) {
                logger.warn("Failed to update settings to database: {}", e.getMessage(), e);
            }
        });

    }

    private void setupConfig() {
        if (!ConfigHandler.getInstance().isMysqlEnabled()) {
            hikariConfig.setJdbcUrl("jdbc:sqlite:" + SimplePMs.getInstance().getDataFolder() + "/simple-pms.db");
            dataSource = new HikariDataSource(hikariConfig);
            return;
        }
        hikariConfig.setJdbcUrl("jdbc:mysql://" + ConfigHandler.getInstance().getMysqlIp() + "/" + ConfigHandler.getInstance().getMysqlName());
        hikariConfig.setUsername(ConfigHandler.getInstance().getMysqlUsername());
        hikariConfig.setPassword(ConfigHandler.getInstance().getMysqlPassword());
        dataSource = new HikariDataSource(hikariConfig);
    }

    private static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
