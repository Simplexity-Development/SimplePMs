package simplexity.simplepms.paper.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import simplexity.simplepms.paper.logic.Constants;
import simplexity.simplepms.paper.saving.Cache;

import java.util.UUID;

public class JoinListener implements Listener {

    @EventHandler
    public void onLogin(PlayerJoinEvent joinEvent) {
        Player player = joinEvent.getPlayer();
        UUID playerUuid = player.getUniqueId();
        Cache.populateCache(playerUuid, player, player.hasPermission(Constants.ADMIN_SOCIAL_SPY));
    }
}
