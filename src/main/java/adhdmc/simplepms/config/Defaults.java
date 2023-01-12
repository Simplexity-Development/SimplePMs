package adhdmc.simplepms.config;

import adhdmc.simplepms.SimplePMs;
import org.bukkit.configuration.file.FileConfiguration;

public class Defaults {

    public static void setDefaults(){
        FileConfiguration config = SimplePMs.getInstance().getConfig();
        config.addDefault("plugin-prefix", "<yellow>SimplePM<white> »");
        config.addDefault("console-format", "<dark_red>[<red>Server</red>]</dark_red>");
        config.addDefault("console-format-spy", "<gray>[Server]</gray>");
        config.addDefault("config-reloaded", "<plugin_prefix> <gold>SimplePM Config has been reloaded");
        config.addDefault("error-no-permission", "<plugin_prefix> <red>You do not have permission to do this");
        config.addDefault("error-no-recipient-provided", "<plugin_prefix> <red>You must provide a valid recipient for your message");
        config.addDefault("error-recipient-offline", "<plugin_prefix> There are no players online by the name of <receiver>");
        config.addDefault("error-blank-message", "<plugin_prefix> You cannot send someone a blank message");
        config.addDefault("error-recipient-blocked", "<plugin_prefix> Looks like <receiver> isn't accepting PMs from you at this time");
        config.addDefault("error-player-command", "<plugin_prefix> <red>You must be a player to execute this command.");
        config.addDefault("sending-format", "<gray>[<yellow>You</yellow> <gold>→</gold> <green><receiver></green>]</gray><reset> <message>");
        config.addDefault("receiving-format", "<gray>[<green><sender></green> <gold>→</gold> <yellow>You</yellow>]</gray><reset> <message>");
        config.addDefault("no-user-to-reply", "<red>There is nobody to reply to, sorry. Try <gray>/msg [name]</gray> instead");
        config.addDefault("spy-format", "<dark_gray>[<#989898>PM-Spy</#989898>]</dark_gray> <#989898><sender> → <receiver> <dark_gray>»</dark_gray> <gray><message>");
        config.addDefault("spy-enabled", "<dark_gray>[<#989898>PM-Spy</#989898>]</dark_gray> <green>PM Spy has been enabled");
        config.addDefault("spy-disabled", "<dark_gray>[<#989898>PM-Spy</#989898>]</dark_gray> <gray>PM Spy has been disabled");
    }
}
