package simplexity.simplepms.config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import simplexity.simplepms.SimplePMs;

import java.io.File;
import java.io.IOException;

public class LocaleHandler {

    private static LocaleHandler instance;
    private final String fileName = "locale.yml";
    private final File localeFile = new File(SimplePMs.getInstance().getDataFolder(), fileName);
    private final FileConfiguration localeConfig = new YamlConfiguration();
    private String pluginPrefix, pluginReloaded, pluginBlockedPlayer, consoleMessagePrefix, consoleSocialSpyPrefix, messageSent,
            messageReceived, socialSpyFormat, socialSpyEnabled, socialSpyDisabled, errorNoPermission,
            errorNoRecipientProvided, errorRecipientOffline, errorBlankMessage, errorRecipientDoesNotHavePerms,
            errorRecipientNotAcceptingPMs, errorOnlyPlayer, errorNoUserToReplyTo, toggleEnabled, toggleDisabled;


    private LocaleHandler() {
        if (!localeFile.exists()) SimplePMs.getInstance().saveResource(fileName, false);
        reloadLocale();
    }

    /**
     * Gets the LocaleConfig instance, instantiates it if it has not been already
     *
     * @return LocaleConfig
     */
    public static LocaleHandler getInstance() {
        if (instance == null) instance = new LocaleHandler();
        return instance;
    }

    /**
     * gets the Locale config
     *
     * @return FileConfiguration
     */
    public FileConfiguration getLocale() {
        return localeConfig;
    }

    /**
     * Reloads all locale messages. Logs errors to console if any are found
     */
    public void reloadLocale() {
        try {
            localeConfig.load(localeFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        pluginPrefix = localeConfig.getString("plugin.prefix", "<yellow>SimplePM<white> »");
        pluginReloaded = localeConfig.getString("plugin.reloaded", "<prefix> <gold>SimplePM Config has been reloaded");
        pluginBlockedPlayer = localeConfig.getString("plugin.blocked-player", "<prefix> <gray>You will no longer receive messages from <target></gray>");
        consoleMessagePrefix = localeConfig.getString("console.message-prefix", "<dark_red>[<red>Server</red>]</dark_red>");
        consoleSocialSpyPrefix = localeConfig.getString("console.social-spy-prefix", "<dark_gray>[Server]</dark_gray>");
        messageSent = localeConfig.getString("message.sent", "<gray>[<yellow>You</yellow> <gold>→</gold> <green><target></green>]</gray><reset> <message>");
        messageReceived = localeConfig.getString("message.received", "<gray>[<green><initiator></green> <gold>→</gold> <yellow>You</yellow>]</gray><reset> <message>");
        socialSpyFormat = localeConfig.getString("social-spy.format", "<dark_gray>[<#989898>PM-Spy</#989898>]</dark_gray> <#989898><initiator> → <target></#989898> <dark_gray>»</dark_gray> <gray><message>");
        socialSpyEnabled = localeConfig.getString("social-spy.enabled", "<dark_gray>[<#989898>PM-Spy</#989898>]</dark_gray> <green>PM Spy has been enabled");
        socialSpyDisabled = localeConfig.getString("social-spy.disabled", "<dark_gray>[<#989898>PM-Spy</#989898>]</dark_gray> <gray>PM Spy has been disabled");
        errorNoPermission = localeConfig.getString("error.no-permission", "<prefix> <red>You do not have permission to do this</red>");
        errorNoRecipientProvided = localeConfig.getString("error.no-recipient-provided", "<prefix> <red>You must provide a valid recipient for your message</red>");
        errorRecipientOffline = localeConfig.getString("error.recipient-offline", "<prefix> There are no players online by the name of <target>");
        errorBlankMessage = localeConfig.getString("error.blank-message", "<prefix> <red>You cannot send someone a blank message</red>");
        errorRecipientDoesNotHavePerms = localeConfig.getString("error.recipient-does-not-have-perms", "<prefix> <gray>Sorry, looks like that player cannot receive messages right now</gray>");
        errorRecipientNotAcceptingPMs = localeConfig.getString("error.recipient-not-accepting-pms", "<prefix> <gray>Sorry, looks like that player isn't accepting messages right now</gray>");
        errorOnlyPlayer = localeConfig.getString("error.only-player", "<prefix> <red>You must be a player to use this command</red>");
        errorNoUserToReplyTo = localeConfig.getString("error.no-user-to-reply-to", "<prefix> <red>There is nobody to reply to, sorry. Try <gray>/msg [name]</gray> instead");
        toggleEnabled = localeConfig.getString("toggle.enabled", "<prefix> <green>You are now allowing direct messages</green>");
        toggleDisabled = localeConfig.getString("toggle.disabled", "<prefix> <red>You are no longer allowing direct messages</red>");

    }

    public String getPluginPrefix() {
        return pluginPrefix;
    }

    public String getPluginReloaded() {
        return pluginReloaded;
    }

    public String getPluginBlockedPlayer() {
        return pluginBlockedPlayer;
    }

    public String getConsoleMessagePrefix() {
        return consoleMessagePrefix;
    }

    public String getConsoleSocialSpyPrefix() {
        return consoleSocialSpyPrefix;
    }

    public String getMessageSent() {
        return messageSent;
    }

    public String getMessageReceived() {
        return messageReceived;
    }

    public String getSocialSpyFormat() {
        return socialSpyFormat;
    }

    public String getSocialSpyEnabled() {
        return socialSpyEnabled;
    }

    public String getSocialSpyDisabled() {
        return socialSpyDisabled;
    }

    public String getErrorNoPermission() {
        return errorNoPermission;
    }

    public String getErrorNoRecipientProvided() {
        return errorNoRecipientProvided;
    }

    public String getErrorRecipientOffline() {
        return errorRecipientOffline;
    }

    public String getErrorBlankMessage() {
        return errorBlankMessage;
    }

    public String getErrorRecipientDoesNotHavePerms() {
        return errorRecipientDoesNotHavePerms;
    }

    public String getErrorRecipientNotAcceptingPMs() {
        return errorRecipientNotAcceptingPMs;
    }

    public String getErrorOnlyPlayer() {
        return errorOnlyPlayer;
    }

    public String getErrorNoUserToReplyTo() {
        return errorNoUserToReplyTo;
    }

    public String getToggleEnabled() {
        return toggleEnabled;
    }

    public String getToggleDisabled() {
        return toggleDisabled;
    }
}
