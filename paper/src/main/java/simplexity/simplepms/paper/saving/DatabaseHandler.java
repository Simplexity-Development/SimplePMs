package simplexity.simplepms.paper.saving;

import com.simplexity.simplepms.common.database.SQLHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import simplexity.simplepms.paper.SimplePMs;
import simplexity.simplepms.paper.config.ConfigHandler;
import com.simplexity.simplepms.common.database.objects.PlayerBlock;
import com.simplexity.simplepms.common.database.objects.PlayerSettings;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings({"SqlResolve", "StringTemplateMigration", "SameParameterValue"})
public class DatabaseHandler {

    private DatabaseHandler() { }

    private static DatabaseHandler instance;

    public static DatabaseHandler getInstance() {
        if (instance == null) instance = new DatabaseHandler();
        return instance;
    }

    private static final Plugin plugin = SimplePMs.getInstance();

    public void init() {
        setupConfig();
        SQLHandler.setUsingMySQL(ConfigHandler.getInstance().isMysqlEnabled());
        SQLHandler.getInstance().init();
    }

    public void reloadDatabase() {
        shutdownConnection();
        setupConfig();
    }

    public CompletableFuture<PlayerSettings> getSettings(UUID playerUUID) {
        return CompletableFuture.supplyAsync(() -> SQLHandler.getInstance().getSettings(playerUUID));
    }

    public CompletableFuture<List<PlayerBlock>> getBlockedPlayers(UUID playerUUID) {
        return CompletableFuture.supplyAsync(() -> SQLHandler.getInstance().getBlockedPlayers(playerUUID));
    }

    public void addBlockedPlayer(UUID playerUUID, UUID blockedPlayerUUID, String blockedPlayerName, String reason) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> SQLHandler.getInstance().addBlockedPlayer(playerUUID, blockedPlayerUUID, blockedPlayerName, reason));
    }

    public void removeBlockedPlayer(UUID playerUUID, UUID blockedPlayerUUID) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> SQLHandler.getInstance().removeBlockedPlayer(playerUUID, blockedPlayerUUID));
    }

    public void updateSettings(UUID playerUUID, boolean socialSpyEnabled, boolean messagesDisabled) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> SQLHandler.getInstance().updateSettings(playerUUID, socialSpyEnabled, messagesDisabled));

    }

    private void setupConfig() {
        if (ConfigHandler.getInstance().isMysqlEnabled()) {
            SQLHandler.getInstance().setupConfig(
                    "jdbc:mysql://" + ConfigHandler.getInstance().getMysqlIp() + "/" + ConfigHandler.getInstance().getMysqlName(),
                    ConfigHandler.getInstance().getMysqlUsername(),
                    ConfigHandler.getInstance().getMysqlPassword()
            );
        }
        else {
            SQLHandler.getInstance().setupConfig(
                    "jdbc:sqlite:" + SimplePMs.getInstance().getDataFolder() + "/simple-pms.db",
                    null,
                    null
            );
        }
    }

    public void shutdownConnection() {
        SQLHandler.getInstance().shutdownConnection();
    }
}
