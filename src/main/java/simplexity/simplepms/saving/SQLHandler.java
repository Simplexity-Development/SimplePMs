package simplexity.simplepms.saving;

import simplexity.simplepms.SimplePMs;
import simplexity.simplepms.config.ConfigHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class SQLHandler extends SaveHandler {
    Connection connection;
    Logger logger = SimplePMs.getInstance().getLogger();
    private SQLHandler() {
    }
    private static SQLHandler instance;

    public static SQLHandler getInstance() {
        if (instance == null) {
            instance = new SQLHandler();
        }
        return instance;
    }

    @Override
    public void init() {
        try {
            connection = sqlOrSqlLite();
            try (Statement statement = connection.createStatement()) {
                statement.execute("""
                    CREATE TABLE IF NOT EXISTS blocklist (
                        player_uuid_and_other_uuid VARCHAR (255) PRIMARY KEY,
                        player_uuid VARCHAR(36),
                        blocked_player_uuid VARCHAR(36)
                    );""");
            }
        } catch (SQLException e) {
            logger.severe("Failed to connect to database: " + e.getMessage());
        }

    }

    @Override
    public List<UUID> getBlockedPlayers(UUID playerUUID) {
        return List.of();
    }

    @Override
    public void addBlockedPlayer(UUID playerUUID, UUID blockedPlayerUUID) {

    }

    @Override
    public void removeBlockedPlayer(UUID playerUUID, UUID blockedPlayerUUID) {

    }

    @Override
    public boolean isBlocked(UUID playerUUID, UUID blockedPlayerUUID) {
        return false;
    }

    @Override
    public void setMessagesEnabled(UUID playerUUID, boolean enabled) {

    }

    @Override
    public boolean areMessagesEnabled(UUID playerUUID) {
        return false;
    }

    @Override
    public void setSocialSpyEnabled(UUID playerUUID, boolean enabled) {

    }

    @Override
    public boolean isSocialSpyEnabled(UUID playerUUID) {
        return false;
    }

    private Connection sqlOrSqlLite() throws SQLException {
        if (ConfigHandler.getInstance().isMysqlEnabled()) {
            return DriverManager.getConnection("jdbc:mysql://" + ConfigHandler.getInstance().getMysqlIp() + "/" + ConfigHandler.getInstance().getMysqlName(), ConfigHandler.getInstance().getMysqlUsername(), ConfigHandler.getInstance().getMysqlPassword());
        } else {
            return DriverManager.getConnection("jdbc:sqlite:" + SimplePMs.getInstance().getDataFolder() + "/homes.db");
        }
    }
}
