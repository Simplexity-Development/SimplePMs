package simplexity.simplepms.logic;

import org.bukkit.permissions.Permission;

public class Constants {
    public static Permission MESSAGE_BASIC = new Permission("message.basic");
    public static Permission MESSAGE_SEND = new Permission("message.basic.send");
    public static Permission MESSAGE_RECEIVE = new Permission("message.basic.receive");
    public static Permission MESSAGE_TOGGLE = new Permission("message.basic.toggle");
    public static Permission MESSAGE_BLOCK = new Permission("message.basic.block");
    public static Permission RELOAD = new Permission("message.reload");
    public static Permission MESSAGE_ADMIN = new Permission("message.admin");
    public static Permission ADMIN_OVERRIDE = new Permission("message.admin.override");
    public static Permission ADMIN_SOCIAL_SPY = new Permission("message.admin.social-spy");
    public static Permission ADMIN_CONSOLE_SPY = new Permission("message.admin.console-spy");
    public static Permission BYPASS_SOCIAL_SPY = new Permission("message.bypass.social-spy");
    public static Permission BYPASS_COMMAND_SPY = new Permission("message.bypass.command-spy");
}
