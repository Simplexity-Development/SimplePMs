package simplexity.simplepms.paper.config;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import simplexity.simplepms.paper.SimplePMs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;

public class ConfigHandler {
    private static ConfigHandler instance;

    public static ConfigHandler getInstance() {
        if (instance == null) instance = new ConfigHandler();
        return instance;
    }

    private final Logger logger = SimplePMs.getInstance().getLogger();
    private boolean mysqlEnabled, playersSendToConsole, playersSendToHiddenPlayers, consoleHasSocialSpy,
            commandSpyEnabled, consoleHasCommandSpy, receiveSoundEnabled, sendSoundEnabled, spySoundEnabled;
    private NamespacedKey receiveSound = Registry.SOUNDS.getKey(Sound.BLOCK_NOTE_BLOCK_XYLOPHONE);
    private NamespacedKey sendSound = Registry.SOUNDS.getKey(Sound.ENTITY_ALLAY_ITEM_THROWN);
    private NamespacedKey spySound = Registry.SOUNDS.getKey(Sound.ENTITY_ITEM_FRAME_ROTATE_ITEM);
    private float receivePitch, receiveVolume, sendPitch, sendVolume, spyPitch, spyVolume;
    private String mysqlIp, mysqlName, mysqlUsername, mysqlPassword, normalFormat, socialSpyFormat;
    private final List<String> validNamesForConsole = new ArrayList<>();
    private final HashSet<String> commandsToSpy = new HashSet<>();

    public void loadConfigValues() {
        SimplePMs.getInstance().reloadConfig();
        FileConfiguration config = SimplePMs.getInstance().getConfig();
        LocaleHandler.getInstance().reloadLocale();
        List<String> commands = config.getStringList("command-spy.commands");
        List<String> consoleNames = config.getStringList("valid-console-names");
        updateHashSet(commandsToSpy, commands);
        validateConsoleNames(consoleNames);
        normalFormat = config.getString("format.normal", "<displayname>");
        socialSpyFormat = config.getString("format.social-spy", "<username>");
        mysqlEnabled = config.getBoolean("mysql.enabled", false);
        commandSpyEnabled = config.getBoolean("command-spy.enabled", false);
        mysqlIp = config.getString("mysql.ip", "localhost:3306");
        mysqlName = config.getString("mysql.name", "homes");
        mysqlUsername = config.getString("mysql.username", "username1");
        mysqlPassword = config.getString("mysql.password", "badpassword!");
        playersSendToConsole = config.getBoolean("allow-messaging.console", true);
        playersSendToHiddenPlayers = config.getBoolean("allow-messaging.hidden-players", false);
        consoleHasSocialSpy = config.getBoolean("console-has-social-spy", true);
        consoleHasCommandSpy = config.getBoolean("console-has-command-spy", false);
        receiveSoundEnabled = config.getBoolean("sounds.received.enabled", false);
        sendSoundEnabled = config.getBoolean("sounds.sent.enabled", false);
        spySoundEnabled = config.getBoolean("sounds.spy.enabled", false);
        if (receiveSoundEnabled) loadReceiveSoundInfo(config);
        if (sendSoundEnabled) loadSendSoundInfo(config);
        if (spySoundEnabled) loadSpySoundInfo(config);
    }

    private void updateHashSet(HashSet<String> set, List<String> list) {
        set.clear();
        set.addAll(list);
    }

    private void loadReceiveSoundInfo(FileConfiguration config) {
        String soundString = config.getString("sounds.received.sound", "minecraft:block.note_block.xylophone");
        receiveSound = getValidSound(soundString, Registry.SOUNDS.getKey(Sound.BLOCK_NOTE_BLOCK_XYLOPHONE));
        receivePitch = getValidFloat(config.getDouble("sounds.received.pitch", 1.8));
        receiveVolume = getValidFloat(config.getDouble("sounds.received.volume", 0.5));
    }

    private void loadSendSoundInfo(FileConfiguration config) {
        String soundString = config.getString("sounds.sent.sound", "minecraft:entity.allay.item_thrown");
        sendSound = getValidSound(soundString, Registry.SOUNDS.getKey(Sound.ENTITY_ALLAY_ITEM_THROWN));
        sendPitch = getValidFloat(config.getDouble("sounds.sent.pitch", 1.8));
        sendVolume = getValidFloat(config.getDouble("sounds.sent.volume", 0.5));
    }

    private void loadSpySoundInfo(FileConfiguration config) {
        String soundString = config.getString("sounds.spy.sound", "minecraft:entity.item_frame.rotate_item");
        spySound = getValidSound(soundString, Registry.SOUNDS.getKey(Sound.ENTITY_ITEM_FRAME_ROTATE_ITEM));
        spyPitch = getValidFloat(config.getDouble("sounds.spy.pitch", 1.8));
        spyVolume = getValidFloat(config.getDouble("sounds.spy.volume", 0.5));
    }

    private NamespacedKey getValidSound(String soundString, NamespacedKey defaultSound) {
        NamespacedKey key = NamespacedKey.fromString(soundString);
        if (key == null || Registry.SOUNDS.get(key) == null) {
            String warning = LocaleMessage.LOG_ERROR_SOUND_NOT_VALID.getMessage().replace("%sound-string%", soundString);
            String warning2 = LocaleMessage.LOG_ERROR_USING_DEFAULT_SOUND.getMessage().replace("%default-sound%", defaultSound.getKey());
            logger.warning(warning);
            logger.warning(warning2);
            return defaultSound;
        }
        return key;
    }

    private float getValidFloat(double numberToCheck) {
        if (numberToCheck <= 2 && numberToCheck >= 0) return (float) numberToCheck;
        String warning = LocaleMessage.LOG_ERROR_FLOAT_OUT_OF_RANGE.getMessage().replace("%number%", String.valueOf(numberToCheck));
        logger.warning(warning);
        logger.warning(LocaleMessage.LOG_ERROR_USING_DEFAULT_FLOAT.getMessage());
        return 1.0f;
    }

    private void validateConsoleNames(List<String> list) {
        validNamesForConsole.clear();
        for (String name : list) {
            validNamesForConsole.add(name.toLowerCase());
        }
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

    public HashSet<String> getCommandsToSpy() {
        return commandsToSpy;
    }

    public boolean doesConsoleHaveSocialSpy() {
        return consoleHasSocialSpy;
    }

    public boolean doesConsoleHaveCommandSpy() {
        return consoleHasCommandSpy;
    }

    public boolean isCommandSpyEnabled() {
        return commandSpyEnabled;
    }

    public boolean receivingMessagePlaysSound() {
        return receiveSoundEnabled;
    }

    public boolean sendingMessagePlaysSound() {
        return sendSoundEnabled;
    }

    public boolean messagePlaysSoundForSpy() {
        return spySoundEnabled;
    }

    public String getNormalFormat() {
        return normalFormat;
    }

    public String getSocialSpyFormat() {
        return socialSpyFormat;
    }

    @NotNull
    public NamespacedKey getReceiveSound() {
        return receiveSound;
    }

    @NotNull
    public NamespacedKey getSendSound() {
        return sendSound;
    }

    @NotNull
    public NamespacedKey getSpySound() {
        return spySound;
    }

    public float getReceivePitch() {
        return receivePitch;
    }

    public float getReceiveVolume() {
        return receiveVolume;
    }

    public float getSendPitch() {
        return sendPitch;
    }

    public float getSendVolume() {
        return sendVolume;
    }

    public float getSpyPitch() {
        return spyPitch;
    }

    public float getSpyVolume() {
        return spyVolume;
    }
}
