package simplexity.simplepms.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import simplexity.simplepms.SimplePMs;
import simplexity.simplepms.objects.PlayerSettings;
import simplexity.simplepms.saving.SqlHandler;

public class LoginListener implements Listener {

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        PlayerSettings playerSettings = SqlHandler.getInstance().getSettings(player.getUniqueId());
        if (playerSettings.socialSpyEnabled()) {
            SimplePMs.getSpyingPlayers().add(player);
        }
        SimplePMs.getPlayers().add(player);
    }

}
