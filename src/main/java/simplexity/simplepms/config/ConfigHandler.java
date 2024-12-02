package simplexity.simplepms.config;

import org.bukkit.configuration.file.FileConfiguration;
import simplexity.simplepms.SimplePMs;
import simplexity.simplepms.saving.SqlHandler;

import java.util.ArrayList;
import java.util.List;

public class ConfigHandler {
    private static ConfigHandler instance;

    public static ConfigHandler getInstance() {
        if (instance == null) instance = new ConfigHandler();
        return instance;
    }

    private boolean mysqlEnabled, playersSendToConsole, playersSendToHiddenPlayers, consoleHasSocialSpy;
    private String mysqlIp, mysqlName, mysqlUsername, mysqlPassword, normalFormat, socialSpyFormat;
    private List<String> validNamesForConsole = new ArrayList<>();

    public void loadConfigValues() {
        SimplePMs.getInstance().reloadConfig();
        FileConfiguration config = SimplePMs.getInstance().getConfig();
        SqlHandler.getInstance().init();
        LocaleHandler.getInstance().reloadLocale();
        validNamesForConsole.clear();
        normalFormat = config.getString("format.normal", "<displayname>");
        socialSpyFormat = config.getString("format.social-spy", "<username>");
        mysqlEnabled = config.getBoolean("mysql.enabled", false);
        mysqlIp = config.getString("mysql.ip", "localhost:3306");
        mysqlName = config.getString("mysql.name", "homes");
        mysqlUsername = config.getString("mysql.username", "username1");
        mysqlPassword = config.getString("mysql.password", "badpassword!");
        playersSendToConsole = config.getBoolean("allow-messaging.console", true);
        playersSendToHiddenPlayers = config.getBoolean("allow-messaging.hidden-players", false);
        validNamesForConsole = config.getStringList("valid-console-names");
        consoleHasSocialSpy = config.getBoolean("console-has-social-spy", true);
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





    public boolean doesConsoleHaveSocialSpy() {
        return consoleHasSocialSpy;
    }

    public String getNormalFormat() {
        return normalFormat;
    }
    public String getSocialSpyFormat() {
        return socialSpyFormat;
    }
}
