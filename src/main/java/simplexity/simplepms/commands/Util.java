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
import org.jetbrains.annotations.NotNull;
import simplexity.simplepms.SimplePMs;
import simplexity.simplepms.config.LocaleHandler;

public class Util {
    private static final MiniMessage miniMessage = SimplePMs.getMiniMessage();

    public static Component parseMessage(String localeMessage, @NotNull CommandSender sender,
                                         @NotNull CommandSender otherSender, String messageContent,
                                         boolean socialSpy) {
        Component senderName = getCommmandSenderComponent(sender, socialSpy);
        Component otherSenderName = getCommmandSenderComponent(otherSender, socialSpy);
        Player sendingPlayer = getPlayerFromCommandSender(sender);
        Player otherSendingPlayer = getPlayerFromCommandSender(otherSender);
        if (SimplePMs.isPapiEnabled()) {
            return miniMessage.deserialize(localeMessage,
                    Placeholder.component("initiator", senderName),
                    Placeholder.component("target", otherSenderName),
                    Placeholder.unparsed("message", messageContent),
                    papiTag(sendingPlayer),
                    papiTag(otherSendingPlayer));
        } else {
            return miniMessage.deserialize(localeMessage,
                    Placeholder.component("initiator", senderName),
                    Placeholder.component("target", otherSenderName),
                    Placeholder.unparsed("message", messageContent));
        }
    }

    private static Component getCommmandSenderComponent(CommandSender sender, boolean socialSpy) {
        if (!(sender instanceof Player player)) {
            if (socialSpy) return miniMessage.deserialize(LocaleHandler.Message.CONSOLE_NAME_SOCIAL_SPY.getMessage());
            return miniMessage.deserialize(LocaleHandler.Message.CONSOLE_SENDER_NAME.getMessage());
        }
        return player.displayName();
    }

    public static Player getPlayerFromCommandSender(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            return null;
        }
        return player;
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
        Player player;
        player = SimplePMs.getInstance().getServer().getPlayer(name);
        if (player != null) {
            return player;
        }
        for (Player listedPlayer : SimplePMs.getPlayers()) {
            //noinspection deprecation
            if (listedPlayer.getDisplayName().equalsIgnoreCase(name)) {
                return listedPlayer;
            }
        }
        return null;
    }
}
