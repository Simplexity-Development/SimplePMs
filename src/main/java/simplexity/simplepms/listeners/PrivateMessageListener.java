package simplexity.simplepms.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import simplexity.simplepms.config.ConfigHandler;
import simplexity.simplepms.events.PrivateMessageEvent;
import simplexity.simplepms.hooks.DiscordWebHook;

public class PrivateMessageListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPrivateMessage(PrivateMessageEvent event) {
        if (!ConfigHandler.getInstance().isWebhookEnabled()) return;
        DiscordWebHook.sendWebHook(event.getInitiator(), event.getRecipient(), event.getMessageContent());
    }

}
