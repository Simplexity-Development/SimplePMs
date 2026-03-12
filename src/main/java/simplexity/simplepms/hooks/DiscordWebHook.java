package simplexity.simplepms.hooks;

import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simplexity.simplepms.SimplePMs;
import simplexity.simplepms.config.ConfigHandler;
import simplexity.simplepms.config.LocaleMessage;
import simplexity.simplepms.logic.MessageUtils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public class DiscordWebHook {

    public static HttpClient client;

    public static void sendWebHook(CommandSender sender, CommandSender recipient, String message) {

        if (client == null) {
            SimplePMs.getInstance().getLogger().log(Level.WARNING, LocaleMessage.LOG_ERROR_WEBHOOK_CLIENT_NOT_CREATED.getMessage());
            createClient();
        }

        String content = MessageUtils.getInstance().format(
                ConfigHandler.getInstance().getWebhookBody(),
                Map.of(
                        "sender", sender.getName(),
                        "sender_display_name", (sender instanceof Player player) ? PlainTextComponentSerializer.plainText().serialize(player.displayName()) : sender.getName(),
                        "sender_uuid", (sender instanceof Player player) ? player.getUniqueId().toString() : "",
                        "recipient", recipient.getName(),
                        "recipient_display_name", (recipient instanceof Player player) ? PlainTextComponentSerializer.plainText().serialize(player.displayName()) : recipient.getName(),
                        "recipient_uuid", (recipient instanceof Player player) ? player.getUniqueId().toString() : "",
                        "message", message,
                        "timestamp", Long.toString(System.currentTimeMillis()/1000)
                )
        );

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(ConfigHandler.getInstance().getWebhookUri())
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(content))
                    .build();

            CompletableFuture<HttpResponse<String>> asyncResponse = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            asyncResponse.thenAccept(response -> {
                if (response.statusCode() < 200 || response.statusCode() >= 300) {
                    //noinspection StringTemplateMigration: String Template is considered preview and may be removed in a future release.
                    SimplePMs.getInstance().getLogger().log(Level.WARNING, "Webhook has failed to send, HTTP Status " + response.statusCode() + " with JSON Body:\n" + response.body());
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createClient() {
        removeClient();
        client = HttpClient.newHttpClient();
    }

    public static void removeClient() {
        if (client == null) return;
        client.close();
        client = null;
    }

}
