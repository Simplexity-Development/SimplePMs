package adhdmc.simplepms.utils;

import adhdmc.simplepms.SimplePMs;
import org.bukkit.NamespacedKey;

public enum SPMKey {
    /**
     * Internal key used for keeping track of a player's spy toggle state
     */
    SPY_TOGGLE(new NamespacedKey(SimplePMs.getInstance(), "spy-toggle")),
    /**
     * Internal key used for keeping track of who a player last messaged
     */
    LAST_MESSAGED(new NamespacedKey(SimplePMs.getInstance(), "last-messaged"));

    final NamespacedKey key;


    SPMKey(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
