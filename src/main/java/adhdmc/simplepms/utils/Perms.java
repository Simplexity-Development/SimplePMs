package adhdmc.simplepms.utils;

import org.bukkit.permissions.Permission;

public enum Perms {
    SOCIAL_SPY(new Permission("spm.socialspy")),
    SOCIAL_SPY_TOGGLE(new Permission("spm.socialspy.toggle")),
    CONSOLE_MESSAGE_SPY(new Permission("spm.consolespy")),
    RELOAD_CONFIG(new Permission("spm.reload")),
    SEND_MESSAGE(new Permission("spm.send")),
    RECEIVE_MESSAGE(new Permission("spm.receive"));

    final Permission perm;

    Perms(Permission perm) {
        this.perm = perm;
    }
    public Permission getPerm(){
        return perm;
    }
}
