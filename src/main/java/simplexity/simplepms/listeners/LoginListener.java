package simplexity.simplepms.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import simplexity.simplepms.SimplePMs;
import simplexity.simplepms.saving.Cache;
import simplexity.simplepms.saving.objects.PlayerSettings;

public class LoginListener implements Listener {

    @EventHandler
    public void onLogin(PlayerLoginEvent loginEvent) {
        PlayerSettings settings = Cache.getPlayerSettings(loginEvent.getPlayer().getUniqueId());
        if (settings.isSocialSpyEnabled()) SimplePMs.getSpyingPlayers().add(loginEvent.getPlayer());

    }
}
