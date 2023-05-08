package adhdmc.simplepms.listeners;

import adhdmc.simplepms.events.PrivateMessageEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;

public class MessageRegexListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onSimplePM(PrivateMessageEvent event) {
        // TODO: Make Configurable, add bypass permission.
        Map<String, String> sampleRegexes = new HashMap<>();
        sampleRegexes.put("averysimpleregex", "Censored 1");
        sampleRegexes.put("averysimpleregex2", "Censored 2");
        sampleRegexes.put("averysimpleregex3", "Censored 3");

        String message = event.getMessageContent();
        for (Map.Entry<String, String> entry : sampleRegexes.entrySet()) {
            message = message.replace(entry.getKey(), entry.getValue());
        }
        event.setMessageContent(message);
    }

}
