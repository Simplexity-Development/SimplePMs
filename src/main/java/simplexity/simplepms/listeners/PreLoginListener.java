package simplexity.simplepms.listeners;

import com.destroystokyo.paper.profile.PlayerProfile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import simplexity.simplepms.saving.Cache;

public class PreLoginListener implements Listener {

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        PlayerProfile player = event.getPlayerProfile();
        Cache.addPlayerSettingsToCache(player.getId());
        Cache.addBlockListToCache(player.getId());
    }

}
