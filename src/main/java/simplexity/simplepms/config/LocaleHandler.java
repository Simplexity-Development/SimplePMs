package simplexity.simplepms.config;

import net.kyori.adventure.text.Component;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import simplexity.simplepms.SimplePMs;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class LocaleHandler {

    public enum Message {
        RELOADED("plugin.reloaded", "<prefix> <gold>SimplePM Config has been reloaded"),
        BLOCKED_PLAYER("plugin.blocked-player", "<gray>You will no longer receive messages from <target></gray>"),
        CONSOLE_SENDER_NAME("console.name", "<dark_red>[<red>Server</red>]</dark_red>"),
        CONSOLE_NAME_SOCIAL_SPY("console.social-spy-name", "[Server]"),
        MESSAGE_SENT("message.sent", "<gray>[<yellow>You</yellow> <gold>→</gold> <green><target></green>]</gray><reset> <message>"),
        MESSAGE_RECEIVED("message.received", "<gray>[<green><initiator></green> <gold>→</gold> <yellow>You</yellow>]</gray><reset> <message>"),
        MESSAGES_ENABLED("message.toggle-enabled", "<green>You are now allowing direct messages</green>"),
        MESSAGES_DISABLED("message.toggle-disabled", "<red>You are no longer allowing direct messages</red>"),
        SOCIAL_SPY_FORMAT("social-spy.format", "<dark_gray>[<#989898>PM-Spy</#989898>]</dark_gray> <#989898><initiator> → <target></#989898> <dark_gray>»</dark_gray> <gray><message>"),
        SOCIAL_SPY_ENABLED("social-spy.enabled", "<dark_gray>[<#989898>PM-Spy</#989898>]</dark_gray> <green>PM Spy has been enabled</green>"),
        SOCIAL_SPY_DISABLED("social-spy.disabled", "<dark_gray>[<#989898>PM-Spy</#989898>]</dark_gray> <gray>PM Spy has been disabled</green>"),
        NO_PERMISSION("error.no-permission", "<red>You do not have permission to do this"),
        NO_RECIPIENT_PROVIDED("error.no-recipient-provided", "<red>You must provide a valid recipient for your message"),
        RECIPIENT_NOT_EXIST("error.recipient-not-exist", "<red>The user <yellow><name></yellow> either does not exist, or is not online</red>"),
        BLANK_MESSAGE("error.blank-message", "You cannot send someone a blank message"),
        TARGET_CANNOT_RECIEVE_MESSAGE("error.cannot-receive-message", "<red>Sorry, looks like that player cannot receive messages right now</red>"),
        CANNOT_REPLY("error.cannot-reply", "<red>There is nobody to reply to, sorry. Try <gray>/msg [name]</gray> instead"),
        YOUR_MESSAGES_CURRENTLY_DISABLED("error.your-messages-currently-disabled", "<red>Sorry, you cannot send a message to someone while your messages are disabled.</red>"),
        CANNOT_MESSAGE_SOMEONE_YOU_BLOCKED("error.cannot-message-someone-you-blocked", "<red>Sorry, you cannot message someone you currently have blocked.</red>"),
        ONLY_PLAYER("error.only-player", "<red>You must be a player to execute this command.");

        private final String path;
        private String message;

        Message(String path, String message) {
            this.path = path;
            this.message = message;
        }

        public String getPath() {
            return path;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

    }

    private static LocaleHandler instance;
    private final String fileName = "locale.yml";
    private final File dataFile = new File(SimplePMs.getInstance().getDataFolder(), fileName);
    private FileConfiguration locale = new YamlConfiguration();

    private LocaleHandler() {
        try {
            dataFile.getParentFile().mkdirs();
            dataFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        reloadLocale();
    }

    public static LocaleHandler getInstance() {
        if (instance == null) {
            instance = new LocaleHandler();
        }
        return instance;
    }

    public void reloadLocale() {
        try {
            locale.load(dataFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        populateLocale();
        sortLocale();
        saveLocale();
    }


    private void populateLocale() {
        Set<Message> missing = new HashSet<>(Arrays.asList(Message.values()));
        for (Message message : Message.values()) {
            if (locale.contains(message.getPath())) {
                message.setMessage(locale.getString(message.getPath()));
                missing.remove(message);
            }
        }

        for (Message message : missing) {
            locale.set(message.getPath(), message.getMessage());
        }


    }

    private void sortLocale() {
        Map<String, Object> sortedMap = new TreeMap<>();
        for (String key : locale.getKeys(true)) {
            sortedMap.put(key, locale.get(key));
        }

        // Clear and rewrite the keys in sorted order
        sortedMap.forEach(locale::set);
    }

    private void saveLocale() {
        try {
            locale.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
