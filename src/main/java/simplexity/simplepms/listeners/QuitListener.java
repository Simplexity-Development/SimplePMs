package simplexity.simplepms.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import simplexity.simplepms.SimplePMs;

public class QuitListener implements Listener {
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        SimplePMs.getSpyingPlayers().remove(e.getPlayer());
        SimplePMs.getPlayers().remove(e.getPlayer());
    }

}
