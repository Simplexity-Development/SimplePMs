package simplexity.simplepms.paper.logic;

import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.Map;

public class Constants {
    public static Permission MESSAGE_BASIC = new Permission("message.basic", "Base permission for basic functionality",
            PermissionDefault.TRUE, Map.of(
            "message.basic.send", true,
            "message.basic.receive", true,
            "message.basic.toggle", true,
            "message.basic.block", true
    ));
    public static Permission MESSAGE_ADMIN = new Permission("message.admin", "Base permission for the admin commands",
            PermissionDefault.OP, Map.of(
            "message.admin.override", true,
            "message.admin.social-spy", true,
            "message.admin.console-spy", true
    ));

    public static Permission MESSAGE_SEND = new Permission("message.basic.send",
            "Allows sending messages", PermissionDefault.TRUE);
    public static Permission MESSAGE_RECEIVE = new Permission("message.basic.receive",
            "Allows receiving messages", PermissionDefault.TRUE);
    public static Permission MESSAGE_TOGGLE = new Permission("message.basic.toggle",
            "Allows enabling/disabling direct messages", PermissionDefault.TRUE);
    public static Permission MESSAGE_BLOCK = new Permission("message.basic.block",
            "Allows blocking direct messages from and to specific users", PermissionDefault.TRUE);
    public static Permission PLUGIN_RELOAD = new Permission("message.reload",
            "Reloads the Simple PMs plugin", PermissionDefault.OP);
    public static Permission ADMIN_OVERRIDE = new Permission("message.admin.override",
            "Allows messaging someone who has their messages currently disabled, " +
            "has you blocked or does not have permissions to usually see messages", PermissionDefault.OP);
    public static Permission ADMIN_SOCIAL_SPY = new Permission("message.admin.social-spy",
            "Shows the direct messages of other players for moderation purposes", PermissionDefault.OP);
    public static Permission ADMIN_CONSOLE_SPY = new Permission("message.admin.console-spy",
            "Shows the direct messages sent to and from the console sender (from this plugin) " +
            "to other players", PermissionDefault.OP);
    public static Permission BYPASS_SOCIAL_SPY = new Permission("message.bypass.social-spy",
            "Prevents messages to and from this player from showing in social spy, " +
            "does not prevent the message from being formatted on console", PermissionDefault.OP);
    public static Permission BYPASS_COMMAND_SPY = new Permission("message.bypass.command-spy",
            "Stops your commands from being shown to others with command spy (if they are configured to be tracked). " +
            "Does not prevent the message from being formatted on console", PermissionDefault.OP);
}
