package simplexity.simplepms.commands;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simplexity.simplepms.SimplePMs;
import simplexity.simplepms.config.LocaleHandler;

public class Util {
    private static final MiniMessage miniMessage = SimplePMs.getMiniMessage();

    public static Component parseMessage(String message, CommandSender sender, CommandSender otherSender, String value) {
        Component senderName;
        Component otherSenderName;
        Player playerSender = null;
        Player otherPlayerSender = null;
        if (sender instanceof Player) {
            playerSender = (Player) sender;
            senderName = playerSender.displayName();
        } else {
            senderName = Component.text(sender.getName());
        }
        if (otherSender instanceof Player) {
            otherPlayerSender = (Player) otherSender;
            otherSenderName = otherPlayerSender.displayName();
        } else {
            otherSenderName = Component.text(otherSender.getName());
        }
        if (SimplePMs.isPapiEnabled()) {
            return miniMessage.deserialize(message,
                    Placeholder.parsed("prefix", LocaleHandler.getInstance().getPluginPrefix()),
                    Placeholder.component("initiator", senderName),
                    Placeholder.component("target", otherSenderName),
                    Placeholder.parsed("value", value),
                    papiTag(playerSender),
                    papiTag(otherPlayerSender));
        } else {
            return miniMessage.deserialize(message,
                    Placeholder.parsed("prefix", LocaleHandler.getInstance().getPluginPrefix()),
                    Placeholder.component("initiator", senderName),
                    Placeholder.component("target", otherSenderName),
                    Placeholder.parsed("value", value));
        }
    }

    public static TagResolver papiTag(final Player player) {
        return TagResolver.resolver("papi", (argumentQueue, context) -> {
            final String papiPlaceholder = argumentQueue.popOr("PLACEHOLDER API NEEDS ARGUMENT").value();
            final String parsedPlaceholder = PlaceholderAPI.setPlaceholders(player, '%' + papiPlaceholder + '%');
            final Component componentPlaceholder = LegacyComponentSerializer.legacySection().deserialize(parsedPlaceholder);
            return Tag.selfClosingInserting(componentPlaceholder);
        });
    }

    public static Player getPlayer(String name) {
        return SimplePMs.getInstance().getServer().getPlayer(name);
    }
}
