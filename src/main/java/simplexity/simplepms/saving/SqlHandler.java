package simplexity.simplepms.saving;

import org.bukkit.Bukkit;
import simplexity.simplepms.SimplePMs;
import simplexity.simplepms.config.ConfigHandler;
import simplexity.simplepms.objects.PlayerBlock;
import simplexity.simplepms.objects.PlayerSettings;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@SuppressWarnings("CallToPrintStackTrace")
public class SqlHandler {
    private Connection connection;
    private final Logger logger = SimplePMs.getInstance().getLogger();
    private static final String blocklistInitStatement = """
            CREATE TABLE IF NOT EXISTS blocklist (
               player_uuid VARCHAR (36) NOT NULL,
               blocked_player_uuid VARCHAR(36) NOT NULL,
               block_reason VARCHAR(256),
               PRIMARY KEY (player_uuid, blocked_player_uuid)
            );""";

    private static final String settingsInitStatement = """
            CREATE TABLE IF NOT EXISTS settings (
               player_uuid VARCHAR (36) NOT NULL PRIMARY KEY,
               socialspy_enabled BOOLEAN NOT NULL,
               messages_disabled BOOLEAN NOT NULL
            );""";

    private static final String settingsUpdateStatement = """
            REPLACE INTO settings (player_uuid, socialspy_enabled, messages_disabled)
            VALUES (?, ?, ?);""";

    private static final String settingsSelection = """
            SELECT socialspy_enabled, messages_disabled
            FROM settings
            WHERE player_uuid = ?;""";

    private static final String blocklistUpdateStatement = """
            REPLACE INTO blocklist (player_uuid, blocked_player_uuid, block_reason)
            VALUES (?, ?, ?);""";

    private static final String deleteBlockStatement = """
            DELETE FROM blocklist
            WHERE player_uuid = ? and blocked_player_uuid = ?;""";

    private static final String blockSelection = """
            SELECT blocked_player_uuid, block_reason
            from blocklist
            WHERE player_uuid = ?;""";

    private SqlHandler() {
    }

    private static SqlHandler instance;

    public static SqlHandler getInstance() {
        if (instance == null) {
            instance = new SqlHandler();
        }
        return instance;
    }


    public void init() {
        try {
            connection = sqlOrSqlLite();
            try (Statement statement = connection.createStatement()) {
                statement.execute(blocklistInitStatement);
                statement.execute(settingsInitStatement);
            }
        } catch (SQLException e) {
            logger.severe("Failed to connect to database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public PlayerSettings getSettings(UUID playerUUID) {
        try (PreparedStatement statement = connection.prepareStatement(settingsSelection)) {
            statement.setString(1, String.valueOf(playerUUID));
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    PlayerSettings settings = new PlayerSettings(playerUUID);
                    updateSettings(playerUUID, settings);
                    return settings;
                } else {
                    boolean socialSpy = resultSet.getBoolean("socialspy_enabled");
                    boolean messagesDisabled = resultSet.getBoolean("messages_disabled");
                    return new PlayerSettings(playerUUID, socialSpy, messagesDisabled);
                }
            }
        } catch (SQLException e) {
            logger.severe("Failed to retrieve settings from database: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public void addBlockedPlayer(UUID playerUUID, UUID blockedPlayerUUID, String reason) {
        try (PreparedStatement statement = connection.prepareStatement(blocklistUpdateStatement)) {
            statement.setString(1, String.valueOf(playerUUID));
            statement.setString(2, String.valueOf(blockedPlayerUUID));
            statement.setString(3, reason);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.severe("Failed to add blocked player: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void removeBlockedPlayer(UUID playerUUID, UUID blockedPlayerUUID) {
        try (PreparedStatement statement = connection.prepareStatement(deleteBlockStatement)) {
            statement.setString(1, String.valueOf(playerUUID));
            statement.setString(2, String.valueOf(blockedPlayerUUID));
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.severe("Failed to remove blocked player: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<PlayerBlock> getBlockedPlayers(UUID playerUUID) {
        List<PlayerBlock> blockedPlayers = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(blockSelection)) {
            statement.setString(1, String.valueOf(playerUUID));
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) return null;
                while (resultSet.next()) {
                    UUID blockedPlayerUUID = UUID.fromString(resultSet.getString("blocked_player_uuid"));
                    String blockedPlayerName = Bukkit.getOfflinePlayer(blockedPlayerUUID).getName();
                    String reason = resultSet.getString("block_reason");
                    PlayerBlock block = new PlayerBlock(playerUUID, blockedPlayerName, blockedPlayerUUID, reason);
                    blockedPlayers.add(block);
                }
            }
            return blockedPlayers;
        } catch (SQLException e) {
            logger.severe("Failed to get blocked players: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public void updateSettings(UUID playerUUID, PlayerSettings settings) {
        try (PreparedStatement statement = connection.prepareStatement(settingsUpdateStatement)) {
            statement.setString(1, String.valueOf(playerUUID));
            statement.setBoolean(2, settings.isSocialSpyEnabled());
            statement.setBoolean(3, settings.areMessagesDisabled());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.severe("Failed to update settings to database: " + e.getMessage());
            e.printStackTrace();
        }

    }

    private Connection sqlOrSqlLite() throws SQLException {
        if (ConfigHandler.getInstance().isMysqlEnabled()) {
            return DriverManager.getConnection("jdbc:mysql://" + ConfigHandler.getInstance().getMysqlIp() + "/" + ConfigHandler.getInstance().getMysqlName(), ConfigHandler.getInstance().getMysqlUsername(), ConfigHandler.getInstance().getMysqlPassword());
        } else {
            return DriverManager.getConnection("jdbc:sqlite:" + SimplePMs.getInstance().getDataFolder() + "/simple-pms.db");
        }
    }
}
