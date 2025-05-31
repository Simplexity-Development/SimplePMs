package simplexity.simplepms.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import simplexity.simplepms.SimplePMs;
import simplexity.simplepms.saving.Cache;

public class QuitListener implements Listener {
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        SimplePMs.getSpyingPlayers().remove(e.getPlayer());
        Cache.removeBlockListFromCache(e.getPlayer().getUniqueId());
        Cache.removePlayerSettingsFromCache(e.getPlayer().getUniqueId());
    }

}
