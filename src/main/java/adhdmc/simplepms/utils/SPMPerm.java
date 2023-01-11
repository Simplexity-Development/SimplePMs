package adhdmc.simplepms.utils;

import org.bukkit.permissions.Permission;

public enum SPMPerm {
    SOCIAL_SPY_TOGGLE(new Permission("spm.socialspy.toggle"));
    final Permission perm;

    SPMPerm(Permission perm) {
        this.perm = perm;
    }
    public Permission getPerm(){
        return perm;
    }
}
