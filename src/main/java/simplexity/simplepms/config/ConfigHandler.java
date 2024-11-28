package simplexity.simplepms.config;

import org.bukkit.configuration.file.FileConfiguration;
import simplexity.simplepms.SimplePMs;

import java.util.ArrayList;
import java.util.List;

public class ConfigHandler {
    private static ConfigHandler instance;

    public static ConfigHandler getInstance() {
        if (instance == null) instance = new ConfigHandler();
        return instance;
    }

    private boolean mysqlEnabled, playersSendToConsole, playersSendToHiddenPlayers;
    private String mysqlIp, mysqlName, mysqlUsername, mysqlPassword;
    private List<String> validNamesForConsole = new ArrayList<>();

    public void loadConfigValues() {
        SimplePMs.getInstance().reloadConfig();
        FileConfiguration config = SimplePMs.getInstance().getConfig();
        validNamesForConsole.clear();
        mysqlEnabled = config.getBoolean("mysql.enabled");
        mysqlIp = config.getString("mysql.ip");
        mysqlName = config.getString("mysql.name");
        mysqlUsername = config.getString("mysql.username");
        mysqlPassword = config.getString("mysql.password");
        playersSendToConsole = config.getBoolean("allow-messaging.console", true);
        playersSendToHiddenPlayers = config.getBoolean("allow-messaging.hidden-players", false);
        validNamesForConsole = config.getStringList("valid-console-names");
    }

    public boolean isMysqlEnabled() {
        return mysqlEnabled;
    }

    public String getMysqlIp() {
        return mysqlIp;
    }

    public String getMysqlName() {
        return mysqlName;
    }

    public String getMysqlUsername() {
        return mysqlUsername;
    }

    public String getMysqlPassword() {
        return mysqlPassword;
    }

    public boolean canPlayersSendToConsole() {
        return playersSendToConsole;
    }

    public boolean canPlayersSendToHiddenPlayers() {
        return playersSendToHiddenPlayers;
    }

    public List<String> getValidNamesForConsole() {
        return validNamesForConsole;
    }
}
