package adhdmc.simplepms.utils;

public enum Message {
    //plugin
    /**
     * Default: {@code <yellow>SimplePM<white> »}
     */
    PLUGIN_PREFIX("<yellow>SimplePM<white> »"),
    /**
     * Default: {@code <dark_red>[<red>Server</red>]</dark_red>}
     */
    CONSOLE_FORMAT("<dark_red>[<red>Server</red>]</dark_red>"),
    /**
     * Default: {@code <gray>[Server]</gray>}
     */
    CONSOLE_FORMAT_SPY("<gray>[Server]</gray>"),
    /**
     * Default: {@code <plugin_prefix> <gold>SimplePM Config has been reloaded}
     */
    CONFIG_RELOADED("<plugin_prefix> <gold>SimplePM Config has been reloaded"),
    /**
     * Default: {@code <plugin_prefix> <red>You do not have permission to do this}
     */
    //Errors
    ERROR_NO_PERMISSION("<plugin_prefix> <red>You do not have permission to do this"),
    /**
     * Default: {@code <plugin_prefix> <red>You must provide a valid recipient for your message}
     */
    ERROR_NO_RECIPIENT_PROVIDED("<plugin_prefix> <red>You must provide a valid recipient for your message"),
    /**
     * Default: {@code <plugin_prefix> There are no players online by the name of <target>}
     */
    ERROR_RECIPIENT_OFFLINE("<plugin_prefix> There are no players online by the name of <target>"),
    /**
     * Default: {@code <plugin_prefix> You cannot send someone a blank message}
     */
    ERROR_BLANK_MESSAGE("<plugin_prefix> You cannot send someone a blank message"),
    /**
     * Default: {@code <plugin_prefix> Sorry, looks like that player cannot receive messages right now}
     */
    ERROR_RECIPIENT_NO_PERMS("<plugin_prefix> Sorry, looks like that player cannot receive messages right now"),
    /**
     * Default: {@code <plugin_prefix> <red>You must be a player to execute this command.}
     */
    ERROR_PLAYER_COMMAND("<plugin_prefix> <red>You must be a player to execute this command."),
    /**
     * Default: {@code papi tag requires an argument}
     */
    ERROR_PAPI_NEEDS_ARGUMENT("papi tag requires an argument"),
    //basic
    /**
     * Default: {@code <gray>[<yellow>You</yellow> <gold>→</gold> <green><target></green>]</gray><reset> <message>}
     */
    SENDING_FORMAT("<gray>[<yellow>You</yellow> <gold>→</gold> <green><target></green>]</gray><reset> <message>"),
    /**
     * Default: {@code <gray>[<green><initiator></green> <gold>→</gold> <yellow>You</yellow>]</gray><reset> <message>}
     */
    RECEIVING_FORMAT("<gray>[<green><initiator></green> <gold>→</gold> <yellow>You</yellow>]</gray><reset> <message>"),
    /**
     * Default: {@code <red>There is nobody to reply to, sorry. Try <gray>/msg [name]</gray> instead}
     */
    NO_USER_TO_REPLY("<red>There is nobody to reply to, sorry. Try <gray>/msg [name]</gray> instead"),
    //socialspy
    /**
     * Default: {@code <dark_gray>[<#989898>PM-Spy</#989898>]</dark_gray> <#989898><initiator> → <target> <dark_gray>»</dark_gray> <gray><message>}
     */
    SPY_FORMAT("<dark_gray>[<#989898>PM-Spy</#989898>]</dark_gray> <#989898><initiator> → <target> <dark_gray>»</dark_gray> <gray><message>"),
    /**
     * Default: {@code <dark_gray>[<#989898>PM-Spy</#989898>]</dark_gray> <green>PM Spy has been enabled}
     */
    SPY_ENABLED("<dark_gray>[<#989898>PM-Spy</#989898>]</dark_gray> <green>PM Spy has been enabled"),
    /**
     * Default: {@code <dark_gray>[<#989898>PM-Spy</#989898>]</dark_gray> <gray>PM Spy has been disabled}
     */
    SPY_DISABLED("<dark_gray>[<#989898>PM-Spy</#989898>]</dark_gray> <gray>PM Spy has been disabled"),
    /**
     * Default: {@code Invalid locale key found: }
     */
    LOGGER_INVALID_LOCALE_KEY("Invalid locale key found: "),
    //internal use only - don't add to locale
    PDC_CONSOLE("console");

    String message;

    Message(String message) {
        this.message = message;
    }

    /**
     * Gets the content of this message
     * @return String
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Sets the content of this message
     * @param message String
     */
    public void setMessage(String message) {
        this.message = message;
    }

}
