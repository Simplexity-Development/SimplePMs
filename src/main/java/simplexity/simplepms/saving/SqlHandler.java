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
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@SuppressWarnings("CallToPrintStackTrace")
public class SqlHandler {
    Connection connection;
    Logger logger = SimplePMs.getInstance().getLogger();

    private SqlHandler() {
    }

    public static final HashMap<UUID, List<PlayerBlock>> blockList = new HashMap<>();
    public static final HashMap<UUID, PlayerSettings> playerSettings = new HashMap<>();

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
                statement.execute("""
                        CREATE TABLE IF NOT EXISTS blocklist (
                            player_uuid VARCHAR (36) NOT NULL,
                            blocked_player_uuid VARCHAR(36) NOT NULL,
                            block_reason VARCHAR(256),
                            PRIMARY KEY (player_uuid, blocked_player_uuid)
                        );""");
                statement.execute("""
                        CREATE TABLE IF NOT EXISTS settings (
                            player_uuid VARCHAR (36) NOT NULL PRIMARY KEY,
                            socialspy_enabled BOOLEAN NOT NULL,
                            messages_disabled BOOLEAN NOT NULL
                        );
                        """);
            }
        } catch (SQLException e) {
            logger.severe("Failed to connect to database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<PlayerBlock> getBlockList(UUID uuid) {
        if (blockList.containsKey(uuid)) {
            return blockList.get(uuid);
        }
        addBlockListToCache(uuid);
        return blockList.get(uuid);
    }

    public PlayerSettings getSettings(UUID uuid) {
        if (playerSettings.containsKey(uuid)) {
            return playerSettings.get(uuid);
        }
        addSettingsToCache(uuid);
        return playerSettings.get(uuid);
    }

    public void addBlockedPlayer(UUID playerUUID, UUID blockedPlayerUUID, String reason) {
        String query = """
                REPLACE INTO blocklist (player_uuid, blocked_player_uuid, block_reason)
                VALUES (?, ?, ?);
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, String.valueOf(playerUUID));
            statement.setString(2, String.valueOf(blockedPlayerUUID));
            statement.setString(3, reason);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.severe("Failed to add blocked player: " + e.getMessage());
            e.printStackTrace();
        }
        addBlockedPlayerToList(playerUUID, blockedPlayerUUID, reason);
    }

    public void removeBlockedPlayer(UUID playerUUID, UUID blockedPlayerUUID) {
        String query = """
              DELETE FROM blocklist
              WHERE player_uuid = ? and blocked_player_uuid = ?;
              """;
        try (PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, String.valueOf(playerUUID));
            statement.setString(2, String.valueOf(blockedPlayerUUID));
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.severe("Failed to remove blocked player: " + e.getMessage());
            e.printStackTrace();
            return;
        }
        removeBlockedPlayerFromList(playerUUID, blockedPlayerUUID);
    }

    public void setSocialSpyEnabled(UUID playerUUID, boolean socialSpyEnabled) {
        String query = """
                UPDATE settings
                SET socialspy_enabled = ?
                WHERE player_uuid = ?;
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)){
            statement.setBoolean(1, socialSpyEnabled);
            statement.setString(2, String.valueOf(playerUUID));
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.severe("Failed to update social spy settings: " + e.getMessage());
            e.printStackTrace();
            return;
        }
        updateCachedSocialSpySettings(playerUUID, socialSpyEnabled);
    }

    public void setMessagesDisabled(UUID playerUUID, boolean messagesDisabled) {
        String query = """
                UPDATE settings
                SET messages_disabled = ?
                WHERE player_uuid = ?;
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)){
            statement.setBoolean(1, messagesDisabled);
            statement.setString(2, String.valueOf(playerUUID));
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.severe("Failed to update message settings: " + e.getMessage());
            e.printStackTrace();
            return;
        }
        updateCachedMessageSettings(playerUUID, messagesDisabled);
    }

    private void addBlockListToCache(UUID playerUuid) {
        String query = """
                SELECT blocked_player_uuid, block_reason
                from blocklist
                WHERE player_uuid = ?;
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, String.valueOf(playerUuid));
            try (ResultSet resultSet = statement.executeQuery()) {
                List<PlayerBlock> playerBlocks = new ArrayList<>();
                while (resultSet.next()) {
                    String blockReason = resultSet.getString("block_reason");
                    UUID blockedPlayerUuid = UUID.fromString(resultSet.getString("blocked_player_uuid"));
                    String playerName = Bukkit.getOfflinePlayer(blockedPlayerUuid).getName();
                    playerBlocks.add(new PlayerBlock(playerUuid, playerName, blockedPlayerUuid, blockReason));
                }
                blockList.put(playerUuid, playerBlocks);
            }
        } catch (SQLException e) {
            logger.severe("Failed to add blocklist cache: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void addSettingsToCache(UUID playerUuid) {
        String query = """
                SELECT socialspy_enabled, messages_disabled
                from settings where player_uuid = ?;
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, String.valueOf(playerUuid));
            try (ResultSet resultSet = statement.executeQuery()) {
                boolean socialSpyEnabled = resultSet.getBoolean("socialspy_enabled");
                boolean messagesDisabled = resultSet.getBoolean("messages_disabled");
                playerSettings.put(playerUuid, new PlayerSettings(playerUuid, socialSpyEnabled, messagesDisabled));
            }
        } catch (SQLException e) {
            logger.severe("Failed to add settings to cache: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void addBlockedPlayerToList(UUID playerUUID, UUID blockedPlayerUUID, String reason) {
        List<PlayerBlock> blockedPlayers = blockList.get(playerUUID);
        if (blockedPlayers == null) {
            blockedPlayers = new ArrayList<>();
        }
        removeBlockedPlayerFromList(playerUUID, blockedPlayerUUID);
        String playerName = Bukkit.getOfflinePlayer(blockedPlayerUUID).getName();
        blockedPlayers.add(new PlayerBlock(playerUUID, playerName, blockedPlayerUUID, reason));
        blockList.put(playerUUID, blockedPlayers);
    }

    private void removeBlockedPlayerFromList(UUID playerUUID, UUID blockedPlayerUUID) {
        List<PlayerBlock> blockedPlayers = blockList.get(playerUUID);
        if (blockedPlayers == null) {
            return;
        }
        blockedPlayers.removeIf(block -> block.blockedPlayerUUID().equals(blockedPlayerUUID));
        blockList.put(playerUUID, blockedPlayers);
    }

    private void updateCachedSocialSpySettings(UUID playerUUID, boolean socialSpyEnabled) {
        PlayerSettings settings = playerSettings.get(playerUUID);
        if (settings == null) {
            settings = new PlayerSettings(playerUUID, socialSpyEnabled, true);
            playerSettings.put(playerUUID, settings);
            return;
        }
        settings.setSocialSpyEnabled(socialSpyEnabled);
        playerSettings.put(playerUUID, settings);
    }

    private void updateCachedMessageSettings(UUID playerUUID, boolean messagesDisabled) {
        PlayerSettings settings = playerSettings.get(playerUUID);
        if (settings == null) {
            settings = new PlayerSettings(playerUUID, false, messagesDisabled);
            playerSettings.put(playerUUID, settings);
            return;
        }
        settings.setMessagesDisabled(messagesDisabled);
        playerSettings.put(playerUUID, settings);
    }


    private Connection sqlOrSqlLite() throws SQLException {
        if (ConfigHandler.getInstance().isMysqlEnabled()) {
            return DriverManager.getConnection("jdbc:mysql://" + ConfigHandler.getInstance().getMysqlIp() + "/" + ConfigHandler.getInstance().getMysqlName(), ConfigHandler.getInstance().getMysqlUsername(), ConfigHandler.getInstance().getMysqlPassword());
        } else {
            return DriverManager.getConnection("jdbc:sqlite:" + SimplePMs.getInstance().getDataFolder() + "/simple-pms.db");
        }
    }
}
