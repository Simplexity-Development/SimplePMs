package simplexity.simplepms.config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import simplexity.simplepms.SimplePMs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("ALL")
public class LocaleHandler {

    public enum Message {
        CONSOLE_SENDER_NAME("console.name", "<dark_red>[<red>Server</red>]</dark_red>"),
        CONSOLE_NAME_SOCIAL_SPY("console.social-spy-name", "[Server]"),
        RELOADED("plugin.reloaded", "<gold>SimplePMs Config has been reloaded"),
        BLOCKED_PLAYER("plugin.blocking.successful", "<gray>You will no longer receive messages from <name></gray>"),
        PLAYER_NOT_BLOCKED("plugin.blocking.not-blocked", "<red>You do not have this player blocked</red>"),
        NO_LONGER_BLOCKING("plugin.blocking.removed", "<gray>You are no longer blocking <name></gray>"),
        FORMAT_SENT("plugin.format.sent", "<gray>[<yellow>You</yellow> <gold>→</gold> <green><target></green>]</gray><reset> <message>"),
        FORMAT_RECEIVED("plugin.format.received", "<gray>[<green><initiator></green> <gold>→</gold> <yellow>You</yellow>]</gray><reset> <message>"),
        FORMAT_SOCIAL_SPY("plugin.format.social-spy", "<dark_gray>[<gray>Spy</gray>]</dark_gray> <gray><initiator> → <target></gray> <dark_gray>»</dark_gray> <gray><message></gray>"),
        SOCIAL_SPY_ENABLED("plugin.social-spy.enabled", "<dark_gray>[<#989898>PM-Spy</#989898>]</dark_gray> <green>PM Spy has been enabled</green>"),
        SOCIAL_SPY_DISABLED("plugin.social-spy.disabled", "<dark_gray>[<#989898>PM-Spy</#989898>]</dark_gray> <gray>PM Spy has been disabled</gray>"),
        MESSAGES_ENABLED("plugin.message.toggle-enabled", "<green>You are now allowing direct messages</green>"),
        MESSAGES_DISABLED("plugin.message.toggle-disabled", "<gray>You are no longer allowing direct messages</gray>"),
        BLOCKLIST_HEADER("plugin.block-list.header", "<aqua>Block List</aqua>"),
        BLOCKLIST_NAME("plugin.block-list.name", "<white>• <name>"),
        BLOCKLIST_REASON("plugin.block-list.reason", "<gray> (Reason: <reason>)</gray>"),
        BLOCKLIST_EMPTY("plugin.block-list.empty", "<gray>You are not blocking anybody</gray>"),
        ONLY_PLAYER("error.only-player", "<red>You must be a player to execute this command."),
        NO_PERMISSION("error.no-permission", "<red>You do not have permission to do this"),
        NO_RECIPIENT_PROVIDED("error.no-recipient-provided", "<red>You must provide a valid recipient for your message"),
        NO_USER_PROVIDED("error.no-user-provided", "<red>You must provide a valid user</red>"),
        BLANK_MESSAGE("error.blank-message", "You cannot send someone a blank message"),
        RECIPIENT_NOT_EXIST("error.recipient-not-exist", "<red>The user <yellow><name></yellow> either does not exist, or is not online</red>"),
        TARGET_CANNOT_RECIEVE_MESSAGE("error.cannot-receive-message", "<red>Sorry, looks like that player cannot receive messages right now</red>"),
        CANNOT_REPLY("error.cannot-reply", "<red>There is nobody to reply to, sorry. Try <gray>/msg [name]</gray> instead"),
        YOUR_MESSAGES_CURRENTLY_DISABLED("error.your-messages-currently-disabled", "<red>Sorry, you cannot send a message to someone while your messages are disabled.</red>"),
        CANNOT_MESSAGE_SOMEONE_YOU_BLOCKED("error.cannot-message-someone-you-blocked", "<red>Sorry, you cannot message someone you currently have blocked.</red>"),
        CANNOT_MESSAGE_CONSOLE("error.cannot-message-console", "<red>Sorry, you cannot message the server</red>"),
        SOMETHING_WENT_WRONG("error.something-wrong", "<red>Sorry, something went wrong, please check console for more information</red>");

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
        FileConfiguration newLocale = new YamlConfiguration();
        List<String> keys = new ArrayList<>();
        keys.addAll(locale.getKeys(true));
        Collections.sort(keys);
        for (String key : keys) {
            newLocale.set(key, locale.getString(key));
        }
        locale = newLocale;
    }

    private void saveLocale() {
        try {
            locale.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
