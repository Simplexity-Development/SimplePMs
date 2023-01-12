package adhdmc.simplepms.utils;

import org.bukkit.permissions.Permission;

public enum SPMPerm {
    SOCIAL_SPY_TOGGLE(new Permission("spm.socialspy.toggle")),
    RELOAD_CONFIG(new Permission("spm.reload")),
    SEND_MESSAGE(new Permission("spm.send"));

    final Permission perm;

    SPMPerm(Permission perm) {
        this.perm = perm;
    }
    public Permission getPerm(){
        return perm;
    }
}
