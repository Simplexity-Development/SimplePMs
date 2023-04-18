package adhdmc.simplepms.config;

import adhdmc.simplepms.SimplePMs;
import adhdmc.simplepms.utils.Message;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class LocaleConfig {

    private static LocaleConfig instance;
    private final String fileName = "locale.yml";
    private final File localeFile = new File(SimplePMs.getInstance().getDataFolder(), fileName);
    private final FileConfiguration localeConfig = new YamlConfiguration();


    private LocaleConfig() {
        if (!localeFile.exists()) SimplePMs.getInstance().saveResource(fileName, false);
        reloadLocale();
    }

    public static LocaleConfig getInstance() {
        if (instance == null) instance = new LocaleConfig();
        return instance;
    }

    public FileConfiguration getLocale() { return localeConfig; }

    /**
     * Reloads all locale messages. Logs errors to console if any are found
     */
    public void reloadLocale() {
        try { localeConfig.load(localeFile); }
        catch (IOException | InvalidConfigurationException e) { e.printStackTrace(); }
        Set<String> keys = localeConfig.getKeys(false);
        for (String key : keys) {
            try {
                Message message = Message.valueOf(key);
                message.setMessage(localeConfig.getString(key, message.getMessage()));
            } catch (IllegalArgumentException e) {
                SimplePMs.getInstance().getLogger().warning(Message.LOGGER_INVALID_LOCALE_KEY.getMessage() + key);
            }
        }
    }
}
