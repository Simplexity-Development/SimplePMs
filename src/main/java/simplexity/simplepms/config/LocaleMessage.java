package simplexity.simplepms.config;

public enum LocaleMessage {
    CONSOLE_SENDER_NAME("console.name", "<dark_red>[<red>Server</red>]</dark_red>"),
    CONSOLE_NAME_SOCIAL_SPY("console.social-spy-name", "[Server]"),
    RELOADED("plugin.reloaded", "<gold>SimplePMs Config has been reloaded"),
    BLOCKED_PLAYER("plugin.blocking.successful", "<gray>You will no longer receive messages from <name></gray>"),
    PLAYER_NOT_BLOCKED("plugin.blocking.not-blocked", "<red>You do not have this player blocked</red>"),
    NO_LONGER_BLOCKING("plugin.blocking.removed", "<gray>You are no longer blocking <name></gray>"),
    FORMAT_SENT("plugin.format.sent", "<gray>[<yellow>You</yellow> <gold>→</gold> <green><target></green>]</gray><reset> <message>"),
    FORMAT_RECEIVED("plugin.format.received", "<gray>[<green><initiator></green> <gold>→</gold> <yellow>You</yellow>]</gray><reset> <message>"),
    FORMAT_SOCIAL_SPY("plugin.format.social-spy", "<dark_gray>[<gray>Spy</gray>]</dark_gray> <gray><initiator> → <target></gray> <dark_gray>»</dark_gray> <gray><message></gray>"),
    FORMAT_COMMAND_SPY("plugin.format.command-spy", "<dark_gray>[<gray>Spy</gray>]</dark_gray> <gray><initiator> used <yellow>[<gray><message></gray>]</yellow>"),
    SOCIAL_SPY_ENABLED("plugin.social-spy.enabled", "<dark_gray>[<#989898>PM-Spy</#989898>]</dark_gray> <green>PM Spy has been enabled</green>"),
    SOCIAL_SPY_DISABLED("plugin.social-spy.disabled", "<dark_gray>[<#989898>PM-Spy</#989898>]</dark_gray> <gray>PM Spy has been disabled</gray>"),
    MESSAGES_ENABLED("plugin.message.toggle-enabled", "<green>You are now allowing direct messages</green>"),
    MESSAGES_DISABLED("plugin.message.toggle-disabled", "<gray>You are no longer allowing direct messages</gray>"),
    BLOCKLIST_HEADER("plugin.block-list.header", "<aqua>Block List</aqua>"),
    BLOCKLIST_NAME("plugin.block-list.name", "<white>• <name>"),
    BLOCKLIST_REASON("plugin.block-list.reason", "<gray> (Reason: <reason>)</gray>"),
    BLOCKLIST_EMPTY("plugin.block-list.empty", "<gray>You are not blocking anybody</gray>"),
    ERROR_NOT_A_PLAYER("error.only-player", "<red>You must be a player to execute this command."),
    ERROR_NO_PERMISSION("error.no-permission", "<red>You do not have permission to do this"),
    ERROR_NO_RECIPIENT_PROVIDED("error.no-recipient-provided", "<red>You must provide a valid recipient for your message"),
    ERROR_NO_USER_PROVIDED("error.no-user-provided", "<red>You must provide a valid user</red>"),
    ERROR_BLANK_MESSAGE("error.blank-message", "You cannot send someone a blank message"),
    ERROR_RECIPIENT_NOT_EXIST("error.recipient-not-exist", "<red>The user <yellow><name></yellow> either does not exist, or is not online</red>"),
    ERROR_TARGET_CANNOT_RECEIVE_MESSAGE("error.cannot-receive-message", "<red>Sorry, looks like that player cannot receive messages right now</red>"),
    ERROR_CANNOT_REPLY("error.cannot-reply", "<red>There is nobody to reply to, sorry. Try <gray>/msg [name]</gray> instead"),
    ERROR_YOUR_MESSAGES_CURRENTLY_DISABLED("error.your-messages-currently-disabled", "<red>Sorry, you cannot send a message to someone while your messages are disabled.</red>"),
    ERROR_CANNOT_MESSAGE_SOMEONE_YOU_BLOCKED("error.cannot-message-someone-you-blocked", "<red>Sorry, you cannot message someone you currently have blocked.</red>"),
    ERROR_CANNOT_MESSAGE_CONSOLE("error.cannot-message-console", "<red>Sorry, you cannot message the server</red>"),
    ERROR_SOMETHING_WENT_WRONG("error.something-wrong", "<red>Sorry, something went wrong, please check console for more information</red>"),
    ERROR_NAME_NOT_FOUND("error.name-not-found", "Name not found"),
    LOG_ERROR_SOUND_NOT_VALID("console-error.sound-not-valid", "Warning! The sound you have input: '%sound-string%' is invalid! Please be sure you use a sound that is listed on https://jd.papermc.io/paper/1.21.4/org/bukkit/Sound.html"),
    LOG_ERROR_USING_DEFAULT_SOUND("console-error.using-default-sound", "Using %default-sound% until a valid sound is provided"),
    LOG_ERROR_FLOAT_OUT_OF_RANGE("console-error.float-out-of-range", "The number %number% is out of range! Volume and pitch values must be a number between 0 and 2!"),
    LOG_ERROR_USING_DEFAULT_FLOAT("console-error.using-default-float", "Setting to 1.0 until a valid value is provided");


    private final String path;
    private String message;

    LocaleMessage(String path, String message) {
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
