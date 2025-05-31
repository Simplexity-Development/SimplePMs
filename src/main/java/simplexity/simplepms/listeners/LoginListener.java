package simplexity.simplepms.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import simplexity.simplepms.SimplePMs;
import simplexity.simplepms.saving.Cache;
import simplexity.simplepms.saving.objects.PlayerSettings;

public class LoginListener implements Listener {

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        Cache.addPlayerSettingsToCache(player.getUniqueId());
        Cache.addBlockListToCache(player.getUniqueId());
        PlayerSettings playerSettings = Cache.getPlayerSettings(player.getUniqueId());
        if (playerSettings.isSocialSpyEnabled()) {
            SimplePMs.getSpyingPlayers().add(player);
        }
    }

}
