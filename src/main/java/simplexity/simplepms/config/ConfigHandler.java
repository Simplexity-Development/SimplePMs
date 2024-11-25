package simplexity.simplepms.config;

import org.bukkit.configuration.file.FileConfiguration;
import simplexity.simplepms.SimplePMs;

public class ConfigHandler {
    private static ConfigHandler instance;

    public static ConfigHandler getInstance() {
        if (instance == null) instance = new ConfigHandler();
        return instance;
    }

    private boolean mysqlEnabled;
    private String mysqlIp, mysqlName, mysqlUsername, mysqlPassword;

    public void loadConfigValues() {
        SimplePMs.getInstance().reloadConfig();
        FileConfiguration config = SimplePMs.getInstance().getConfig();
        mysqlEnabled = config.getBoolean("mysql.enabled");
        mysqlIp = config.getString("mysql.ip");
        mysqlName = config.getString("mysql.name");
        mysqlUsername = config.getString("mysql.username");
        mysqlPassword = config.getString("mysql.password");

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
}
