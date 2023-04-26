package adhdmc.simplepms.utils;

import org.bukkit.permissions.Permission;

public enum Perm {
    SOCIAL_SPY(new Permission("spm.socialspy")),
    CONSOLE_MESSAGE_SPY(new Permission("spm.consolespy")),
    RECEIVE_OVERRIDE(new Permission("spm.recipient.override")),
    RELOAD_CONFIG(new Permission("spm.reload")),
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
