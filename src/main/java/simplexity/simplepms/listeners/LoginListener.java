package simplexity.simplepms.listeners;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import simplexity.simplepms.SimplePMs;
import simplexity.simplepms.utils.Perm;
import simplexity.simplepms.utils.SPMKey;

import java.util.HashSet;

public class LoginListener implements Listener {
    private final HashSet<Player> spyingPlayers = SimplePMs.getSpyingPlayers();
    private final NamespacedKey spyToggle = SPMKey.SPY_TOGGLE.getKey();

    /**
     * Adds players to the 'spyingPlayers' set on login. Requires the social spy permission, and having toggled socialspy on
     * @param loginEvent PlayerLoginEvent
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerLogin(PlayerLoginEvent loginEvent) {
        Bukkit.getScheduler().runTaskLater(SimplePMs.getInstance(), ()-> {
            Player player = loginEvent.getPlayer();
            PersistentDataContainer playerPDC = player.getPersistentDataContainer();
            if ((playerPDC.getOrDefault(spyToggle, PersistentDataType.BYTE, (byte)0) == (byte)1) &&
                    player.hasPermission(Perm.SOCIAL_SPY.getPerm())) {
                spyingPlayers.add(player);
            }
        },4);
    }
}
