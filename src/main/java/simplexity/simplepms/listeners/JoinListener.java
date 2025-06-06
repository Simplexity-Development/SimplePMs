package simplexity.simplepms.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import simplexity.simplepms.logic.Constants;
import simplexity.simplepms.saving.Cache;

import java.util.UUID;

public class JoinListener implements Listener {

    @EventHandler
    public void onLogin(PlayerJoinEvent joinEvent) {
        Player player = joinEvent.getPlayer();
        UUID playerUuid = player.getUniqueId();
        Cache.populateCache(playerUuid, player, player.hasPermission(Constants.ADMIN_SOCIAL_SPY));
    }
}
