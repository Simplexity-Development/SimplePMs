package simplexity.simplepms.config;

import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import simplexity.simplepms.SimplePMs;
import simplexity.simplepms.saving.SqlHandler;

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
    private Sound receiveSound, sendSound, spySound;
    private float receivePitch, receiveVolume, sendPitch, sendVolume, spyPitch, spyVolume;
    private String mysqlIp, mysqlName, mysqlUsername, mysqlPassword, normalFormat, socialSpyFormat;
    private List<String> validNamesForConsole = new ArrayList<>();
    private final HashSet<String> commandsToSpy = new HashSet<>();

    public void loadConfigValues() {
        SimplePMs.getInstance().reloadConfig();
        FileConfiguration config = SimplePMs.getInstance().getConfig();
        SqlHandler.getInstance().init();
        LocaleHandler.getInstance().reloadLocale();
        validNamesForConsole.clear();
        List<String> commands = config.getStringList("command-spy.commands");
        updateHashSet(commandsToSpy, commands);
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
        validNamesForConsole = config.getStringList("valid-console-names");
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
        String soundString = config.getString("sounds.received.sound", "BLOCK_NOTE_BLOCK_XYLOPHONE");
        receiveSound = getValidSound(soundString, Sound.BLOCK_NOTE_BLOCK_XYLOPHONE);
        receivePitch = getValidFloat(config.getDouble("sounds.received.pitch", 1.8));
        receiveVolume = getValidFloat(config.getDouble("sounds.received.volume", 0.5));
    }

    private void loadSendSoundInfo(FileConfiguration config){
        String soundString = config.getString("sounds.sent.sound", "ENTITY_ALLAY_ITEM_THROWN");
        sendSound = getValidSound(soundString, Sound.ENTITY_ALLAY_ITEM_THROWN);
        sendPitch = getValidFloat(config.getDouble("sounds.sent.pitch", 1.8));
        sendVolume = getValidFloat(config.getDouble("sounds.sent.volume", 0.5));
    }

    private void loadSpySoundInfo(FileConfiguration config){
        String soundString = config.getString("sounds.spy.sound", "ENTITY_ITEM_FRAME_ROTATE_ITEM");
        spySound = getValidSound(soundString, Sound.ENTITY_ITEM_FRAME_ROTATE_ITEM);
        spyPitch = getValidFloat(config.getDouble("sounds.spy.pitch", 1.8));
        spyVolume = getValidFloat(config.getDouble("sounds.spy.volume", 0.5));
    }

    private Sound getValidSound(String soundString, Sound defaultSound){
        Sound sound;
        try {
            sound = Sound.valueOf(soundString);
        } catch (IllegalArgumentException exception) {
            String warning = Message.SOUND_NOT_VALID.getMessage().replace("%sound-string%", soundString);
            String warning2 = Message.USING_DEFAULT_SOUND.getMessage().replace("%default-sound%", defaultSound.name());
            logger.warning(warning);
            logger.warning(warning2);
            sound = defaultSound;
        }
        return sound;
    }

    private float getValidFloat(double numberToCheck){
        if (numberToCheck <= 2 && numberToCheck >= 0) return (float) numberToCheck;
        String warning = Message.OUT_OF_RANGE.getMessage().replace("%number%", String.valueOf(numberToCheck));
        logger.warning(warning);
        logger.warning(Message.USING_DEFAULT_NUMBER.getMessage());
        return 1.0f;
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

    public boolean receivingMessagePlaysSound(){
        return receiveSoundEnabled;
    }

    public boolean sendingMessagePlaysSound(){
        return sendSoundEnabled;
    }

    public boolean messagePlaysSoundForSpy(){
        return spySoundEnabled;
    }

    public String getNormalFormat() {
        return normalFormat;
    }

    public String getSocialSpyFormat() {
        return socialSpyFormat;
    }

    public Sound getReceiveSound() {
        return receiveSound;
    }

    public Sound getSendSound() {
        return sendSound;
    }

    public Sound getSpySound() {
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
