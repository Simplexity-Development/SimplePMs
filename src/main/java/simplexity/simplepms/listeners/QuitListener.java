package simplexity.simplepms.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import simplexity.simplepms.SimplePMs;

import java.util.HashSet;

public class QuitListener implements Listener {
    private final HashSet<Player> spyingPlayers = SimplePMs.getSpyingPlayers();

    /**
     * Clears players from spyingPlayers when they quit the game
     * @param quitEvent PlayerQuitEvent
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent quitEvent) {
        Player player = quitEvent.getPlayer();
        spyingPlayers.remove(player);
    }
}
