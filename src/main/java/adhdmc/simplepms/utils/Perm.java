package adhdmc.simplepms.utils;

import org.bukkit.permissions.Permission;

public enum Perm {
    SOCIAL_SPY(new Permission("spm.socialspy")),
    SOCIAL_SPY_TOGGLE(new Permission("spm.socialspy.toggle")),
    CONSOLE_MESSAGE_SPY(new Permission("spm.consolespy")),
    RECEIVE_BYPASS(new Permission("spm.bypass.recipient")),
    RELOAD_CONFIG(new Permission("spm.reload")),
    MESSAGE_BASE(new Permission("spm.message")),
    SEND_MESSAGE(new Permission("spm.message.send")),
    RECEIVE_MESSAGE(new Permission("spm.message.receive"));

    final Permission perm;

    Perm(Permission perm) {
        this.perm = perm;
    }
    public Permission getPerm(){
        return perm;
    }
}
