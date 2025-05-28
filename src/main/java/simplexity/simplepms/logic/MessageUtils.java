package simplexity.simplepms.logic;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import simplexity.simplepms.SimplePMs;
import simplexity.simplepms.config.ConfigHandler;
import simplexity.simplepms.config.Message;

public class MessageUtils {
    private static MessageUtils instance;
    private final MiniMessage miniMessage = SimplePMs.getMiniMessage();

    private MessageUtils() {
    }

    public static MessageUtils getInstance() {
        if (instance == null) instance = new MessageUtils();
        return instance;
    }

    public Component parseMessage(String localeMessage, @NotNull CommandSender initiator,
                                  @NotNull CommandSender target, String messageContent,
                                  boolean socialSpy) {
        Component senderComponent = getCommmandSenderComponent(initiator, socialSpy);
        Component targetComponent = getCommmandSenderComponent(target, socialSpy);
        return miniMessage.deserialize(localeMessage,
                Placeholder.component("initiator", senderComponent),
                Placeholder.component("target", targetComponent),
                Placeholder.unparsed("message", messageContent));
    }

    public Component parseMessage(String localeMessage, @NotNull CommandSender initiator, String command, String messageContent, boolean socialSpy) {
        Component senderComponent = getCommmandSenderComponent(initiator, socialSpy);
        return miniMessage.deserialize(localeMessage,
                Placeholder.component("initiator", senderComponent),
                Placeholder.unparsed("command", command),
                Placeholder.unparsed("message", messageContent));
    }


    private Component getCommmandSenderComponent(CommandSender sender, boolean socialSpy) {
        if (!(sender instanceof Player player)) {
            if (socialSpy) return miniMessage.deserialize(Message.CONSOLE_NAME_SOCIAL_SPY.getMessage());
            return miniMessage.deserialize(Message.CONSOLE_SENDER_NAME.getMessage());
        }
        if (!SimplePMs.isPapiEnabled()) {
            if (socialSpy) return parseName(player, ConfigHandler.getInstance().getSocialSpyFormat());
            return parseName(player, ConfigHandler.getInstance().getNormalFormat());
        }
        if (socialSpy) return parsePapiName(player, ConfigHandler.getInstance().getSocialSpyFormat());
        return parsePapiName(player, ConfigHandler.getInstance().getNormalFormat());
    }

    private Component parseName(Player player, String message) {
        return miniMessage.deserialize(
                message,
                Placeholder.component("displayname", player.displayName()),
                Placeholder.unparsed("username", player.getName())
        );
    }

    private Component parsePapiName(Player player, String message) {
        return miniMessage.deserialize(
                message,
                papiTag(player),
                Placeholder.component("displayname", player.displayName()),
                Placeholder.unparsed("username", player.getName())
        );
    }

    public Player getPlayerFromCommandSender(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            return null;
        }
        return player;
    }

    public TagResolver papiTag(final Player player) {
        if (player == null) return TagResolver.empty();
        return TagResolver.resolver("papi", (argumentQueue, context) -> {
            final String papiPlaceholder = argumentQueue.popOr("PLACEHOLDER API NEEDS ARGUMENT").value();
            final String parsedPlaceholder = PlaceholderAPI.setPlaceholders(player, '%' + papiPlaceholder + '%');
            final Component componentPlaceholder = LegacyComponentSerializer.legacySection().deserialize(parsedPlaceholder);
            return Tag.inserting(componentPlaceholder);
        });
    }

    public Player getPlayer(String name) {
        Player player;
        player = SimplePMs.getInstance().getServer().getPlayer(name);
        if (player != null) {
            return player;
        }
        for (Player listedPlayer : SimplePMs.getPlayers()) {
            String listedPlayerPlainName = PlainTextComponentSerializer.plainText().serialize(listedPlayer.displayName());
            if (listedPlayerPlainName.equalsIgnoreCase(name)) {
                return listedPlayer;
            }
        }
        return null;
    }
}
