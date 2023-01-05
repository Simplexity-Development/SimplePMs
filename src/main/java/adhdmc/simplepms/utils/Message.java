package adhdmc.simplepms.utils;

public enum Message {
    //plugin
    PLUGIN_PREFIX("<yellow>SimplePM<white> »"),
    CONSOLE_FORMAT("<dark_red>[<red>Server</red>]</dark_red>"),
    CONFIG_RELOADED("<prefix> <gold>SimplePM Config has been reloaded"),
    //Errors
    ERROR_NO_PERMISSION("<prefix> <red>You do not have permission to do this"),
    ERROR_NO_RECIPIENT_PROVIDED("<prefix> <red>You must provide a valid recipient for your message"),
    ERROR_RECIPIENT_OFFLINE("<prefix> There are no players online by the name of <receiver>"),
    ERROR_BLANK_MESSAGE("<prefix> You cannot send someone a blank message"),
    ERROR_RECIPIENT_BLOCKED("<prefix> Looks like <receiver> isn't accepting PMs from you at this time"),
    ERROR_PLAYER_COMMAND("<prefix> <red>You must be a player to execute this command."),
    //basic
    SENDING_FORMAT("<gray>[<yellow>You</yellow> <gold>→</gold> <green><receiver></green>]</gray><reset> <message>"),
    RECEIVING_FORMAT("<gray>[<green><sender></green> <gold>→</gold> <yellow>You</yellow>]</gray><reset> <message>"),
    NO_USER_TO_REPLY("<red>There is nobody to reply to, sorry. Try <gray>/msg [name]</gray> instead"),
    //socialspy
    SPY_FORMAT("<dark_gray>[<#989898>PM-Spy</#989898>]</dark_gray> <#989898><sender> → <receiver> <dark_gray>»</dark_gray>"),
    SPY_ENABLED("<dark_gray>[<#989898>PM-Spy</#989898>]</dark_gray> <green>PM Spy has been enabled"),
    SPY_DISABLED("<dark_gray>[<#989898>PM-Spy</#989898>]</dark_gray> <gray>PM Spy has been disabled");

    String message;

    Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    private void setMessage(String message) {
        this.message = message;
    }
}
