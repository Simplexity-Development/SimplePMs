package adhdmc.simplepms.utils;

import adhdmc.simplepms.SimplePMs;
import org.bukkit.configuration.file.FileConfiguration;

public enum Message {
    //plugin
    PLUGIN_PREFIX("<yellow>SimplePM<white> »"),
    CONSOLE_FORMAT("<dark_red>[<red>Server</red>]</dark_red>"),
    CONSOLE_FORMAT_SPY("<gray>[Server]</gray>"),
    CONFIG_RELOADED("<plugin_prefix> <gold>SimplePM Config has been reloaded"),
    //Errors
    ERROR_NO_PERMISSION("<plugin_prefix> <red>You do not have permission to do this"),
    ERROR_NO_RECIPIENT_PROVIDED("<plugin_prefix> <red>You must provide a valid recipient for your message"),
    ERROR_RECIPIENT_OFFLINE("<plugin_prefix> There are no players online by the name of <receiver>"),
    ERROR_BLANK_MESSAGE("<plugin_prefix> You cannot send someone a blank message"),
    ERROR_RECIPIENT_BLOCKED("<plugin_prefix> Looks like <receiver> isn't accepting PMs from you at this time"),
    ERROR_PLAYER_COMMAND("<plugin_prefix> <red>You must be a player to execute this command."),
    ERROR_PAPI_NEEDS_ARGUMENT("papi tag requires an argument"),
    //basic
    SENDING_FORMAT("<gray>[<yellow>You</yellow> <gold>→</gold> <green><receiver></green>]</gray><reset> <message>"),
    RECEIVING_FORMAT("<gray>[<green><sender></green> <gold>→</gold> <yellow>You</yellow>]</gray><reset> <message>"),
    NO_USER_TO_REPLY("<red>There is nobody to reply to, sorry. Try <gray>/msg [name]</gray> instead"),
    //socialspy
    SPY_FORMAT("<dark_gray>[<#989898>PM-Spy</#989898>]</dark_gray> <#989898><sender> → <receiver> <dark_gray>»</dark_gray> <gray><message>"),
    SPY_ENABLED("<dark_gray>[<#989898>PM-Spy</#989898>]</dark_gray> <green>PM Spy has been enabled"),
    SPY_DISABLED("<dark_gray>[<#989898>PM-Spy</#989898>]</dark_gray> <gray>PM Spy has been disabled"),
    LOGGER_INVALID_LOCALE_KEY("Invalid locale key found: "),
    //internal use only - don't add to locale
    PDC_CONSOLE("console");

    String message;

    Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static void reloadMessages(){
        FileConfiguration config = SimplePMs.getInstance().getConfig();
        PLUGIN_PREFIX.setMessage(config.getString("plugin-prefix"));
        CONSOLE_FORMAT.setMessage(config.getString("console-format"));
        CONSOLE_FORMAT_SPY.setMessage(config.getString("console-format-spy"));
        CONFIG_RELOADED.setMessage(config.getString("config-reloaded"));
        ERROR_NO_PERMISSION.setMessage(config.getString("error-no-permission"));
        ERROR_NO_RECIPIENT_PROVIDED.setMessage(config.getString("error-no-recipient-provided"));
        ERROR_RECIPIENT_OFFLINE.setMessage(config.getString("error-recipient-offline"));
        ERROR_BLANK_MESSAGE.setMessage(config.getString("error-blank-message"));
        ERROR_RECIPIENT_BLOCKED.setMessage(config.getString("error-recipient-blocked"));
        ERROR_PLAYER_COMMAND.setMessage(config.getString("error-player-command"));
        SENDING_FORMAT.setMessage(config.getString("sending-format"));
        RECEIVING_FORMAT.setMessage(config.getString("receiving-format"));
        NO_USER_TO_REPLY.setMessage(config.getString("no-user-to-reply"));
        SPY_FORMAT.setMessage(config.getString("spy-format"));
        SPY_ENABLED.setMessage(config.getString("spy-enabled"));
        SPY_DISABLED.setMessage(config.getString("spy-disabled"));
    }
}
