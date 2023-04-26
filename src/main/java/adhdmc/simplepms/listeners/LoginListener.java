package adhdmc.simplepms.listeners;

import adhdmc.simplepms.SimplePMs;
import adhdmc.simplepms.utils.SPMKey;
import adhdmc.simplepms.utils.Perm;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashSet;

public class LoginListener implements Listener {
    private final HashSet<Player> spyingPlayers = SimplePMs.getSpyingPlayers();
    private final NamespacedKey spyToggle = SPMKey.SPY_TOGGLE.getKey();

    @EventHandler
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
