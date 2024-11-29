package simplexity.simplepms.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import simplexity.simplepms.SimplePMs;
import simplexity.simplepms.objects.PlayerSettings;
import simplexity.simplepms.saving.SQLHandler;

public class LoginListener implements Listener {

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        PlayerSettings playerSettings = SQLHandler.getInstance().getSettings(player.getUniqueId());
        if (playerSettings.socialSpyEnabled()) {
            SimplePMs.getSpyingPlayers().add(player);
        }
        SimplePMs.getPlayers().add(player);


    }

    @EventHandler
    public void onChatEvent(AsyncPlayerChatEvent event) {
        SimplePMs.getInstance().getLogger().info(event.getMessage());
    }

}
