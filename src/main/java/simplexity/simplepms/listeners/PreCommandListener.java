package simplexity.simplepms.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import simplexity.simplepms.config.ConfigHandler;
import simplexity.simplepms.logic.PMHandler;

public class PreCommandListener implements Listener {
    @EventHandler(priority= EventPriority.MONITOR, ignoreCancelled = true)

    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (!ConfigHandler.getInstance().isCommandSpyEnabled()) return;
        String[] args = event.getMessage().split(" ");
        if (!ConfigHandler.getInstance().getCommandsToSpy().contains(args[0])) return;
        String command = args[0].toLowerCase();
        String message = event.getMessage();
        PMHandler.sendCommandSpy(event.getPlayer(), command, message);
    }
}
